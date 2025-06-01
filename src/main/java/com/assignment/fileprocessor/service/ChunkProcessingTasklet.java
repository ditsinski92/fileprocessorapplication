package com.assignment.fileprocessor.service;

import com.assignment.fileprocessor.api.DistributedFs;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class ChunkProcessingTasklet implements Tasklet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkProcessingTasklet.class);

    private DistributedFs fsClient;

    @Override
    public RepeatStatus execute(@NonNull StepContribution contribution, @NonNull ChunkContext chunkContext) throws Exception {
        long fileSize = fsClient.fileLength();
        long chunkSize = fileSize / 100;

        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Integer>> results = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            int nodeId = i + 1;
            long offset = i * chunkSize;
            long size = (i == 99) ? fileSize - offset : chunkSize;

            results.add(executor.submit(() -> {
                RemoteExecutor<Integer> remote = new RemoteExecutor<>();
                return remote.run(nodeId, new RemoteLineProcessor(offset, size));
            }));
        }

        int globalMax = 0;
        for (Future<Integer> future : results) {
            globalMax = Math.max(globalMax, future.get());
        }

        LOGGER.info("Max words in any line = {}", globalMax);
        // Instead of just log the result we can: 1. Save it to DB and query return via REST later; 2. Save it to S3 and create REST endpoint to return it later or check it directly in AWS console
        executor.shutdown();

        return RepeatStatus.FINISHED;
    }
}
