package com.ouyu.unifyexceptionhandler;


import com.alibaba.fastjson.JSONObject;
import com.ouyu.unifyexceptionhandler.entity.HandlerType;
import com.ouyu.unifyexceptionhandler.entity.Result;
import com.ouyu.unifyexceptionhandler.enums.IEnum;
import com.ouyu.unifyexceptionhandler.enums.ResponseEnum;
import com.ouyu.unifyexceptionhandler.exception.PropertyInvalidException;
import com.ouyu.unifyexceptionhandler.strategy.ExceptionStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * @Auther: ousakai
 * @Date: 2021-01-13 15:42
 * @Description: 统一异常处理
 * 修改版本: 1.0
 * 修改日期:
 * 修改人 :
 * 修改说明: 初步完成
 * 复审人 :
 * </pre>
 */
@ConditionalOnProperty(prefix = "unify.exception-handler", name = "enable", havingValue = "true")
@EnableConfigurationProperties(value = HandlerType.class)
public class ExceptionHandler implements HandlerExceptionResolver {

    private HandlerType handlerType;
    private static final String JSON = "json";
    private static final String VIEW = "view";
    private static final Logger log = LoggerFactory.getLogger(ExceptionHandler.class);
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[]{"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};

    public ExceptionHandler(HandlerType handlerType) {
        this.handlerType = handlerType;
    }
    @Autowired
    private Environment env;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        //如果设置了处理视图类型则直接返回设置的页面
        if (Objects.equals(handlerType.getHandlerType(), VIEW)) {
            String viewPath = handlerType.getViewPath();
            int classpathIndex = -1;
            if (Objects.isNull(viewPath) || (classpathIndex = viewPath.indexOf("classpath:")) == -1) {
                //log.error("检查确保配置文件中unify.exception-handler.view路径在classpath下");
                throw new PropertyInvalidException("检查确保配置文件中unify.exception-handler.view路径在classpath下");
            }
           /* String realPath = viewPath.substring(10 - classpathIndex);
            InputStream resource = getClass().getResourceAsStream(realPath);
            if(resource == null){
                throw  new PropertyInvalidException("检查确保配置文件中unify.exception-handler.view路径正确");
            }*/
            String realPath = null;
            String[] classpath_resource_locations_clone = copyResourceLocations();
            for (String resourceLocation : classpath_resource_locations_clone) {
                if (!viewPath.contains(resourceLocation)) {
                    continue;
                }
                realPath = viewPath.replace(resourceLocation, "");
                break;
            }
            if (Objects.isNull(realPath)) {
                throw new PropertyInvalidException("检查确保配置文件中unify.exception-handler.view路径[" + viewPath + "]正确并在静态资源文件中");
            }
            ModelAndView modelAndView = new ModelAndView(realPath);
            modelAndView.addObject("result",resolverException(e));
            return modelAndView;

        } else if (Objects.equals(handlerType.getHandlerType(), JSON)) {
            Result result = resolverException(e);
            log.error("系统运行出错",result.getData(),e);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            try {
                response.getWriter().print(JSONObject.toJSON(result));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return new ModelAndView();
    }

    private String[] copyResourceLocations() {
        String thymeleaf_location = env.getProperty("spring.thymeleaf.prefix");
        //新增thymeleaf的前缀地址
        String[] classpath_resource_locations_clone = new String[CLASSPATH_RESOURCE_LOCATIONS.length+1];
        classpath_resource_locations_clone[0]=thymeleaf_location;
        System.arraycopy(CLASSPATH_RESOURCE_LOCATIONS,0,classpath_resource_locations_clone,1,CLASSPATH_RESOURCE_LOCATIONS.length);
        return classpath_resource_locations_clone;
    }

    private Result resolverException(Exception e) {
        Result result = new Result();
        if (e instanceof ExceptionStrategy) {
            ExceptionStrategy exceptionStrategy = (ExceptionStrategy) e;
            IEnum iEnum = exceptionStrategy.getEnumStrategy();
            addResult(result, iEnum, iEnum.getValue());

        } else if (e instanceof BindException) {
            BindException bindException = (BindException) e;
            resolverBindException(bindException, result);
        } else {
            resolverSystemException(e, result);
        }
        return result;
    }


    /**
     * 组装返回数据
     *
     * @author ouyu
     */
    private void addResult(Result result, final IEnum iEnum, final String data) {
        result.setCode(iEnum.getCode());
        result.setMessage(iEnum.getValue());
        result.setData(data);
    }

    /**
     * 处理参数绑定异常
     *
     * @author ouyu
     */
    private void resolverBindException(Throwable e, Result result) {
        BindException be = (BindException) e;
        List<FieldError> errorList = be.getBindingResult().getFieldErrors();
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = errorList.size(); i < size; i++) {
            FieldError error = errorList.get(i);
            sb.append(error.getDefaultMessage());
            if (size > 1 && i != size - 1) {
                sb.append("并且");
            }
        }
        addResult(result, ResponseEnum.BUSINESS_EXCEPTION, sb.toString());
    }

    /**
     * 处理系统异常数据
     *
     * @author ouyu
     */
    private void resolverSystemException(Exception e, Result result) {
        addResult(result, ResponseEnum.SYSTEM_EXCEPTION, e.getMessage());
    }
}
