package com.yzhk.smartsh.Utils;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yzhk.smartsh.activity.HomeActivity;
import com.yzhk.smartsh.domain.CategroiesData;
import com.yzhk.smartsh.fragment.LeftMenuFragment;
import com.yzhk.smartsh.global.GlobalConstant;

public class NetAccessUtil {
	
	public static void getDataFromServer(final Context context) {

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, GlobalConstant.CATEGORY_URL,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						arg0.printStackTrace();
						Toast.makeText(context, "服务器未打开，Sorry！",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String jsonResult = responseInfo.result;
						CacheUtil.putCache(context,
								GlobalConstant.CATEGORY_URL, jsonResult);
						parseDataAndSetSilding(context,jsonResult);
					}
				});
	}

	
	public static void parseDataAndSetSilding(Context context, String result) {
		Gson gson = new Gson();
		CategroiesData json = gson.fromJson(result, CategroiesData.class);
		HomeActivity mainUI = (HomeActivity) context;
		LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
		leftMenuFragment.setListViewData(json.data);
	}


}
