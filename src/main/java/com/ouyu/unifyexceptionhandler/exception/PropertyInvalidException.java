package com.ouyu.unifyexceptionhandler.exception;

/**
 * <pre>
 * @Auther: ousakai
 * @Date: 2021-01-13 16:47
 * @Description: 配置文件验证异常
 * 修改版本: 1.0
 * 修改日期:
 * 修改人 :
 * 修改说明: 初步完成
 * 复审人 :
 * </pre>
 */
public class PropertyInvalidException extends RuntimeException {
    public PropertyInvalidException(String message) {
        super(message);
    }
}
