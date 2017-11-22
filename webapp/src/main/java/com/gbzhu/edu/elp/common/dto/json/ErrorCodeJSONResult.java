package com.gbzhu.edu.elp.common.dto.json;


/**
 * 还回错误码json
 * @author 金丙传
 *
 */
public class ErrorCodeJSONResult extends StandardJSONResult {
	/**
	 * 错误码
	 */
	private String code;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

}
