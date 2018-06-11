package com.resultant.task;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.resultant.task.filter.LogFilter;
import com.resultant.task.handler.RESTAccessDeniedHandler;
import com.resultant.task.handler.RESTAuthenticationEntryPoint;
import com.resultant.task.handler.RESTAuthenticationFailureHandler;
import com.resultant.task.handler.RESTAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.Executor;

@SpringBootApplication
@ComponentScan(basePackages = {"com.resultant.task.controller", "com.resultant.task.service",
        "com.resultant.task.config", "com.resultant.task.handler"})
@EnableJpaRepositories("com.resultant.task.dao")
@EnableAutoConfiguration
@EntityScan(
        basePackageClasses = { TemplateApplication.class, Jsr310JpaConverters.class }
)
@PropertySource("classpath:appenv.properties")
@EnableAsync
public class TemplateApplication {

    @Value("${template.date-format}")
    private String dateFormat;

    @Value("${template.log-id}")
    private String logId;


    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }


    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();
        return executor;
    }

    @Bean
    public FilterRegistrationBean commonFilterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setName("CommonFilter");
        registration.setOrder(2);
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(false);
        registration.setFilter(loggingFilter);
        return registration;
    }

    @Bean
    public FilterRegistrationBean logFilterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setName("LogFilter");
        registration.setOrder(1);
        LogFilter logFilter = new LogFilter(logId);
        registration.setFilter(logFilter);
        return registration;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public RESTAuthenticationSuccessHandler getSuccessHandler(){
        return new RESTAuthenticationSuccessHandler();
    }

    @Bean
    public RESTAuthenticationEntryPoint getEntryPoint(){
        return new RESTAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler(){
        return new RESTAccessDeniedHandler();
    }

    @Bean
    public RESTAuthenticationFailureHandler getFailureHandler(){
        return new RESTAuthenticationFailureHandler();
    }

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.setVisibility(objectMapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.ANY));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
        /*javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());*/
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }
}