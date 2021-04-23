package com.util.library.common.apachemath.fitting


import com.util.library.common.apachemath.fitting.funtion.*
import org.apache.commons.math3.analysis.ParametricUnivariateFunction
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
import org.apache.commons.math3.analysis.function.HarmonicOscillator
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction
import org.apache.commons.math3.fitting.HarmonicCurveFitter
import org.apache.commons.math3.fitting.PolynomialCurveFitter
import org.apache.commons.math3.fitting.SimpleCurveFitter
import org.apache.commons.math3.fitting.WeightedObservedPoints

class SimpleFitting(x: DoubleArray, y: DoubleArray, var type: FittingConst) {

    var arrayParam = doubleArrayOf()
    private lateinit var simpleFun: ParametricUnivariateFunction
    private lateinit var fittingFunction: UnivariateDifferentiableFunction
    private val polynomialList = arrayOf(FittingConst.Fitting_Polynomial_1, FittingConst.Fitting_Polynomial_2,
            FittingConst.Fitting_Polynomial_3, FittingConst.Fitting_Parabola, FittingConst.Fitting_Harmonic)

    /**
     * 对数拟合的y值
     */
    fun value(x: Double): Double {
        var y = Double.NaN
        if (!DataCheckUtils.checkX(x, type)) return y
        y = try {
            if (type in polynomialList) {
                fittingFunction.value(x)
            } else {
                simpleFun.value(x, *arrayParam)
            }
        } catch (e: Exception) {
            Double.NaN
        }
        return y
    }


    init {
        if (DataCheckUtils.checkDate(x, y)) {
            var start = doubleArrayOf()
            val obs = WeightedObservedPoints()
            x.forEachIndexed { index, d ->
                if (DataCheckUtils.checkRange(d, y[index], type)) {
                    obs.add(d, y[index])
                }
            }
            when (type) {
                FittingConst.Fitting_Log -> {
                    simpleFun = NaturalLogarithm()
                    start = doubleArrayOf(1.0, 1.0)
                }

                FittingConst.Fitting_Parabola -> PolynomialCurveFitter.create(2).apply {
                    fittingFunction = PolynomialFunction(fit(obs.toList()))
                }

                FittingConst.Fitting_Reciprocal -> {
                    simpleFun = Reciprocal()
                    start = doubleArrayOf(1.0, 1.0)
                }

                FittingConst.Fitting_HyperbolaCurve -> {
                    simpleFun = Hyperbola()
                    start = doubleArrayOf(1.0, 1.0)
                }

                FittingConst.Fitting_Sin -> {
                    simpleFun = Sine()
                    start = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
                }


                FittingConst.Fitting_Cos -> {
                    simpleFun = Cosine()
                    start = doubleArrayOf(1.0, 1.0, 1.0, 1.0)
                }

                FittingConst.Fitting_Exponent -> {
                    simpleFun = Exponent()
                    start = doubleArrayOf(1.0, 1.0)
                }

                FittingConst.Fitting_Polynomial_1 -> PolynomialCurveFitter.create(1).apply {
                    fittingFunction = PolynomialFunction(fit(obs.toList()))
                }

                FittingConst.Fitting_Polynomial_2 -> PolynomialCurveFitter.create(2).apply {
                    fittingFunction = PolynomialFunction(fit(obs.toList()))
                }

                FittingConst.Fitting_Polynomial_3 -> PolynomialCurveFitter.create(3).apply {
                    fittingFunction = PolynomialFunction(fit(obs.toList()))
                }

                FittingConst.Fitting_Harmonic -> HarmonicCurveFitter.create().apply {
                    arrayParam = fit(obs.toList())
                    fittingFunction = HarmonicOscillator(arrayParam[0],arrayParam[1],arrayParam[2])
                }
            }

            if (type !in polynomialList) {
                arrayParam = SimpleCurveFitter.create(simpleFun, start).fit(obs.toList())
            }

        }

    }


}