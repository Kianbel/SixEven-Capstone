package com.example.demo.classes;

import java.util.HashSet;
import java.util.List;

public abstract class Expression {
    public abstract int getRuntime();
    public abstract String simplifiedToString();
    public abstract String toString();

    public abstract String checkedToString();
}
