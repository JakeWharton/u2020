/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jakewharton.u2020.ui.debug;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewDebug;
import com.jakewharton.u2020.ui.ActivityHierarchyServer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>This class can be used to enable the use of HierarchyViewer inside an
 * application. HierarchyViewer is an Android SDK tool that can be used
 * to inspect and debug the user interface of running applications. For
 * security reasons, HierarchyViewer does not work on production builds
 * (for instance phones bought in store.) By using this class, you can
 * make HierarchyViewer work on any device. You must be very careful
 * however to only enable HierarchyViewer when debugging your
 * application.</p>
 *
 * <p>To use this view server, your application must require the INTERNET
 * permission.</p>
 */
public class SocketActivityHierarchyServer implements Runnable, ActivityHierarchyServer {
  /**
   * The default port used to start view servers.
   */
  private static final int VIEW_SERVER_DEFAULT_PORT = 4939;
  private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;

  private static final String LOG_TAG = "ActivityHierarchyServer";

  private static final String VALUE_PROTOCOL_VERSION = "4";
  private static final String VALUE_SERVER_VERSION = "4";

  // Protocol commands
  // Returns the protocol version
  private static final String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
  // Returns the server version
  private static final String COMMAND_SERVER_VERSION = "SERVER";
  // Lists all of the available windows in the system
  private static final String COMMAND_WINDOW_MANAGER_LIST = "LIST";
  // Keeps a connection open and notifies when the list of windows changes
  private static final String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
  // Returns the focused window
  private static final String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";

  private ServerSocket mServer;
  private final int mPort;

  private Thread mThread;
  private ExecutorService mThreadPool;

  private final List<WindowListener> mListeners = new CopyOnWriteArrayList<>();

  private final HashMap<View, String> mWindows = new HashMap<>();
  private final ReentrantReadWriteLock mWindowsLock = new ReentrantReadWriteLock();

  private View mFocusedWindow;
  private final ReentrantReadWriteLock mFocusLock = new ReentrantReadWriteLock();

  /**
   * Creates a new ActivityHierarchyServer associated with the specified window manager on the
   * default local port. The server is not started by default.
   *
   * @see #start()
   */
  public SocketActivityHierarchyServer() {
    mPort = SocketActivityHierarchyServer.VIEW_SERVER_DEFAULT_PORT;
  }

  /**
   * Starts the server.
   *
   * @return True if the server was successfully created, or false if it already exists.
   * @throws java.io.IOException If the server cannot be created.
   */
  public boolean start() throws IOException {
    if (mThread != null) {
      return false;
    }

    mThread = new Thread(this, "Local View Server [port=" + mPort + "]");
    mThreadPool = Executors.newFixedThreadPool(VIEW_SERVER_MAX_CONNECTIONS);
    mThread.start();

    return true;
  }

  @Override public void onActivityCreated(Activity activity, Bundle bundle) {
    String name = activity.getTitle().toString();
    if (TextUtils.isEmpty(name)) {
      name = activity.getClass().getCanonicalName() +
          "/0x" + Integer.toHexString(System.identityHashCode(activity));
    } else {
      name += " (" + activity.getClass().getCanonicalName() + ")";
    }
    mWindowsLock.writeLock().lock();
    try {
      mWindows.put(activity.getWindow().getDecorView().getRootView(), name);
    } finally {
      mWindowsLock.writeLock().unlock();
    }
    fireWindowsChangedEvent();
  }

  @Override public void onActivityStarted(Activity activity) {
  }

