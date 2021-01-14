package com.ouyu.unifyexceptionhandler.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <pre>
 * @Auther: ousakai
 * @Date: 2021-01-13 16:15
 * @Description: 处理返回类型 分为json，view
 * 修改版本: 1.0
 * 修改日期:
 * 修改人 :
 * 修改说明: 初步完成
 * 复审人 :
 * </pre>
 */
@ConfigurationProperties(prefix = "unify.exception-handler")
public class HandlerType {
    private Boolean enable;
    private String handlerType;
    private String viewPath;

    public HandlerType() {
        this.handlerType = "json";
        this.viewPath = "classpath:/templates/error.html";
    }


    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getHandlerType() {
        return handlerType;
    }

    public void setHandlerType(String handlerType) {
        this.handlerType = handlerType;
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }
}
