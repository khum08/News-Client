package com.yzhk.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yzhk.mobilesafe.R;

public class Guide1Activity extends BaseGuideActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide1);
	}
	

	@Override
	public void showNextPage() {
		startActivity(new Intent(this,Guide2Activity.class));
		finish();
		overridePendingTransition(R.anim.enteranim_next,R.anim.exitanim_next);
	}

	@Override
	public void showPreviousPage() {
	}
}
