package com.jakewharton.u2020.ui.debug;

import android.view.View;
import android.view.ViewGroup;

/**
 * A {@link android.view.ViewGroup.OnHierarchyChangeListener hierarchy change listener} which recursively
 * monitors an entire tree of views.
 */
public final class HierarchyTreeChangeListener implements ViewGroup.OnHierarchyChangeListener {

    /**
     * Wrap a regular {@link android.view.ViewGroup.OnHierarchyChangeListener hierarchy change listener} with one
     * that monitors an entire tree of views.
     */
    public static HierarchyTreeChangeListener wrap(ViewGroup.OnHierarchyChangeListener delegate) {
        return new HierarchyTreeChangeListener(delegate);
    }

    private final ViewGroup.OnHierarchyChangeListener delegate;

    private HierarchyTreeChangeListener(ViewGroup.OnHierarchyChangeListener delegate) {
        if (delegate == null) {
            throw new NullPointerException("Delegate must not be null.");
        }
        this.delegate = delegate;
    }

    @Override
    public void onChildViewAdded(View parent, View child) {
        delegate.onChildViewAdded(parent, child);
        if (child instanceof ViewGroup) {
            ViewGroup childGroup = (ViewGroup) child;
            childGroup.setOnHierarchyChangeListener(this);
            for (int i = 0; i < childGroup.getChildCount(); i++) {
                onChildViewAdded(childGroup, childGroup.getChildAt(i));
            }
        }
    }

    @Override
    public void onChildViewRemoved(View parent, View child) {
        if (child instanceof ViewGroup) {
            ViewGroup childGroup = (ViewGroup) child;
            for (int i = 0; i < childGroup.getChildCount(); i++) {
                onChildViewRemoved(childGroup, childGroup.getChildAt(i));
            }
            childGroup.setOnHierarchyChangeListener(null);
        }
        delegate.onChildViewRemoved(parent, child);
    }
}
