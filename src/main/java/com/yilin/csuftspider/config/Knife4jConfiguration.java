//package com.yilin.csuftspider.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;
//
//@Configuration
//@EnableSwagger2WebMvc
//public class Knife4jConfiguration {
//
//    @Bean(value = "dockerBean")
//    public Docket dockerBean() {
//        //指定使用Swagger2规范
//        Docket docket=new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(new ApiInfoBuilder()
//                //描述字段支持Markdown语法
//                .description("# CSUFT SPIDER APIs")
//                .termsOfServiceUrl("http://yctor.icu/")
//                .contact("1322780122@11.com")
//                .version("1.0")
//                .build())
//                //分组名称
//                .groupName("CSUFTSPIDER")
//                .select()
//                //这里指定Controller扫描包路径
//                .apis(RequestHandlerSelectors.basePackage("com.yilin.csuftspider.controller"))
//                .paths(PathSelectors.any())
//                .build();
//        return docket;
//    }
//}