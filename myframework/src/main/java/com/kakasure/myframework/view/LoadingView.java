package com.kakasure.myframework.view;

/**
 * 加载中View接口
 * 
 * @author pan
 * 
 */
public interface LoadingView {

	/**
	 * 显示加载中View
	 */
	void showLoadingView();

	/**
	 * 隐藏加载中View
	 */
	void hideLoadingView();

	/**
	 * 错误加载中View
	 */
	void errorLoadingView();
}
