package com.example.demo.classes;

public class RuntimeRunnable implements Runnable {
    MultiLineStatement statement;
    String runtime;
    public RuntimeRunnable(MultiLineStatement statement) {
        this.statement = statement;
    }

    public String getRuntime() {
        return runtime;
    }

    @Override
    public void run() {
        runtime = statement.getRuntime();
    }
}
