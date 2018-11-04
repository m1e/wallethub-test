package com.ef.log;

public interface LogMapper<SOURCE> {
    Log map(SOURCE source);
}