  @Override public void onActivityResumed(Activity activity) {
    View view = activity.getWindow().getDecorView();
    mFocusLock.writeLock().lock();
    try {
      mFocusedWindow = view == null ? null : view.getRootView();
      if (mFocusedWindow != null) {
        mFocusedWindow.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
          @Override public void onViewAttachedToWindow(View v) {
          }

          @Override public void onViewDetachedFromWindow(View v) {
            mFocusLock.writeLock().lock();
            try {
              if (v == mFocusedWindow) {
                mFocusedWindow = null;
              }
            } finally {
              mFocusLock.writeLock().unlock();
            }
          }
        });
      }
    } finally {
      mFocusLock.writeLock().unlock();
    }
    fireFocusChangedEvent();
  }

  @Override public void onActivityPaused(Activity activity) {
  }

  @Override public void onActivityStopped(Activity activity) {
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
  }

  @Override public void onActivityDestroyed(Activity activity) {
    mWindowsLock.writeLock().lock();
    try {
      mWindows.remove(activity.getWindow().getDecorView().getRootView());
    } finally {
      mWindowsLock.writeLock().unlock();
    }
    fireWindowsChangedEvent();
  }

  public void run() {
    try {
      mServer = new ServerSocket(mPort, VIEW_SERVER_MAX_CONNECTIONS, InetAddress.getLocalHost());
    } catch (Exception e) {
      Log.w(LOG_TAG, "Starting ServerSocket error: ", e);
    }

    while (mServer != null && Thread.currentThread() == mThread) {
      // Any uncaught exception will crash the system process
      try {
        Socket client = mServer.accept();
        if (mThreadPool != null) {
          mThreadPool.submit(new ViewServerWorker(client));
        } else {
          try {
            client.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } catch (Exception e) {
        Log.w(LOG_TAG, "Connection error: ", e);
      }
    }
  }

  private static boolean writeValue(Socket client, String value) {
    boolean result;
    BufferedSink out = null;
    try {
      out = Okio.buffer(Okio.sink(client));
      out.writeUtf8(value);
      out.writeUtf8CodePoint('\n');
      out.flush();
      result = true;
    } catch (Exception e) {
      result = false;
    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          result = false;
        }
      }
    }
    return result;
  }

  private void fireWindowsChangedEvent() {
    for (WindowListener listener : mListeners) {
      listener.windowsChanged();
    }
  }

  private void fireFocusChangedEvent() {
    for (WindowListener listener : mListeners) {
      listener.focusChanged();
    }
  }

  private void addWindowListener(WindowListener listener) {
    if (!mListeners.contains(listener)) {
      mListeners.add(listener);
    }
  }

  private void removeWindowListener(WindowListener listener) {
    mListeners.remove(listener);
  }

  private interface WindowListener {
    void windowsChanged();

    void focusChanged();
  }

  private static class UncloseableOutputStream extends OutputStream {
    private final OutputStream mStream;

    UncloseableOutputStream(OutputStream stream) {
      mStream = stream;
    }

    public void close() throws IOException {
      // Don't close the stream
    }

    public boolean equals(Object o) {
      return mStream.equals(o);
    }

    public void flush() throws IOException {
      mStream.flush();
    }

    public int hashCode() {
      return mStream.hashCode();
    }

    public String toString() {
      return mStream.toString();
    }

    public void write(byte[] buffer, int offset, int count) throws IOException {
      mStream.write(buffer, offset, count);
    }

    public void write(byte[] buffer) throws IOException {
      mStream.write(buffer);
    }

    public void write(int oneByte) throws IOException {
      mStream.write(oneByte);
    }
  }

  private class ViewServerWorker implements Runnable, WindowListener {
    private Socket mClient;
    private boolean mNeedWindowListUpdate;
    private boolean mNeedFocusedWindowUpdate;

    private final Object[] mLock = new Object[0];

    public ViewServerWorker(Socket client) {
      mClient = client;
      mNeedWindowListUpdate = false;
      mNeedFocusedWindowUpdate = false;
    }

    public void run() {
      BufferedSource in = null;
      try {
        in = Okio.buffer(Okio.source(mClient));

        final String request = in.readUtf8Line();

        String command;
        String parameters;

        int index = request.indexOf(' ');
        if (index == -1) {
          command = request;
          parameters = "";
        } else {
          command = request.substring(0, index);
          parameters = request.substring(index + 1);
        }

        boolean result;
        if (COMMAND_PROTOCOL_VERSION.equalsIgnoreCase(command)) {
          result = writeValue(mClient, VALUE_PROTOCOL_VERSION);
        } else if (COMMAND_SERVER_VERSION.equalsIgnoreCase(command)) {
          result = writeValue(mClient, VALUE_SERVER_VERSION);
        } else if (COMMAND_WINDOW_MANAGER_LIST.equalsIgnoreCase(command)) {
          result = listWindows(mClient);
        } else if (COMMAND_WINDOW_MANAGER_GET_FOCUS.equalsIgnoreCase(command)) {
          result = getFocusedWindow(mClient);
        } else if (COMMAND_WINDOW_MANAGER_AUTOLIST.equalsIgnoreCase(command)) {
          result = windowManagerAutolistLoop();
        } else {
          result = windowCommand(mClient, command, parameters);
        }

        if (!result) {
          Log.w(LOG_TAG, "An error occurred with the command: " + command);
        }
      } catch (IOException e) {
        Log.w(LOG_TAG, "Connection error: ", e);
      } finally {
        if (in != null) {
          try {
            in.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        if (mClient != null) {
          try {
            mClient.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    private boolean windowCommand(Socket client, String command, String parameters) {
      boolean success = true;
      BufferedSink out = null;

      try {
        // Find the hash code of the window
        int index = parameters.indexOf(' ');
        if (index == -1) {
          index = parameters.length();
        }
        final String code = parameters.substring(0, index);
        int hashCode = (int) Long.parseLong(code, 16);

        // Extract the command's parameter after the window description
        if (index < parameters.length()) {
          parameters = parameters.substring(index + 1);
        } else {
          parameters = "";
        }

        final View window = findWindow(hashCode);
        if (window == null) {
          return false;
        }

        // call stuff
        final Method dispatch =
            ViewDebug.class.getDeclaredMethod("dispatchCommand", View.class, String.class,
                String.class, OutputStream.class);
        dispatch.setAccessible(true);
        dispatch.invoke(null, window, command, parameters,
            new UncloseableOutputStream(client.getOutputStream()));

        if (!client.isOutputShutdown()) {
          out = Okio.buffer(Okio.sink(client));
          out.writeUtf8("DONE\n");
          out.flush();
        }
      } catch (Exception e) {
        Log.w(LOG_TAG, "Could not send command " + command +
            " with parameters " + parameters, e);
        success = false;
      } finally {
        if (out != null) {
          try {
            out.close();
          } catch (IOException e) {
            success = false;
          }
        }
      }

      return success;
    }

    private View findWindow(int hashCode) {
      if (hashCode == -1) {
        View window = null;
        mWindowsLock.readLock().lock();
        try {
          window = mFocusedWindow;
        } finally {
          mWindowsLock.readLock().unlock();
        }
        return window;
      }

      mWindowsLock.readLock().lock();
      try {
        for (Entry<View, String> entry : mWindows.entrySet()) {
          if (System.identityHashCode(entry.getKey()) == hashCode) {
            return entry.getKey();
          }
        }
      } finally {
        mWindowsLock.readLock().unlock();
      }

      return null;
    }

    private boolean listWindows(Socket client) {
      boolean result = true;
      BufferedSink out = null;

      try {
        mWindowsLock.readLock().lock();

        out = Okio.buffer(Okio.sink(client));

        for (Entry<View, String> entry : mWindows.entrySet()) {
          out.writeHexadecimalUnsignedLong(System.identityHashCode(entry.getKey()));
          out.writeUtf8CodePoint(' ');
          out.writeUtf8(entry.getValue());
          out.writeUtf8CodePoint('\n');
        }

        out.writeUtf8("DONE.\n");
        out.flush();
      } catch (Exception e) {
        result = false;
      } finally {
        mWindowsLock.readLock().unlock();

        if (out != null) {
          try {
            out.close();
          } catch (IOException e) {
            result = false;
          }
        }
      }

      return result;
    }

    private boolean getFocusedWindow(Socket client) {
      boolean result = true;
      String focusName = null;

      BufferedSink out = null;
      try {
        out = Okio.buffer(Okio.sink(client));

        View focusedWindow = null;

        mFocusLock.readLock().lock();
        try {
          focusedWindow = mFocusedWindow;
        } finally {
          mFocusLock.readLock().unlock();
        }

        if (focusedWindow != null) {
          mWindowsLock.readLock().lock();
          try {
            focusName = mWindows.get(mFocusedWindow);
          } finally {
            mWindowsLock.readLock().unlock();
          }

          out.writeHexadecimalUnsignedLong(System.identityHashCode(focusedWindow));
          out.writeUtf8CodePoint(' ');
          out.writeUtf8(focusName);
        }
        out.writeUtf8CodePoint('\n');
        out.flush();
      } catch (Exception e) {
        result = false;
      } finally {
        if (out != null) {
          try {
            out.close();
          } catch (IOException e) {
            result = false;
          }
        }
      }

      return result;
    }

    public void windowsChanged() {
      synchronized (mLock) {
        mNeedWindowListUpdate = true;
        mLock.notifyAll();
      }
    }

    public void focusChanged() {
      synchronized (mLock) {
        mNeedFocusedWindowUpdate = true;
        mLock.notifyAll();
      }
    }

    private boolean windowManagerAutolistLoop() {
      addWindowListener(this);
      BufferedSink out = null;
      try {
        out = Okio.buffer(Okio.sink(mClient));
        while (!Thread.interrupted()) {
          boolean needWindowListUpdate = false;
          boolean needFocusedWindowUpdate = false;
          synchronized (mLock) {
            while (!mNeedWindowListUpdate && !mNeedFocusedWindowUpdate) {
              mLock.wait();
            }
            if (mNeedWindowListUpdate) {
              mNeedWindowListUpdate = false;
              needWindowListUpdate = true;
            }
            if (mNeedFocusedWindowUpdate) {
              mNeedFocusedWindowUpdate = false;
              needFocusedWindowUpdate = true;
            }
          }
          if (needWindowListUpdate) {
            out.writeUtf8("LIST UPDATE\n");
            out.flush();
          }
          if (needFocusedWindowUpdate) {
            out.writeUtf8("FOCUS UPDATE\n");
            out.flush();
          }
        }
      } catch (Exception e) {
        Log.w(LOG_TAG, "Connection error: ", e);
      } finally {
        if (out != null) {
          try {
            out.close();
          } catch (IOException e) {
            // Ignore
          }
        }
        removeWindowListener(this);
      }
      return true;
    }
  }
}
