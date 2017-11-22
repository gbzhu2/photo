package com.gbzhu.edu.elp.common.dao.base;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * sql server数据库访问类
 * @author yywang5
 *
 */
public abstract class AbstractSqlserverBaseDao {

	/**
	 * jdbcTemplateSqlserver
	 */
	@Autowired(required=false)
	@Qualifier("jdbcTemplateSqlserver")
	protected JdbcTemplate jdbcTemplateSqlserver;
	
	/**
	 * namedParameterJdbcTemplateSqlserver 带name参数的jdbcTemplate
	 */
	@Autowired(required=false)
	@Qualifier("namedParameterJdbcTemplateSqlserver")
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplateSqlserver;
}
