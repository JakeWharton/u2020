package com.jakewharton.u2020.ui;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.squareup.spoon.Spoon;

import static android.os.SystemClock.sleep;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public final class DummyTest {
  @Rule public final ActivityTestRule<MainActivity> main =
      new ActivityTestRule<>(MainActivity.class);

  @Test public void noneOfTheThings() {
    Spoon.screenshot(main.getActivity(), "initial_state");
    sleep(SECONDS.toMillis(5)); // Long enough to see some data from mock mode.
    assertTrue(true);
    Spoon.screenshot(main.getActivity(), "displayed_data");
  }
}
