package com.yzhk.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

public class ProcessInfo {

	private Drawable icon;
	private String processName;
	private String processPackage;
	private boolean isSystemProcess;
	private long memoryUsed;
	private boolean ischecked;
	
	public boolean isIschecked() {
		return ischecked;
	}
	public void setIschecked(boolean ischecked) {
		this.ischecked = ischecked;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getProcessPackage() {
		return processPackage;
	}
	public void setProcessPackage(String processPackage) {
		this.processPackage = processPackage;
	}
	public boolean isSystemProcess() {
		return isSystemProcess;
	}
	public void setSystemProcess(boolean isSystemProcess) {
		this.isSystemProcess = isSystemProcess;
	}
	public long getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	
}
