package com.OrderMate.annotation;

import com.OrderMate.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: AutoFill
 * Package: com.OrderMate.annotation
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-20 16:41
 */
/*
* 自定义注解
* 用于公共字段自动填充*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 数据库操作类型 UPDATE INSERT
    OperationType value();
}
