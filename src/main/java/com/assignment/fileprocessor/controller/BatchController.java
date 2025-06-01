package com.assignment.fileprocessor.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchController {

    private final JobLauncher jobLauncher;
    private final Job fileProcessingJob;

    public BatchController(JobLauncher jobLauncher, Job fileProcessingJob) {
        this.jobLauncher = jobLauncher;
        this.fileProcessingJob = fileProcessingJob;
    }

    @GetMapping("/start")
    public String startJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(fileProcessingJob, params);
        return "Job started!";
    }

}
