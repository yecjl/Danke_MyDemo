package com.study.mydemo.product;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.study.mydemo.R;

public class VerticalFragment3 extends Fragment {

	private CustemWebView webview;
	private boolean hasInited = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.vertical_fragment3, null);
		webview = (CustemWebView) rootView.findViewById(R.id.fragment3_webview);
		return rootView;
	}

	public void initView() {
		if (null != webview && !hasInited) {
			hasInited = true;
			webview.loadUrl("http://blog.csdn.net/sww_simpcity/article/details/25209195");
		}
	}
}
