package com.assignment.fileprocessor.service;

import com.assignment.fileprocessor.api.DistributedFs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Supplier;

public class RemoteLineProcessor implements Supplier<Integer> {
    private final long offset;
    private final long chunkSize;

    public RemoteLineProcessor(long offset, long chunkSize) {
        this.offset = offset;
        this.chunkSize = chunkSize;
    }

    @Override
    public Integer get() {
        try (InputStream inputStream = DistributedFs.getInstance().getData(offset)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            long bytesRead = 0;
            int maxWords = 0;

            // Buffer to read chunk
            char[] buffer = new char[8192];
            StringBuilder chunkData = new StringBuilder();

            while (bytesRead < chunkSize) {
                int read = reader.read(buffer, 0, (int) Math.min(buffer.length, chunkSize - bytesRead));
                if (read == -1) break;
                chunkData.append(buffer, 0, read);
                bytesRead += read;
            }

            String chunk = chunkData.toString();

            // Handle partial first line
            if (offset != 0) {
                int firstNewline = chunk.indexOf('\n');
                if (firstNewline != -1) {
                    chunk = chunk.substring(firstNewline + 1);
                } else {
                    // No newline at all → whole chunk is partial line → skip it
                    return 0;
                }
            }

            // Handle partial last line
            int lastNewline = chunk.lastIndexOf('\n');
            if (lastNewline != -1 && offset + chunkSize < DistributedFs.getInstance().fileLength()) {
                chunk = chunk.substring(0, lastNewline + 1);
            }

            // Process remaining chunk safely
            String[] lines = chunk.split("\n");
            for (String line : lines) {
                int wordCount = line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
                maxWords = Math.max(maxWords, wordCount);
            }

            return maxWords;

        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
