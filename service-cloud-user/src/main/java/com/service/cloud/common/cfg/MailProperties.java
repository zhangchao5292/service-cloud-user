//package com.xlhy.servicecenter.common.cfg;
//
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cloud.context.config.annotation.RefreshScope;
///**
// * 邮箱配置相关属性类
// * @author liuqs
// *
// */
//@RefreshScope
//@ConfigurationProperties(prefix = "custom.mail")
//public class MailProperties {
//	/**
//	 * 发送者
//	 */
//	private String from;
//	/**
//	 * 接收列表
//	 */
//	private String[] to;
//	/**
//	 * 抄送列表
//	 */
//	private String[] cc;
//
//	public String getFrom() {
//		return from;
//	}
//
//	public void setFrom(String from) {
//		this.from = from;
//	}
//
//	public String[] getTo() {
//		return to;
//	}
//
//	public void setTo(String[] to) {
//		this.to = to;
//	}
//
//	public String[] getCc() {
//		return cc;
//	}
//
//	public void setCc(String[] cc) {
//		this.cc = cc;
//	}
//}