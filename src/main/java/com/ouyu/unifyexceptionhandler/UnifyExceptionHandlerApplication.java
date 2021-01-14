package com.ouyu.unifyexceptionhandler;

import com.ouyu.unifyexceptionhandler.entity.HandlerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.net.URL;

@SpringBootApplication
//@RestController
public class UnifyExceptionHandlerApplication {

    /*@GetMapping("/test")
    public void ss(){
        throw  new RuntimeException("sss");
    }

    @Bean
    public ExceptionHandler exceptionHandler(){
        HandlerType handlerType = new HandlerType();
        handlerType.setHandlerType("view");
        return new ExceptionHandler(handlerType);
    }*/


    public static void main(String[] args) {
        SpringApplication.run(UnifyExceptionHandlerApplication.class, args);
    }

}
