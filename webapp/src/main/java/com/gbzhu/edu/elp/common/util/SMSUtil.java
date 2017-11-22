package com.gbzhu.edu.elp.common.util;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.gbzhu.edu.elp.common.exception.ELPSysException;

/**
 * 短信发送工具类
 * @author yywang5
 *
 */
public class SMSUtil {
	
	/**
	 * 短信服务地址
	 */
	private String smsHost;
	
	/**
	 * 短信服务端口
	 */
	private int smsPort;
	
	/**
	 * 应用id
	 */
	private String appid;
	
	/**
	 * 调用发送短信服务器ip
	 */
	private String sendIP;
	
	/**
	 * 短信后缀
	 */
	private String smsSuffix;
	
	/**
	 * @return the smsHost
	 */
	public String getSmsHost() {
		return smsHost;
	}

	/**
	 * @param smsHost the smsHost to set
	 */
	public void setSmsHost(String smsHost) {
		this.smsHost = smsHost;
	}


	/**
	 * @return the smsPort
	 */
	public int getSmsPort() {
		return smsPort;
	}

	/**
	 * @param smsPort the smsPort to set
	 */
	public void setSmsPort(int smsPort) {
		this.smsPort = smsPort;
	}

	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}

	/**
	 * @param appid the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	/**
	 * @return the sendIP
	 */
	public String getSendIP() {
		return sendIP;
	}

	/**
	 * @param sendIP the sendIP to set
	 */
	public void setSendIP(String sendIP) {
		this.sendIP = sendIP;
	}
	
	/**
	 * @return the smsSuffix
	 */
	public String getSmsSuffix() {
		return smsSuffix;
	}

	/**
	 * @param smsSuffix the smsSuffix to set
	 */
	public void setSmsSuffix(String smsSuffix) {
		this.smsSuffix = smsSuffix;
	}

	/**
	 * 消息模板
	 */
	private static final String MSG_TEL="<?xml version='1.0' encoding='UTF-8'?>"
			+ "<atpacket domain='web' type='event'>"
			+ "<cmd id='send_sm' node='%s'>"
			+ "<para name='appid'  value='%s' />"
			+ "<para name='src'  value='' />"
			+ "<para name='dst'  value='%s' />"
			+ "<para name='context'  value='【%s】%s' />"
			+ "</cmd></atpacket>";

	/**
	 * 发送短信接口
	 * @param mobileNum 手机号
	 * @param context 短信内容
	 */
	public static void sendMsg(String mobileNum,String context){
		SMSUtil smsUtil=(SMSUtil)SpringContextUtil.getApplicationContext().getBean("smsUtil");
		String length = "0x00000000";
		Socket socket=null;
		PrintWriter write=null;
		try {
			socket = new Socket(smsUtil.getSmsHost(),smsUtil.getSmsPort());
			write = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"gb2312"));
			String msg=String.format(MSG_TEL, smsUtil.getSendIP(),smsUtil.getAppid(),mobileNum,smsUtil.getSmsSuffix(),context);
			byte[] bytes = msg.getBytes("gb2312");
			length = length.substring(0, length.length()-Integer.toHexString(bytes.length).length())+Integer.toHexString(bytes.length);
			msg = length+msg;
			write.print(msg);
			write.flush();
		} catch (UnknownHostException e) {
			 throw new ELPSysException("短信发送异常", e);
		} catch (IOException e) {
			throw new ELPSysException("短信发送异常", e);
		}finally{
			if(write!=null){
				write.close();
			}
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					throw new ELPSysException("短信发送异常,socket close failed!", e);
				}
			}
		}
	}	
}