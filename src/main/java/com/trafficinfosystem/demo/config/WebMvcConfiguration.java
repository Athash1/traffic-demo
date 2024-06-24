package com.trafficinfosystem.demo.config;

import com.trafficinfosystem.demo.interceptor.JwtTokenUserInterceptor;
import com.trafficinfosystem.demo.interceptor.JwtTokenAdminInterceptor;
import com.trafficinfosystem.demo.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


import java.util.List;

/**
 * Configure classes to register components related to the web layer
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * Register a custom interceptor
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("Start registering a custom admin interceptor...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/signUp")
                .excludePathPatterns("/user/user/login");
    }


    /**
     * Message converters extending the Spring MVC framework
     * @param converters
     */
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Extended message converter........");
        //Create a message converter object
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        //You need to set up an object converter for the message converter. The object converter can serialize java objects into json data.
        converter.setObjectMapper(new JacksonObjectMapper());

        //Add your own message converter to the container5
        converters.add(0, converter);
    }

    /**
     * Configure CORS
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("set CORS");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8082")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
