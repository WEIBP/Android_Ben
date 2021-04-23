package com.util.library.common

import android.content.Context

class DpUtil {
    companion object {

        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
       @JvmStatic fun dp2px(context: Context, dpValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        @JvmStatic fun px2dp(context: Context, pxValue: Float): Int {
            val scale: Float = context.resources.displayMetrics.density
            return (pxValue / scale + 0.5f).toInt()
        }
    }
}