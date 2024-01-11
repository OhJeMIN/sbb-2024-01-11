package com.ll.sbb20240111;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class HelloJobConfig {
    @Bean
    public Job helloJob(JobRepository jobRepository, Step simpleStep) {
        return new JobBuilder("helloJob", jobRepository)
                .start(simpleStep)
                .build();
    }

    @Bean
    public Step helloStep1(JobRepository jobRepository, Tasklet helloStep1Tasklet,
                           PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("helloJobStep1Tasklet1", jobRepository)
                .tasklet(helloStep1Tasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet helloStep1Tasklet() {
        return ((contribution, chunkContext) -> {
            log.info("Hello World");
            return RepeatStatus.FINISHED;
        });
    }
}
