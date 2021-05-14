package com.vekai.batch.annotation;

import org.springframework.context.annotation.Bean;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Bean
public @interface BatchJob {
}
