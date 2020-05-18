package com.yumao.easyperlibrary;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 有一个或更多权限获取失败。并且用户在授权弹窗选择了不再询问
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestPermissionsDeniedNeverRequest {
    int value() default 0;
}