package com.assignment.fileprocessor.config;

import com.assignment.fileprocessor.service.ChunkProcessingTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public Job fileProcessingJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, ChunkProcessingTasklet chunkProcessingTasklet) {
        return new JobBuilder("fileProcessingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(processChunksStep(jobRepository, transactionManager, chunkProcessingTasklet))
                .build();
    }

    @Bean
    public Step processChunksStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ChunkProcessingTasklet chunkProcessingTasklet) {
        return new StepBuilder("processChunksStep", jobRepository)
                .tasklet(chunkProcessingTasklet, transactionManager)
                .build();
    }
}
