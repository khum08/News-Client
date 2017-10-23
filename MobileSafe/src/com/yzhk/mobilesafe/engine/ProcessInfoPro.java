package com.yzhk.mobilesafe.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

import com.yzhk.mobilesafe.R;
import com.yzhk.mobilesafe.db.domain.ProcessInfo;

public class ProcessInfoPro {

	/**
	 * 获取正在运行的进程数量
	 * @param ctx
	 * @return
	 */
	public static int getProcessCount(Context ctx){
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> processes = am.getRunningAppProcesses();
		return processes.size();
	}
	
	/**
	 * 获取可用内存
	 * @param ctx
	 * @return
	 */
	public static long getAvailMemory(Context ctx){
		ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo(); 
		am.getMemoryInfo(mi);
		return mi.availMem;
	}
	
	/**
	 * 获取总内存大小
	 * @param ctx
	 * @return
	 */
	public static long getTotalMemory(Context ctx){
		
		BufferedReader br = null;
		long parseLong = 0;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/proc/meminfo"))));
			String line1 = br.readLine();
			char[] charArray = line1.toCharArray();
			StringBuffer sb = new StringBuffer();
			for (char c : charArray) {
				if(c>='0' && c<='9'){
					sb.append(c);
				}
			}
			parseLong = Long.parseLong(sb.toString())*1024;
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br!=null){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return parseLong;
	}
	
	/**
	 * 获取提供正运行的应用信息集合
	 * @param ctx
	 * @return 应用集合
	 */
	@SuppressWarnings("deprecation")
	public static List<ProcessInfo> getProcessInfo(Context ctx){
		ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = ctx.getPackageManager();
		List<ProcessInfo> list = new ArrayList<ProcessInfo>();
		
		List<RunningTaskInfo> runningTasks = am.getRunningTasks(300);
		for (RunningTaskInfo runningTaskInfo : runningTasks) {
			ProcessInfo info = new ProcessInfo();
			String pkgName = runningTaskInfo.baseActivity.getPackageName();
			info.setProcessPackage(pkgName);
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(pkgName, 0);
				info.setIcon(applicationInfo.loadIcon(pm));//3
				info.setProcessName(applicationInfo.loadLabel(pm).toString());//5
				if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) !=0){
					info.setSystemProcess(true);//4
				}else{
					info.setSystemProcess(false);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				e.printStackTrace();
				Drawable drawable = ctx.getResources().getDrawable(R.drawable.ic_launcher);
				info.setIcon(drawable);
				info.setSystemProcess(true);
			}
			list.add(info);
		}
		
		
//		List<RunningAppProcessInfo> processesList = am.getRunningAppProcesses();
//		for (RunningAppProcessInfo runningAppProcessInfo : processesList) {
//			ProcessInfo info = new ProcessInfo();
//	    
//			info.setProcessPackage(runningAppProcessInfo.processName);//1
//			
//			android.os.Debug.MemoryInfo memoryUsed= am.getProcessMemoryInfo(new int[]{ runningAppProcessInfo.pid })[0];
//			info.setMemoryUsed(memoryUsed.getTotalPrivateDirty()*1024);//2
//			
//			try {
//				ApplicationInfo applicationInfo = pm.getApplicationInfo(runningAppProcessInfo.processName, 0);
//				info.setIcon(applicationInfo.loadIcon(pm));//3
//				info.setProcessName(applicationInfo.loadLabel(pm).toString());//5
//				if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) !=0){
//					info.setSystemProcess(true);//4
//				}else{
//					info.setSystemProcess(false);
//				}
//				
//			} catch (NameNotFoundException e) {
//				e.printStackTrace();
//				Drawable drawable = ctx.getResources().getDrawable(R.drawable.ic_launcher);
//				info.setIcon(drawable);
//				info.setProcessName(runningAppProcessInfo.processName);
//				info.setProcessPackage(runningAppProcessInfo.processName);
//				info.setSystemProcess(true);
//			}
//			list.add(info);
//		}
		return list;
	}
	
	public static void killPro(Context ctx,ProcessInfo pro){
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		am.killBackgroundProcesses(pro.getProcessPackage());
	}
	
	public static void killAllPro(Context ctx){
		ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		for (RunningAppProcessInfo runningAppProcessInfo : list) {
			if(runningAppProcessInfo.processName.equals(ctx.getPackageName())){
				continue;
			}
			am.killBackgroundProcesses(runningAppProcessInfo.processName);
		}
	}
	
}




