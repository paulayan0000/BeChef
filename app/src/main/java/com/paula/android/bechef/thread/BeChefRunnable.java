package com.paula.android.bechef.thread;

import java.lang.ref.WeakReference;

public class BeChefRunnable implements Runnable {
    private final WeakReference<BeChefRunnableInterface> mWeakInterface;

    public BeChefRunnable(BeChefRunnableInterface anInterface) {
        mWeakInterface = new WeakReference<>(anInterface);
    }

    @Override
    public void run() {
        BeChefRunnableInterface mInterface = mWeakInterface.get();
        if (mInterface != null) mInterface.runTasksOnNewThread();
    }
}