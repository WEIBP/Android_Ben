package com.util.library.common

import android.graphics.Color

/**
 * author: Ben
 * created on: 2020/5/15 10:17
 * description:
 */
class ColorUtil {

    companion object {
        @JvmStatic val VORDIPLOM_COLORS = intArrayOf(
              rgb("#836FFF"), rgb("#F08080"), Color.rgb(140, 234, 255),  Color.YELLOW, rgb("#20B2AA"),Color.rgb(192, 255, 140),
                rgb("#EE9572"), rgb("#FFB6C1"), rgb("#8B658B"),    Color.rgb(255, 208, 140)
        )

        @JvmStatic public fun rgb(hex: String): Int {
            val color = hex.replace("#", "")
                    .toLong(16)
                    .toInt()
            val r = color shr 16 and 0xFF
            val g = color shr 8 and 0xFF
            val b = color shr 0 and 0xFF
            return Color.rgb(r, g, b)
        }


    }

}