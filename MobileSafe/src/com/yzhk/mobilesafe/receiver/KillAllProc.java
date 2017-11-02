package com.yzhk.mobilesafe.receiver;

import com.yzhk.mobilesafe.engine.ProcessInfoPro;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class KillAllProc extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ProcessInfoPro.killAllPro(context);
	}
}
