package com.ben.library;

import android.content.pm.ApplicationInfo;

public class APPUtils {

    private static boolean isApkInDebug(){
        try {
            ApplicationInfo applicationInfo = BenUtils.getContext().getApplicationInfo();
            return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) !=0;
        } catch (Exception e) {
            return false;
        }

    }
}
