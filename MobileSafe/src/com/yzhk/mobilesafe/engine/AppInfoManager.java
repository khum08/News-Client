package com.yzhk.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yzhk.mobilesafe.db.domain.AppInfoBean;

public class AppInfoManager {
	
	public static List<AppInfoBean> getAppInfo(Context ctx){
		
		List<AppInfoBean> AppInfoList = new ArrayList<AppInfoBean>();
		
		PackageManager pm = ctx.getPackageManager();
		List<PackageInfo> list = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : list) {
			AppInfoBean infoBean = new AppInfoBean();
			infoBean.setPackageName(packageInfo.packageName);
			
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			infoBean.setName(applicationInfo.loadLabel(pm).toString());
			infoBean.setIcon(applicationInfo.loadIcon(pm));
			
			if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM){
				infoBean.setSystem(true);
			}else{
				infoBean.setSystem(false);
			}
			
			if((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE){
				infoBean.setSdApp(true);
			}else{
				infoBean.setSdApp(false);
			}
			
			
			AppInfoList.add(infoBean);
		}
		
		return AppInfoList;
	}
}
