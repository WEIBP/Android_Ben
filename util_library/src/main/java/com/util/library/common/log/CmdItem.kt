package com.util.library.common.log

import android.graphics.Color
import androidx.annotation.ColorInt
import com.blankj.utilcode.util.TimeUtils

data class CmdItem(val content:String,@ColorInt val textColor:  Int = Color.BLACK){
     val time = TimeUtils.getNowString(TimeUtils.getSafeDateFormat("HH:mm:ss.SSS")).toString()
}
