package com.navinfo.opentsp.user.dal.annotation;

import com.navinfo.opentsp.user.dal.profiles.Mybatis;
import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *<p>
 * used on a Entity to declare a table mapping
 *</p>
 * <p>
 * 用于注解类， 指定类和数据库表的映射关系
 * </p>
 *
 * @author wupeng
 * @version 1.0
 * @date 2015-09-25
 * @modify
 * @copyright Navi Tsp
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Mybatis
@Component
public @interface MybatisComponent {

}
