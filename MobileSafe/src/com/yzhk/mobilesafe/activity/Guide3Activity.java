package com.yzhk.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yzhk.mobilesafe.R;

public class Guide3Activity extends BaseGuideActivity {

	private SharedPreferences sp;
	private EditText dt_phone_safe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide3);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		dt_phone_safe = (EditText) findViewById(R.id.et_phone_safe);
		
		String phone_safe = sp.getString("phone_safe", "");
		dt_phone_safe.setText(phone_safe);

	}

	@Override
	public void showNextPage() {

		String phoneSafe = dt_phone_safe.getText().toString().trim();
		if (TextUtils.isEmpty(phoneSafe)) {
			Toast.makeText(this, "安全号码不能为空哦", Toast.LENGTH_SHORT).show();
			return;
		} else {
			sp.edit().putString("phone_safe", phoneSafe).commit();

			startActivity(new Intent(this, Guide4Activity.class));
			finish();
			overridePendingTransition(R.anim.enteranim_next,
					R.anim.exitanim_next);
		}
	}

	@Override
	public void showPreviousPage() {
		if (dt_phone_safe.getText() != null) {
			startActivity(new Intent(this, Guide2Activity.class));
			finish();
			overridePendingTransition(R.anim.enteranim_previous,
					R.anim.exitanim_previous);
		} else {
			Toast.makeText(this, "安全号码不能为空哦", Toast.LENGTH_SHORT).show();
		}

	}

	public void selectContact(View v) {
		Intent intent = new Intent(this, SelectContactActivity.class);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK){
			String phone = data.getStringExtra("phone");
			phone = phone.replaceAll("-", "").replaceAll(" ", "");
			EditText et_phone_safe = (EditText) findViewById(R.id.et_phone_safe);
			et_phone_safe.setText(phone);
		}

	}

}
