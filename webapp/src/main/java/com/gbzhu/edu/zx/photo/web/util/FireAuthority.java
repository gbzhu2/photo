package com.gbzhu.edu.zx.photo.web.util;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.gbzhu.edu.zx.photo.web.dto.AuthorityType;
import com.gbzhu.edu.zx.photo.web.dto.LoginTypeEnum;

/**
 * annotation类, 用于标注到需要权限验证的方法
 * 
 * @author zhuguobiao
 * @version 
 */

@Target({ METHOD, TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FireAuthority {
	
	/**
	 * 模块code
	 * @return 
	 */
	public String[] module_code();
	
	/**
	 * 所具有的权限
	 * @return
	 */
	public AuthorityType[] authorityTypes() default AuthorityType.ADMIN_ALLOW;
	
	/**
	 * 两种登录方式
	 * @return
	 */
	public LoginTypeEnum loginType() default LoginTypeEnum.page;
	
}