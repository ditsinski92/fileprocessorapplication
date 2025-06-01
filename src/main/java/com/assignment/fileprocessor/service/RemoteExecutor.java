package com.assignment.fileprocessor.service;

import java.util.function.Supplier;

public class RemoteExecutor<T> {
    public T run(int serviceId, Supplier<T> func) {

        return func.get();
    }
}
