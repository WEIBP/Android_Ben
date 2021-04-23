package com.util.library.common.apachemath

import com.blankj.utilcode.util.LogUtils
import com.util.library.common.apachemath.fitting.DataCheckUtils
import com.util.library.common.apachemath.fitting.FittingConst
import com.util.library.common.apachemath.fitting.SimpleFitting
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints
import org.apache.commons.math3.linear.*
import org.apache.commons.math3.stat.StatUtils
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation
import org.apache.commons.math3.stat.descriptive.rank.Median
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.roundToInt


val IgnoreZeroTypeList = arrayListOf(FittingConst.Fitting_Reciprocal, FittingConst.Fitting_HyperbolaCurve)

class MathUtils {

    companion object {
        val instance: MathUtils by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MathUtils()
        }
    }

    /* 统计测试 */
    fun testTongji() {

        val values = doubleArrayOf(
                0.33, 1.33, 0.27333, 0.3, 0.501,
                0.444, 0.44, 0.34496, 0.33, 0.3, 0.292, 0.667
        )
        val values2 = doubleArrayOf(
                0.89, 1.51, 0.37999, 0.4, 0.701,
                0.484, 0.54, 0.56496, 0.43, 0.3, 0.392, 0.567
        )

        //计数

        //计数
        println("计算样本个数为：" + values.size)
        //mean--算数平均数
        println("平均数：" + StatUtils.mean(values))
        //sum--和
        println("所有数据相加结果为：" + StatUtils.sum(values))
        //max--最小值
        println("最小值：" + StatUtils.min(values))
        //max--最大值
        println("最大值：" + StatUtils.max(values))
        //范围
        println("范围是：" + (StatUtils.max(values) - StatUtils.min(values)))
        //标准差
        val standardDeviation = StandardDeviation()
        println("一组数据的标准差为：" + standardDeviation.evaluate(values))
        //variance--方差
        println("一组数据的方差为：" + StatUtils.variance(values))
        //median--中位数
        val median = Median()
        println("中位数：" + median.evaluate(values))
        //mode--众数
        val res = StatUtils.mode(values)
        println("众数：" + res[0] + "," + res[1])
        for (i in res.indices) {
            println("第" + (i + 1) + "个众数为：" + res[i])
        }
        //geometricMean--几何平均数
        println("几何平均数为：" + StatUtils.geometricMean(values))
        //meanDifference-- 平均差，平均概率偏差
        println("平均差为：" + StatUtils.meanDifference(values, values2))
        //normalize--标准化
        val norm = StatUtils.normalize(values2)
        for (i in res.indices) {
            println("第" + (i + 1) + "个数据标准化结果为：" + norm[i])
        }
        //percentile--百分位数
        println("从小到大排序后位于80%位置的数：" + StatUtils.percentile(values, 70.0))
        //populationVariance--总体方差
        println("总体方差为：" + StatUtils.populationVariance(values))
        //product--乘积
        println("所有数据相乘结果为：" + StatUtils.product(values))
        //sumDifference--和差
        println("两样本数据的和差为：" + StatUtils.sumDifference(values, values2))
        //sumLog--对数求和
        println("一组数据的对数求和为：" + StatUtils.sumLog(values))
        //sumSq--计算一组数值的平方和
        println("一组数据的平方和：" + StatUtils.sumSq(values))
        //varianceDifference --方差差异性。
        println(
                "一组数据的方差差异性为：" + StatUtils.varianceDifference(
                        values,
                        values2,
                        StatUtils.meanDifference(values, values2)
                )
        )

    }


    /**
     * 一元线性拟合 y = a + b*x
     *
     * @param x
     * @param y
     *            reuslt[0] = a result[1] = b result[2] 相关系数 result[3] 决定系数
     *            result[4] 点数量(长度) result[5] 自由度
     */
    fun lineFitting(x: DoubleArray, y: DoubleArray): DoubleArray {
        val size = x.size
        var xmean = 0.0
        var ymean = 0.0
        var xNum: Double
        var yNum: Double
        var xyNum = 0.0
        var xNum2 = 0.0
        var yNum2 = 0.0
        var rss = 0.0
        var tss = 0.0

        val result = DoubleArray(6)

        if (x.size != y.size) return result

        for (i in 0 until size) {
            xmean += x[i]
            ymean += y[i]
            xNum2 += x[i] * x[i]
            yNum2 += y[i] * y[i]
            xyNum += x[i] * y[i]
        }
        xNum = xmean
        yNum = ymean
        xmean /= size.toDouble()
        ymean /= size.toDouble()
        var sumx2 = 0.0
        var sumxy = 0.0
        for (i in 0 until size) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean)
            sumxy += (y[i] - ymean) * (x[i] - xmean)
        }
        val b = sumxy / sumx2
        val a = ymean - b * xmean
        result[0] = a
        result[1] = b
        println("a = $a, b=$b")
        val correlation = ((xyNum - xNum * yNum / size)
                / Math.sqrt((xNum2 - xNum * xNum / size) * (yNum2 - yNum * yNum / size)))
        println("相关系数：$correlation")
        result[2] = correlation
        for (i in 0 until size) {
            rss += (y[i] - (a + b * x[i])) * (y[i] - (a + b * x[i]))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        val r2 = 1 - rss / (size - 1 - 1) / (tss / (size - 1))
        result[3] = r2
        println("决定系数$r2")
        result[4] = x.size.toDouble()
        result[5] = (x.size - 1 - 1).toDouble()
        return result
    }


    fun lineFittingForList(x: DoubleArray, y: DoubleArray): MutableList<DoubleArray> {
        val result = mutableListOf<DoubleArray>()
        if (x.size != y.size || x.size < 2 || x.size < 2) return result
        val obs = WeightedObservedPoints()
        x.forEachIndexed { index, d ->
            obs.add(d, y[index])
        }
        val fitter = PolynomialCurveFitter.create(1)
        val fitterResult = fitter.fit(obs.toList())
        val min = x.min()!! - 5
        val max = x.max()!! + 5

        val ff = PolynomialFunction(fitterResult)
        result.add(doubleArrayOf(min, ff.value(min)))
        result.add(doubleArrayOf(max, ff.value(max)))

        return result
    }


    /**
     * 多元线性拟合 y = a + b*x1 + c*x2
     *
     * @param x
     * @param y
     * result[0] = a result[b] = b . . . result[len - 4] 点数 result[len -
     * 3] 自由度 result[len - 2] 残差平方和 result[len - 1] 确定系数
     */
    fun lineFitting2(
            x: Array<DoubleArray>,
            y: DoubleArray
    ): DoubleArray? {
        val a = DoubleArray(x.size + 1)
        val v = DoubleArray(2) // 这里的2为m
        val dt = DoubleArray(4)
        line2sqt(x, y, 2, 11, a, dt, v)
        var i: Int
        println("残差平方和：" + dt[0])
        val temp = a[a.size - 1]
        // 更换输出位置，把常数放到第一位
        i = a.size - 1
        while (i > 0) {
            a[i] = a[i - 1]
            i--
        }
        a[0] = temp
        val result = DoubleArray(x.size + 5)
        i = 0
        while (i <= x.size) {
            result[i] = a[i]
            i++
        }
        result[x.size + 1] = y.size.toDouble()
        result[x.size + 2] = (y.size - x.size).toDouble()
        result[x.size + 3] = dt[0]
        result[x.size + 4] = getLine2R(x, y, a, x.size)
        println("校正确定系数：" + result[x.size + 4])
        return result
    }

    /**
     * 多项式拟合 y = a + b*x1 + c*x1^2…… result[0] = a result[1] = b . . . result[n + 1]
     * 点数 result[n + 2] 自由度 result[n + 3] 确定系数
     *
     * @param n
     * 几级
     * @return
     */
    fun dxsFitting(x: DoubleArray, y: DoubleArray, n: Int): DoubleArray? {
        val result = DoubleArray(n + 4)
        val obs = WeightedObservedPoints()
        for (i in x.indices) {
            obs.add(x[i], y[i])
        }

        // Instantiate a third-degree polynomial fitterm.
        val fitter: PolynomialCurveFitter = PolynomialCurveFitter.create(n)

        // Retrieve fitted parameters (coefficients of the polynomial function).
        val coeff: DoubleArray = fitter.fit(obs.toList())
        for (i in coeff.indices) {
            result[i] = coeff[i]
        }
        val s = getDxsR(x, y, coeff, 5)
        println("确定系数:$s")
        result[n + 1] = x.size.toDouble()
        result[n + 2] = (x.size - n - 1).toDouble()
        result[n + 3] = s
        return result
    }

    /**
     * 指数拟合 y = b*exp(ax) result[0] = a result[1] = b result[2] 数据点数 result[3] 自由度
     * result[4] 确定系数
     *
     * @param x
     * @param y
     * @return
     */
    fun expFitting(x: DoubleArray, y: DoubleArray): DoubleArray? {
        val size = x.size
        var xmean = 0.0
        var ymean = 0.0
        var rss = 0.0
        var tss = 0.0
        val result = DoubleArray(5)
        for (i in 0 until size) {
            xmean += x[i]
            y[i] = Math.log(y[i])
            ymean += y[i]
        }
        xmean /= size.toDouble()
        ymean /= size.toDouble()
        var sumx2 = 0.0
        var sumxy = 0.0
        for (i in 0 until size) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean)
            sumxy += (y[i] - ymean) * (x[i] - xmean)
        }
        val b = sumxy / sumx2
        var a = ymean - b * xmean
        for (i in 0 until size) {
            rss += (y[i] - (a + b * x[i])) * (y[i] - (a + b * x[i]))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        val r2 = 1 - rss / (size - 1 - 1) / (tss / (size - 1))
        println("决定系数$r2")
        a = Math.exp(a)
        println("a = $a;b= $b")
        result[0] = a
        result[1] = b
        result[2] = x.size.toDouble()
        result[3] = (x.size - 2).toDouble()
        result[4] = r2
        return result
    }

    fun expFittingForList(x: DoubleArray, y: DoubleArray): MutableList<DoubleArray> {
        val size = x.size
        var xmean = 0.0
        var ymean = 0.0
        var rss = 0.0
        var tss = 0.0
        val result = mutableListOf<DoubleArray>()

        if (!DataCheckUtils.checkDate(x, y)) return result

        for (i in 0 until size) {
            xmean += x[i]
            y[i] = ln(y[i])
            ymean += y[i]
        }
        xmean /= size.toDouble()
        ymean /= size.toDouble()
        var sumx2 = 0.0
        var sumxy = 0.0
        for (i in 0 until size) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean)
            sumxy += (y[i] - ymean) * (x[i] - xmean)
        }
        val b = sumxy / sumx2
        var a = ymean - b * xmean
        for (i in 0 until size) {
            rss += (y[i] - (a + b * x[i])) * (y[i] - (a + b * x[i]))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }

        a = exp(a)

        val min = x.min()!!
        val max = x.max()!!

        for (x in 1..10) {
            result.add(doubleArrayOf(x.toDouble(), b * exp(a / x)))
        }


