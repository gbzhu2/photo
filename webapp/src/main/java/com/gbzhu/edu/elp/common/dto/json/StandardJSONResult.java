package com.gbzhu.edu.elp.common.dto.json;

/**
 * 标准JSON结果返回格式超类
 * @author 黄伟
 *
 */
public class StandardJSONResult {

	/**
	 * 处理结果
	 */
	private JSONResult result;
	
	/**
	 * 返回消息
	 */
	private String message;

	/**
	 * 
	 * @return 处理结果
	 */
	public JSONResult getResult() {
		return result;
	}

	/**
	 * 
	 * @param result 处理结果
	 */
	public void setResult(JSONResult result) {
		this.result = result;
	}

	/**
	 * 
	 * @return 返回消息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message 返回消息
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 返回一个不含 message 的标准成功的JSON结果
	 * @return 标准JSON结果
	 */
	public static StandardJSONResult getSuccessInstance(){
		return getSuccessInstance(null);
	}
	
	/**
	 * 返回一个标准成功的JSON结果
	 * @param message 返回JSON中的消息
	 * @return	标准JSON结果
	 */
	public static StandardJSONResult getSuccessInstance(String message){
		StandardJSONResult result = new StandardJSONResult();
		result.setResult(JSONResult.success);
		if(message != null){
			result.setMessage(message);
		}
		return result;
	}
	
	
	/**
	 * 返回一个不含 message 的标准成功的JSON结果
	 * @return 标准JSON结果
	 */
	public static StandardJSONResult getFailedInstance(){
		return getFailedInstance(null);
	}
	
	/**
	 * 返回一个标准失败的JSON结果
	 * @param message 返回JSON中的消息
	 * @return	标准JSON结果
	 */
	public static StandardJSONResult getFailedInstance(String message){
		StandardJSONResult result = new StandardJSONResult();
		result.setResult(JSONResult.failed);
		if(message != null){
			result.setMessage(message);
		}
		return result;
	}
	
}
