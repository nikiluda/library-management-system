package com.zhanlin.library_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;


@Configuration
public class PageableConfig {


    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer pageableCustomizer() {
        return pageableResolver -> {
            pageableResolver.setMaxPageSize(100);
            pageableResolver.setFallbackPageable(PageRequest.of(0, 20));

        };
    }
}