//        CurveFittingManger().exponentLineControl(x,y)


        return result
    }

    var rangeMore = 5


    /**
     * 通用拟合
     * 抛物线公式 y= o.5 * x^2
     *
     * @param x
     * @param y
     * @param type 拟合类型
     */
    fun simpleFittingForList(x: DoubleArray, y: DoubleArray, type: FittingConst): MutableList<DoubleArray> {
        val result = mutableListOf<DoubleArray>()
        if (!DataCheckUtils.checkDate(x, y)) {
            LogUtils.e("数据不合法",x,y)
            return result
        }

        val simpleFitting = SimpleFitting(x, y, type)

        val range = x.maxOrNull()!! - x.minOrNull()!!
        val min = x.minOrNull()!! - range*0.2
        val max = x.maxOrNull()!! + range*0.2


        when (type) {
            FittingConst.Fitting_Polynomial_1 -> {
                //线型添加两个点
                result.add(doubleArrayOf(min, simpleFitting.value(min)))
                result.add(doubleArrayOf(max, simpleFitting.value(max)))
            }

            else -> {
                //其他曲线 添加多个点
                val size = (max - min).roundToInt() * 10
                for (i in 1..size) {
                    val xValue = min + 0.1 * i
                    simpleFitting.value(xValue).apply {
                        if (!this.isNaN()) {
                            result.add(doubleArrayOf(xValue, this))
                        }
                    }

                }
            }
        }

        return result
    }


    /**
     * 对数拟合 y = ln(a * x + b) result[0] = a result[1] = b result[2] 数据点数 result[3]
     * 自由度 result[4] 确定系数
     *
     * @param x
     * @param y
     */
    fun logFitting(x: DoubleArray, y: DoubleArray): DoubleArray? {
        val size = x.size
        var xmean = 0.0
        var ymean = 0.0
        var rss = 0.0
        var tss = 0.0
        val result = DoubleArray(5)
        for (i in 0 until size) {
            xmean += x[i]
            y[i] = exp(y[i])
            ymean += y[i]
        }
        xmean /= size.toDouble()
        ymean /= size.toDouble()
        var sumx2 = 0.0
        var sumxy = 0.0
        for (i in 0 until size) {
            sumx2 += (x[i] - xmean) * (x[i] - xmean)
            sumxy += (y[i] - ymean) * (x[i] - xmean)
        }
        val b = sumxy / sumx2
        val a = ymean - b * xmean
        for (i in 0 until size) {
            rss += (y[i] - (b + a * x[i])) * (y[i] - (b + a * x[i]))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        val r2 = 1 - rss / (size - 1 - 1) / (tss / (size - 1))
        println("决定系数$r2")
        println("a = $a;b= $b")
        result[0] = a
        result[1] = b
        result[2] = x.size.toDouble()
        result[3] = (x.size - 2).toDouble()
        result[4] = r2
        return result
    }

    /**
     * 参考网址：http://wenku.baidu.com/link?url=T-xy9CUR_hVlCpA5o6xjqWb-Z1o5jraGEg-OHEtDgFLEynf6HedV68wMwC5u7RTs7PN5kiYq3qjyiddMveDFywLiuXai11MisNrNvv4KjAW&qq-pf-to=pcqq.c2c
     * 峰拟合 y = y(max) * exp[-(x - x(max))^2/S]
     *
     * @param x
     * @param y
     * result[0] = x(max) result[1] = y(max) result[2] = S result[3] 点数
     * result[4] 自由度 result[5] 确定系数
     */
    fun peakFitting(x: DoubleArray, y: DoubleArray): DoubleArray? {
        val size = x.size
        var maxX = 0.0
        var maxY = 0.0
        var minY = Int.MAX_VALUE.toDouble()
        val result = DoubleArray(6)
        val left =
                Array(x.size) { DoubleArray(3) }
        val right =
                Array(x.size) { DoubleArray(1) }
        for (i in 0 until size) {
            if (y[i] > maxY) {
                maxX = x[i]
                maxY = y[i]
            }
            if (y[i] < minY) {
                minY = y[i]
            }
            for (j in 0..2) {
                left[i][j] = getPeakValue(j, x[i])
            }
            right[i][0] = Math.log(y[i])
        }
        val leftMatrix = MatrixUtils.createRealMatrix(left)
        val rightMatrix = MatrixUtils.createRealMatrix(right)
        val leftMatrix1 = leftMatrix.transpose()
        val m = leftMatrix1.multiply(leftMatrix)
        val tMatrix: RealMatrix = LUDecomposition(m).getSolver().getInverse().multiply(leftMatrix1)
                .multiply(rightMatrix)
        result[0] = maxX
        result[1] = maxY
        result[2] = -1.0 / tMatrix.getEntry(2, 0)
        println(result[0].toString() + " " + result[1] + " " + result[2])
        val aaaa = getPearR(x, y, result, 2)
        println("确定系数:$aaaa")
        result[3] = x.size.toDouble()
        result[4] = (x.size - 1 - 2).toDouble()
        result[5] = aaaa
        return result
    }

    /**
     * 用户自定义拟合
     *
     * @param x
     * @param y
     * @param sf
     * 算法 <input type="checkbox" value="0"></input>常数
     * <input type="checkbox" value="1"></input>x
     * <input type="checkbox" value="2"></input>x^2
     * <input type="checkbox" value="3"></input>x^3
     * <input type="checkbox" value="4"></input>exp(x)
     * <input type="checkbox" value="5"></input>ln(x)
     * <input type="checkbox" value="6"></input>sin(x)
     * <input type="checkbox" value="7"></input>cos(x)
     *
     * result[0] = 第一项系数 result[1] = 第二项系数 . . . result[n] = 第N项系数
     * result[sf.length] 点数 result[sf.length+1] 自由度 result[sf.length+2]
     * 确定系数
     * @return
     */
    fun userDefineFitting(
            x: DoubleArray,
            y: DoubleArray,
            sf: IntArray
    ): DoubleArray? {
        val left =
                Array(sf.size) { DoubleArray(sf.size) }
        val right = DoubleArray(sf.size)
        val result = DoubleArray(sf.size + 3)
        result[sf.size] = x.size.toDouble()
        var containZero = false
        for (i in sf.indices) {
            var yValue = 0.0

            // 数值中包括0
            if (sf[i] == 0) {
                containZero = true
            }
            for (j in sf.indices) {
                var xValue = 0.0

                // 计算X的的司格马
                for (k in x.indices) {
                    xValue += getUserDefineValue(
                            sf[i],
                            x[k]
                    ) * getUserDefineValue(sf[j], x[k])
                }
                left[i][j] = xValue
            }

            // 计算Y的的司格马
            for (k in x.indices) {
                yValue += y[k] * getUserDefineValue(sf[i], x[k])
            }
            right[i] = yValue
        }

        // 计算自由度
        result[sf.size + 1] = (x.size - sf.size - 1).toDouble()

        // 计算自由度
        if (containZero) {
            result[sf.size + 1] = (x.size - sf.size).toDouble()
        }

        // 矩阵求解
        val leftMatrix = MatrixUtils.createRealMatrix(left)
        val solver: DecompositionSolver = LUDecomposition(leftMatrix).getSolver()
        val constants: RealVector = ArrayRealVector(right, false)
        val solution = solver.solve(constants)
        println(solution)

        // 获得系数值
        for (i in 0 until solution.dimension) {
            result[i] = solution.getEntry(i)
        }
        var rss = 0.0
        var tss = 0.0
        var ymean = 0.0
        for (i in x.indices) {
            ymean += y[i]
        }
        ymean /= x.size.toDouble()
        for (i in x.indices) {
            rss += ((y[i] - getUserDefineValueByX(sf, x[i], solution))
                    * (y[i] - getUserDefineValueByX(sf, x[i], solution)))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        val r2 = 1 - rss / result[sf.size + 1] / (tss / (x.size - 1))
        println("决定系数$r2")
        result[sf.size + 2] = r2
        return result
    }

    /**
     * 用户自定义函数，传入X值获得Y值
     *
     * @param n
     * @param x
     * @param solution
     * @return
     */
    private fun getUserDefineValueByX(
            n: IntArray,
            x: Double,
            solution: RealVector
    ): Double {
        var value = 0.0
        for (i in n.indices) {
            value += getUserDefineValue(n[i], x) * solution.getEntry(i)
        }
        return value
    }

    /**
     * 获得用户自定义的值， <input type="checkbox" value="0"></input>常数
     * @param i
     * @param x
     * @return
     */
    private fun getUserDefineValue(i: Int, x: Double): Double {
        // 常数
        if (i == 0) {
            return 1.00
        } else if (i == 1) {
            // x
            return x
        } else if (i == 2) {
            // x^2
            return x * x
        } else if (i == 3) {
            // x^3
            return x * x * x
        } else if (i == 4) {
            // exp(x)
            return Math.pow(Math.E, x)
        } else if (i == 5) {
            // ln(x)
            return Math.log(x)
        } else if (i == 6) {
            // sin(x)
            return Math.sin(x)
        } else if (i == 7) {
            // cos(x)
            return Math.cos(x)
        }
        return 0.0
    }

    /**
     * 获得决定系数
     *
     * @param coeff
     * @return
     */
    private fun getPearR(
            x: DoubleArray,
            y: DoubleArray,
            coeff: DoubleArray,
            n: Int
    ): Double {
        val size = x.size
        var ymean = 0.0
        var rss = 0.0
        var tss = 0.0
        for (i in 0 until size) {
            ymean += y[i]
        }
        ymean /= size.toDouble()
        for (i in 0 until size) {
            rss += (y[i] - getPeakValueByX(
                    x[i],
                    coeff
            )) * (y[i] - getPeakValueByX(x[i], coeff))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        return 1 - rss / (size - n - 1) / (tss / (size - 1))
    }

    /**
     * 通过X获得Y的值 y = y(max) * exp[-(x - x(max))^2/S]
     *
     * @param x
     * @param y
     * result[0] = x(max) result[1] = y(max) result[2] = S
     * @return
     */
    private fun getPeakValueByX(x: Double, result: DoubleArray): Double {
        return Math.exp(
                -Math.pow(
                        x - result[0],
                        2.0
                ) / result[2]
        ) * result[1]
    }

    private fun getPeakValue(i: Int, x: Double): Double {
        // 常数
        if (i == 0) {
            return 1.0
        } else if (i == 1) {
            return x
        } else if (i == 2) {
            // x^2
            return x * x
        }
        return 0.0
    }

    /**
     * 获得决定系数
     *
     * @param coeff
     * @return
     */
    private fun getDxsR(
            x: DoubleArray,
            y: DoubleArray,
            coeff: DoubleArray,
            n: Int
    ): Double {
        val size = x.size
        var ymean = 0.0
        var rss = 0.0
        var tss = 0.0
        for (i in 0 until size) {
            ymean += y[i]
        }
        ymean /= size.toDouble()
        for (i in 0 until size) {
            rss += (y[i] - getDxsValueByX(
                    x[i],
                    coeff
            )) * (y[i] - getDxsValueByX(x[i], coeff))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        return 1 - rss / (size - n - 1) / (tss / (size - 1))
    }

    /**
     * 通过X获得Y的值
     *
     * @param x
     * @param coeff
     * @return
     */
    private fun getDxsValueByX(x: Double, coeff: DoubleArray): Double {
        val size = coeff.size
        var result = coeff[0]
        for (i in 1 until size) {
            result += coeff[i] * Math.pow(x, i.toDouble())
        }
        return result
    }

    /**
     * 多元线性回归分析
     *
     * @param x[m][n]
     * 每一列存放m个自变量的观察值
     * @param y[n]
     * 存放随即变量y的n个观察值
     * @param m
     * 自变量的个数
     * @param n
     * 观察数据的组数
     * @param a
     * 返回回归系数a0,...,am
     * @param dt[4]
     * dt[0]偏差平方和q,dt[1] 平均标准偏差s dt[2]返回复相关系数r dt[3]返回回归平方和u
     * @param v[m]
     * 返回m个自变量的偏相关系数
     */
    private fun line2sqt(
            x: Array<DoubleArray>,
            y: DoubleArray,
            m: Int,
            n: Int,
            a: DoubleArray,
            dt: DoubleArray,
            v: DoubleArray
    ) {
        var i: Int
        var j: Int
        var k: Int
        val mm: Int
        var q: Double
        var e: Double
        var u: Double
        var p: Double
        var yy: Double
        val s: Double
        val r: Double
        var pp: Double
        val b = DoubleArray((m + 1) * (m + 1))
        mm = m + 1
        b[mm * mm - 1] = n.toDouble()
        j = 0
        while (j <= m - 1) {
            p = 0.0
            i = 0
            while (i <= n - 1) {
                p = p + x[j][i]
                i++
            }
            b[m * mm + j] = p
            b[j * mm + m] = p
            j++
        }
        i = 0
        while (i <= m - 1) {
            j = i
            while (j <= m - 1) {
                p = 0.0
                k = 0
                while (k <= n - 1) {
                    p = p + x[i][k] * x[j][k]
                    k++
                }
                b[j * mm + i] = p
                b[i * mm + j] = p
                j++
            }
            i++
        }
        a[m] = 0.0
        i = 0
        while (i <= n - 1) {
            a[m] = a[m] + y[i]
            i++
        }
        i = 0
        while (i <= m - 1) {
            a[i] = 0.0
            j = 0
            while (j <= n - 1) {
                a[i] = a[i] + x[i][j] * y[j]
                j++
            }
            i++
        }
        chlk(b, mm, 1, a)
        yy = 0.0
        i = 0
        while (i <= n - 1) {
            yy = yy + y[i] / n
            i++
        }
        q = 0.0
        e = 0.0
        u = 0.0
        i = 0
        while (i <= n - 1) {
            p = a[m]
            j = 0
            while (j <= m - 1) {
                p = p + a[j] * x[j][i]
                j++
            }
            q = q + (y[i] - p) * (y[i] - p)
            e = e + (y[i] - yy) * (y[i] - yy)
            u = u + (yy - p) * (yy - p)
            i++
        }
        s = Math.sqrt(q / n)
        r = Math.sqrt(1.0 - q / e)
        j = 0
        while (j <= m - 1) {
            p = 0.0
            i = 0
            while (i <= n - 1) {
                pp = a[m]
                k = 0
                while (k <= m - 1) {
                    if (k != j) pp = pp + a[k] * x[k][i]
                    k++
                }
                p = p + (y[i] - pp) * (y[i] - pp)
                i++
            }
            v[j] = Math.sqrt(1.0 - q / p)
            j++
        }
        dt[0] = q
        dt[1] = s
        dt[2] = r
        dt[3] = u
    }

    private fun chlk(a: DoubleArray, n: Int, m: Int, d: DoubleArray): Int {
        var i: Int
        var j: Int
        var k: Int
        var u: Int
        var v: Int
        if (a[0] + 1.0 == 1.0 || a[0] < 0.0) {
            println("fail\n")
            return -2
        }
        a[0] = Math.sqrt(a[0])
        j = 1
        while (j <= n - 1) {
            a[j] = a[j] / a[0]
            j++
        }
        i = 1
        while (i <= n - 1) {
            u = i * n + i
            j = 1
            while (j <= i) {
                v = (j - 1) * n + i
                a[u] = a[u] - a[v] * a[v]
                j++
            }
            if (a[u] + 1.0 == 1.0 || a[u] < 0.0) {
                println("fail\n")
                return -2
            }
            a[u] = Math.sqrt(a[u])
            if (i != n - 1) {
                j = i + 1
                while (j <= n - 1) {
                    v = i * n + j
                    k = 1
                    while (k <= i) {
                        a[v] = a[v] - a[(k - 1) * n + i] * a[(k - 1) * n + j]
                        k++
                    }
                    a[v] = a[v] / a[u]
                    j++
                }
            }
            i++
        }
        j = 0
        while (j <= m - 1) {
            d[j] = d[j] / a[0]
            i = 1
            while (i <= n - 1) {
                u = i * n + i
                v = i * m + j
                k = 1
                while (k <= i) {
                    d[v] = d[v] - a[(k - 1) * n + i] * d[(k - 1) * m + j]
                    k++
                }
                d[v] = d[v] / a[u]
                i++
            }
            j++
        }
        j = 0
        while (j <= m - 1) {
            u = (n - 1) * m + j
            d[u] = d[u] / a[n * n - 1]
            k = n - 1
            while (k >= 1) {
                u = (k - 1) * m + j
                i = k
                while (i <= n - 1) {
                    v = (k - 1) * n + i
                    d[u] = d[u] - a[v] * d[i * m + j]
                    i++
                }
                v = (k - 1) * n + k - 1
                d[u] = d[u] / a[v]
                k--
            }
            j++
        }
        return 2
    }

    /**
     * 获得相关系数
     *
     * @param coeff
     * @return
     */
    private fun getLine2R(
            x: Array<DoubleArray>,
            y: DoubleArray,
            a: DoubleArray,
            n: Int
    ): Double {
        val size: Int = x[0].size
        var ymean = 0.0
        var rss = 0.0
        var tss = 0.0
        for (i in 0 until size) {
            ymean += y[i]
        }
        ymean /= size.toDouble()
        for (i in 0 until size) {
            rss += (y[i] - getLine2ValueByX(x, a, i)) * (y[i] - getLine2ValueByX(x, a, i))
            tss += (y[i] - ymean) * (y[i] - ymean)
        }
        return 1 - rss / (size - n - 1) / (tss / (size - 1))
    }

    /**
     * 通过X获得Y的值
     *
     * @param x
     * @return
     */
    private fun getLine2ValueByX(
            x: Array<DoubleArray>,
            a: DoubleArray,
            n: Int
    ): Double {
        val size = a.size
        var result = a[0]
        for (i in 1 until size) {
            result += x[i - 1][n] * a[i]
        }
        return result
    }
}