package com.powernode.anno;

import java.lang.annotation.*;

/**
 * @author wangjunchen
 * 日志记录注解，所有对数据库中数据的增加，删除，修改等操作都会记录到数据库中
 */
@Target(ElementType.METHOD)//注解使用在方法
@Retention(RetentionPolicy.RUNTIME)//元注解，运行时生效
@Documented
@Inherited
public @interface Log {
    //String是属性operation的类型
    //operation()是属性的名称
    //default "" -> operation属性的默认值
    String operation() default "";
}
