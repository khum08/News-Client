package com.yzhk.mobilesafe.receiver;

import com.yzhk.mobilesafe.db.dao.Addressdao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class CallOutgoingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		String number = getResultData();
		
		String address = Addressdao.searchAddress(number);
		Toast.makeText(context, address, Toast.LENGTH_LONG).show();
	}

}
