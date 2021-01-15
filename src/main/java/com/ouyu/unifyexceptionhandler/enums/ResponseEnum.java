package com.ouyu.unifyexceptionhandler.enums;

/**
 * <pre>
 * @Auther: ousakai
 * @Date: 2021-01-14 14:06
 * @Description: 定义的返回枚举类型
 * 修改版本: 1.0
 * 修改日期:
 * 修改人 :
 * 修改说明: 初步完成
 * 复审人 :
 * </pre>
 */
public enum ResponseEnum implements IEnum {
    SYSTEM_EXCEPTION(1,"系统异常"),
    BUSINESS_EXCEPTION(2,"业务异常"),
    ARGS_EXCEPTION(3,"参数绑定异常");
    private Integer code;
    private String value;

    ResponseEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setValue(String value) {
        this.value = value;
    }}
