package com.study.myqlive.presenters.viewinface;

import com.study.myqlive.model.MemberInfo;

import java.util.ArrayList;

/**
 * 成员列表回调
 */
public interface MembersDialogView extends MvpView {

    void showMembersList(ArrayList<MemberInfo> data);

}
