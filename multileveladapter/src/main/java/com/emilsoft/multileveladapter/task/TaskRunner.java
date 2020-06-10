package com.emilsoft.multileveladapter.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.emilsoft.multileveladapter.callable.AdapterCallable;

public class TaskRunner {

    private final Handler handler = new Handler(Looper.getMainLooper());

    public <T> void execute(AdapterCallable<T> callable) {
        handler.post(new RunnableTask<T>(callable));
    }

    public static class RunnableTask<T> implements Runnable {

        private final AdapterCallable<T> callable;

        public RunnableTask(AdapterCallable<T> callable) {
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                final T result = callable.call();
                callable.onComplete(result);
            } catch (Exception e) {
                Log.e("MultiLevelAdapter", "Exception in MultiLevelAdapter's add item task", e);
            }
        }
    }
}
