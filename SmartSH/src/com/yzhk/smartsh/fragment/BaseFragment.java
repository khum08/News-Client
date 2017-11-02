package com.yzhk.smartsh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public abstract class BaseFragment extends Fragment {

	public FragmentActivity mActivity;

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
	};

	public android.view.View onCreateView(android.view.LayoutInflater inflater,
			android.view.ViewGroup container,
			android.os.Bundle savedInstanceState) {
		View view = initView();
		return view;
	};
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}
	
	public abstract View initView();
	public abstract void initData();
}






















