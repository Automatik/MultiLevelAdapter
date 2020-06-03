package com.emilsoft.multileveladapter.task;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.emilsoft.multileveladapter.callable.AdapterCallable;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TaskRunner {

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Executor executor = Executors.newCachedThreadPool();

    public <T> void executeAsync(AdapterCallable<T> callable) {
        executor.execute(new RunnableTask<>(handler, callable));
    }

    public static class RunnableTask<T> implements Runnable {

        private final Handler handler;
        private final AdapterCallable<T> callable;

        public RunnableTask(Handler handler, AdapterCallable<T> callable) {
            this.handler = handler;
            this.callable = callable;
        }

        @Override
        public void run() {
            try {
                final T result = callable.call();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callable.onComplete(result);
                    }
                });
            } catch (Exception e) {
                Log.e("MultiLevelAdapter", "Exception in MultiLevelAdapter's add item task", e);
            }
        }
    }
}
