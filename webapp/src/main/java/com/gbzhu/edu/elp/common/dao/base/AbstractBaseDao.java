package com.gbzhu.edu.elp.common.dao.base;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
/**
 * 基本JdbcTemplate和namedParameterJdbcTemplate
 * @author 金丙传
 *
 */
public abstract class AbstractBaseDao {
	/**
	 * jdbcTemplate
	 */
	@Autowired(required=false)
	@Qualifier("jdbcTemplate")
	protected JdbcTemplate jdbcTemplate;
	
	/**
	 * namedParameterJdbcTemplate 带name参数的jdbcTemplate
	 */
	@Autowired(required=false)
	@Qualifier("namedParameterJdbcTemplate")
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
}
