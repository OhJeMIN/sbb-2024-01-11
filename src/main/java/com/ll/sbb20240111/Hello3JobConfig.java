package com.ll.sbb20240111;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class Hello3JobConfig {
    @Bean
    public Job hello3Job(JobRepository jobRepository, Flow hello3Flow, Step hello3Step3) {
        // JobBuilder를 사용하여 Job을 정의합니다.
        FlowJobBuilder builder = new JobBuilder("hello3Job", jobRepository)
                .start(hello3Flow) // 정의된 Flow를 시작으로 설정합니다.
                .next(hello3Step3) // Flow 실행 후 hello3Step3을 실행합니다.
                .end(); // Job 정의를 마칩니다.
        return builder.build();
    }

    @Bean
    public Flow hello3Flow(Step hello3Step1, Step hello3Step2) {
        // Flow 객체를 생성하고 Step을 Flow로 변환합니다.
        Flow flowStep1 = new FlowBuilder<Flow>("flowHello3Step1").from(hello3Step1).end();
        Flow flowStep2 = new FlowBuilder<Flow>("flowHello3Step2").from(hello3Step2).end();

        // FlowBuilder를 사용하여 병렬 실행을 구성합니다.
        return new FlowBuilder<SimpleFlow>("hello3Flow")
                .split(new SimpleAsyncTaskExecutor())
                .add(flowStep1, flowStep2)
                .end();
    }



    @Bean
    public Step hello3Step1(JobRepository jobRepository, Tasklet hello3Step2Tasklet,
                           PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("hello3JobStep1Tasklet", jobRepository)
                .tasklet(hello3Step2Tasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Step hello3Step2(JobRepository jobRepository, Tasklet hello3Step1Tasklet,
                            PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("hello3JobStep1Tasklet", jobRepository)
                .tasklet(hello3Step1Tasklet, platformTransactionManager)
                .build();
    }

    @Bean
    public Step hello3Step3(JobRepository jobRepository, Tasklet hello3Step3Tasklet,
                            PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("hello3JobStep1Tasklet", jobRepository)
                .tasklet(hello3Step3Tasklet, platformTransactionManager)
                .build();
    }
    @StepScope
    @Bean
    public Tasklet hello3Step1Tasklet() {
        return ((contribution, chunkContext) -> {
            log.info("Hello World 3/1");
            return RepeatStatus.FINISHED;
        });
    }
    @StepScope
    @Bean
    public Tasklet hello3Step2Tasklet() {
        return ((contribution, chunkContext) -> {
            log.info("Hello World 3/2");
            return RepeatStatus.FINISHED;
        });
    }

    @StepScope
    @Bean
    public Tasklet hello3Step3Tasklet() {
        return ((contribution, chunkContext) -> {
            log.info("Hello World 3/3");
            return RepeatStatus.FINISHED;
        });
    }
}
