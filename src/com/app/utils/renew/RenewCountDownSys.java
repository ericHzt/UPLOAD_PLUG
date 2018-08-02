/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.app.utils.renew;

import java.util.Date;

/**
 * 续费计时Entity
 * @author Carl
 * @version 2018-04-26
 */
public class RenewCountDownSys{
	
	private static final long serialVersionUID = 1L;
	private Date sysDate;		// 系统时间
	private String countDownTime;		// 计时时间(时：分：秒)
	private Long totalMinutes;		// 系统总运行时间（分钟）
	private Long currentRunTime;		// 本次运行时间（秒）
	private Long hour;		// 时
	private Long minutes;		// 分秒
	private Long second;		// 秒
	private Long count;		// 计数
	
	public RenewCountDownSys() {
		super();
	}


	public Date getSysDate() {
		return sysDate;
	}

	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}
	
	public String getCountDownTime() {
		return countDownTime;
	}

	public void setCountDownTime(String countDownTime) {
		this.countDownTime = countDownTime;
	}
	
	public Long getTotalMinutes() {
		return totalMinutes;
	}

	public void setTotalMinutes(Long totalMinutes) {
		this.totalMinutes = totalMinutes;
	}
	
	public Long getCurrentRunTime() {
		return currentRunTime;
	}

	public void setCurrentRunTime(Long currentRunTime) {
		this.currentRunTime = currentRunTime;
	}
	
	public Long getHour() {
		return hour;
	}

	public void setHour(Long hour) {
		this.hour = hour;
	}
	
	public Long getMinutes() {
		return minutes;
	}

	public void setMinutes(Long minutes) {
		this.minutes = minutes;
	}
	
	public Long getSecond() {
		return second;
	}

	public void setSecond(Long second) {
		this.second = second;
	}
	
	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
	
}