package com.emilsoft.multileveladapter.callable;

import java.util.concurrent.Callable;

public interface AdapterCallable<V> extends Callable<V> {

    public void onComplete(V result);

}
