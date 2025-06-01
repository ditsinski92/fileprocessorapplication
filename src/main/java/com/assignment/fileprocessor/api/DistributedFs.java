package com.assignment.fileprocessor.api;

import java.io.InputStream;

public interface DistributedFs {
    long fileLength();
    InputStream getData(long offset);

    static DistributedFs getInstance() {
        return null;
    }
}
