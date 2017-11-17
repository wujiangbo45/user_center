package com.navinfo.opentsp.user.dal.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *<p>
 *      used on a Entity to declare a column mapping
 *</p>
 * <p>
 *     注解类的属性， 用来指定属性对应的表中的列
 * </p>
 * @author wupeng
 * @version 1.0
 * @date 2015-09-25
 * @modify
 * @copyright Navi Tsp
 */
@Documented
@Target({FIELD})
@Retention(RUNTIME)
public @interface Column {

    /**
     *
     * column name
     *
     * @return
     */
    String name() default "";

    /**
     *
     * value can be null
     *
     * @return
     */
    boolean nullable() default true;

    /**
     *
     * the same with name()
     *
     * @return
     */
    String value() default "";

}
