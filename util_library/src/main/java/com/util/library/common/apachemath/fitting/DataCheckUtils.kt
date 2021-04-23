package com.util.library.common.apachemath.fitting

class DataCheckUtils {
    companion object {
        @JvmStatic fun checkDate(x:DoubleArray,y:DoubleArray): Boolean {
            return x.size==y.size && x.size>1 && y.size>1
        }


        @JvmStatic  fun checkX(xValue: Double, type: FittingConst): Boolean {
            return when (type) {
                FittingConst.Fitting_HyperbolaCurve -> xValue != 0.0
                FittingConst.Fitting_Log -> xValue>0.0
                else -> true
            }
        }

        @JvmStatic fun checkRange(xValue: Double, yValue: Double, type: FittingConst): Boolean {
            return checkX(xValue, type) && !yValue.isNaN()
        }
    }
}