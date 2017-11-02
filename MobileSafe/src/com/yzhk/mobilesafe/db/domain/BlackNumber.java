package com.yzhk.mobilesafe.db.domain;

import java.io.Serializable;

public class BlackNumber implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phone;
	private String mode;
	
	@Override
	public String toString() {
		return "BlackNumber [phone=" + phone + ", mode=" + mode + "]";
	}
	
	public BlackNumber(String phone, String mode) {
		super();
		this.phone = phone;
		this.mode = mode;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
}
