package com.ouyu.unifyexceptionhandler.strategy;

import com.ouyu.unifyexceptionhandler.enums.IEnum;

/**
 * <pre>
 * @Auther: ousakai
 * @Date: 2021-01-14 13:33
 * @Description: 统一异常的抽象类策略
 * 修改版本: 1.0
 * 修改日期:
 * 修改人 :
 * 修改说明: 初步完成
 * 复审人 :
 * </pre>
 */
public abstract class ExceptionStrategy extends RuntimeException{
    public abstract IEnum getEnumStrategy();
}
