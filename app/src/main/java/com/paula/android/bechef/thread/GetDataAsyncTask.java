package com.paula.android.bechef.thread;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

public class GetDataAsyncTask<D> extends AsyncTask<Void, Void, D> {
    private WeakReference<GetDataTaskCallback<D>> mWeakCallback;

    public GetDataAsyncTask(GetDataTaskCallback<D> callback) {
        mWeakCallback = new WeakReference<>(callback);
    }

    @Override
    protected D doInBackground(Void... voids) {
        if (this.isCancelled()) {
            mWeakCallback = null;
            return null;
        }
        GetDataTaskCallback<D> mCallback = mWeakCallback.get();
        if (mWeakCallback != null) return mCallback.doInBackground();
        return null;
    }

    @Override
    protected void onPostExecute(D bean) {
        super.onPostExecute(bean);
        GetDataTaskCallback<D> mCallback = mWeakCallback.get();
        if (mCallback != null) {
            mCallback.onCompleted(bean);
        }
    }
}