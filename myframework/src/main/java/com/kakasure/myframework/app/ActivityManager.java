package com.kakasure.myframework.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理活动界面
 * Created by danke on 2015/12/30.
 */
public class ActivityManager {

    private static List<Activity> activities = new ArrayList<>();

    /**
     * 活动界面创建时，管理器记录启动的Activity
     * @param activity
     */
    public static void onActivityCreate(Activity activity) {
        activities.add(activity);
    }

    /**
     * 活动界面销毁时，管理器移除记录的Activity
     * @param activity
     */
    public static void onActivityDestroy(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 根据指定的Class，获取Activity
     * @param clazz
     * @return
     */
    public static Activity getActivity(Class clazz) {
        Activity activity = null;
        for (Activity a: activities) {
            if (a != null && a.getClass() == clazz) {
                activity = a;
                break;
            }
        }
        return activity;
    }

    /**
     * 根据指定的Class，finish Activity
     * @param clazz
     */
    public static void finish(Class clazz) {
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            if (activity != null && activity.getClass() == clazz) {
                activity.finish();
                activities.remove(i);
                break;
            }
        }
    }

    /**
     * 结束所有活动界面
     */
    public static void finishAll() {
        finishAllWithExcept(null);
    }

    /**
     * 结束除except以外的所有界面
     *
     * @param except 不关闭的
     */
    public static void finishAllWithExcept(Class except) {
        int index = 0;
        while (++index < activities.size()) {
            Activity a = activities.get(index);
            if (a != null && a.getClass() != except) {
                a.finish();
                activities.remove(index--);
            }
        }
    }

    /**
     * 结束除excepts列表以外的所有界面
     *
     * @param excepts 不关闭的
     */
    public static void finishAllWitchExcepts(List<Class> excepts) {
        if (excepts == null) finishAll();
        int index = 0;
        while (++index < activities.size()) {
            Activity a = activities.get(index);
            if (a != null && !excepts.contains(a.getClass())) {
                a.finish();
                activities.remove(index--);
            }
        }
    }
}
