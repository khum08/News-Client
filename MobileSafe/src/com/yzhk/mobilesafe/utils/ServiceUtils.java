package com.yzhk.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	
	public static boolean isServiceRunning(Context cxt,String serviceName){
		boolean flag = false;
		
		ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : services) {
			String className = runningServiceInfo.service.getClassName();
			if(serviceName.equals(className)){
				flag = true;
			}
			
		}
		return flag;
	}

}
