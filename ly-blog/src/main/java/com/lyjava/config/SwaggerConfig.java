package com.lyjava.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
            	//扫描的包，需要放到文档上的东西
                .apis(RequestHandlerSelectors.basePackage("com.lyjava.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        //封装一些信息，下面要用
        Contact contact = new Contact("创世神", "http://www.baidu.com", "2529098157@qq.com");
        return new ApiInfoBuilder()
                .title("接口文档")//文档标题
                .description("神说要有光")//文档描述
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}