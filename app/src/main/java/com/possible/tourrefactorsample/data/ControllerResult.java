package com.possible.tourrefactorsample.data;

import java.net.UnknownHostException;

public class ControllerResult<T> {
    private Throwable exception = null;
    private T result = null;

    public ControllerResult(T result) {
        this.result = result;
    }

    public ControllerResult(Throwable exception, T result) {
        this.exception = exception;
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public T getResult() {
        return result;
    }

    public boolean isNetworkError() {
        return exception != null && exception instanceof UnknownHostException;
    }
}
