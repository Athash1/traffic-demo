package com.trafficinfosystem.demo.config;

import com.trafficinfosystem.demo.interceptor.JwtTokenUserInterceptor;
import com.trafficinfosystem.demo.interceptor.JwtTokenAdminInterceptor;
import com.trafficinfosystem.demo.json.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.List;

/**
 * 配置类，注册web层相关组件
 */
@Configuration
@Slf4j
public class WebMvcConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login")
                .excludePathPatterns("/user/shop/status");
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docketAdmin() {
        log.info("Prepare to generate interface documentation。。。");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("sky-takeaway-project-interface-documentation")
                .version("2.0")
                .description("sky-takeaway-project-interface-documentation")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("admin")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.admin"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean
    public Docket docketUser() {
        log.info("Prepare to generate interface documentation。。。");
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("sky-takeaway-project-interface-documentation")
                .version("2.0")
                .description("sky-takeaway-project-interface-documentation")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .groupName("user")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sky.controller.user"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("Start setting up static resource mappings。。。");
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
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

        //Add your own message converter to the container
        converters.add(0, converter);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("设置 CORS");
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
