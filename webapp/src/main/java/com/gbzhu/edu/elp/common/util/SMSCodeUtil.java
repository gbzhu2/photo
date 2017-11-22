package com.gbzhu.edu.elp.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送验证码短信服务
 *
 * @author yywang5
 */
public class SMSCodeUtil {

    /**
     *
     */
    private static Logger logger = LoggerFactory.getLogger(SMSCodeUtil.class.getName());

    /**
     * 短信服务地址
     */
    private String url;

    /**
     * api key
     */
    private String apiKey;

    /**
     * 密码
     */
    private String password;

    /**
     * 模板id
     */
    private String templateid;

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return theapiKey
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * @param apiKey the apiKey
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the templateid
     */
    public String getTemplateid() {
        return templateid;
    }

    /**
     * @param templateid the templateid
     */
    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @param code  验证码
     */
    public static void sendCode(String phone, String code) {
        List<String> phones = new ArrayList<String>();
        phones.add(phone);
        sendCode(phones, code);
    }

    /**
     * 发送验证码
     *
     * @param phones 手机号
     * @param code   code
     */
    public static void sendCode(List<String> phones, String code) {
        SMSCodeUtil smsCodeUtil = (SMSCodeUtil) SpringContextUtil.getApplicationContext().getBean("smsCodeUtil");
        Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", smsCodeUtil.getApiKey());
        params.put("password", smsCodeUtil.getPassword());
        params.put("templateid", smsCodeUtil.getTemplateid());
        params.put("mobile", listToString(phones));
        params.put("templateparams", "{\"code\":\"" + code + "\"}");
        String result = HttpClientUtils.httpPostString(smsCodeUtil.getUrl(), params, 3000);
        logger.info(result);
        String[] codes = result.split(":");
        if (codes.length > 0 && "0".equals(codes[0])) {
            logger.error("发送失败:" + result);
        }
    }

    /**
     * list to String
     *
     * @param strs 字符串
     * @return 字符串
     */
    private static String listToString(List<String> strs) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strs) {
            stringBuilder.append(str).append(",");
        }
        String str = stringBuilder.toString();
        return str.substring(0, str.length() - 1);
    }

}