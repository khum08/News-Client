package com.yzhk.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		String savedSim = sp.getString("sim", null);
		
		if(sp.getBoolean("protect", false)){
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber();
			
			if(savedSim!=null){
				if(savedSim.equals(currentSim)){
					//safe
				}else{
					//dangerous
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(sp.getString("phone_safe", ""), null, "SIM card is changed!", null, null);
				}
			}
		}			
		
	}

}
