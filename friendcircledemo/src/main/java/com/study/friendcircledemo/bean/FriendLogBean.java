package com.study.friendcircledemo.bean;

import java.util.ArrayList;
import java.util.List;

public class FriendLogBean {
	private long articleId;
	private String articleTime;
	private String articleTitile;
	private String articleContent;

	private List<ReplyBean> replyList = new ArrayList<ReplyBean>();

	public List<ReplyBean> getReplyList() {
		return replyList;
	}

	public void setReplyList(List<ReplyBean> replyList) {
		this.replyList = replyList;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}

	public String getArticleTime() {
		return articleTime;
	}

	public void setArticleTime(String articleTime) {
		this.articleTime = articleTime;
	}

	public String getArticleTitile() {
		return articleTitile;
	}

	public void setArticleTitile(String articleTitile) {
		this.articleTitile = articleTitile;
	}

	public String getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}

}
