package com.ouyu.unifyexceptionhandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@RestController
public class UnifyExceptionHandlerApplication {

    /*@GetMapping("/test")
    public void ss(){
        throw  new RuntimeException("我是运行异常");
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
