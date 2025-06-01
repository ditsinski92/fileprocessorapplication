package com.assignment.fileprocessor.service;

import com.assignment.fileprocessor.api.DistributedFs;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class DistributedFSClient implements DistributedFs {

    private final DistributedFs fs = DistributedFs.getInstance();

    public long getFileLength() {
        return fs.fileLength();
    }

    @Override
    public long fileLength() {
        return 0;
    }

    public InputStream getData(long offset) {
        return fs.getData(offset);
    }
}
