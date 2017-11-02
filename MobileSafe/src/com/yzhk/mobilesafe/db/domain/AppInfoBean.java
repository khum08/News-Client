package com.yzhk.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;


public class AppInfoBean {
	
	private String name;
	private String packageName;
	private Drawable icon;
	private boolean isSystem;
	private boolean isSdApp;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable drawable) {
		this.icon = drawable;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}
	public boolean isSdApp() {
		return isSdApp;
	}
	public void setSdApp(boolean isSdApp) {
		this.isSdApp = isSdApp;
	}
	
	
}
