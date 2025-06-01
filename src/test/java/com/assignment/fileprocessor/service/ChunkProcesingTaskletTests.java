package com.assignment.fileprocessor.service;

import com.assignment.fileprocessor.api.DistributedFs;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ChunkProcesingTaskletTests {



/*    @Test
    void testMaxWordsInLine() throws Exception {
        String content = String.join("\n",
                "one two three",
                "four five",
                "six seven eight nine",
                "and this one has six words total"
        );
        DistributedFs fsClient = DistributedFs.getInstance();
        ChunkProcessingTasklet tasklet = new ChunkProcessingTasklet(fsClient) {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                long fileSize = fsClient.fileLength();
                long chunkSize = fileSize / 100;

                ExecutorService executor = Executors.newFixedThreadPool(10);
                List<Future<Integer>> results = new ArrayList<>();

                for (int i = 0; i < 100; i++) {
                    int nodeId = i + 1;
                    long offset = i * chunkSize;
                    long size = (i == 99) ? fileSize - offset : chunkSize;

                    results.add(executor.submit(() -> {
                        return new RemoteExecutor<Integer>()
                                .run(nodeId, new RemoteLineProcessor(offset, size, fsClient));
                    }));
                }

                int globalMax = 0;
                for (Future<Integer> future : results) {
                    globalMax = Math.max(globalMax, future.get());
                }

                System.out.println("Max words in any line = " + globalMax);
                executor.shutdown();

                return RepeatStatus.FINISHED;
            }
        };

        // Act
        tasklet.execute(null, null);
    }*/
}
