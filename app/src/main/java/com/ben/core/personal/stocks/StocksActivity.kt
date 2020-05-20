package com.ben.core.personal.stocks

import android.annotation.SuppressLint
import android.os.Handler
import android.widget.TextView
import com.ben.R.id
import com.ben.R.layout
import com.ben.base.BaseActivity
import com.ben.common.net.RetrofitManager
import com.blankj.utilcode.util.LogUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_stocks.resultTV
import okhttp3.ResponseBody

/**
 * author: Ben
 * created on: 2020/3/9 14:38
 * description:
 */
class StocksActivity : BaseActivity() {
    private val myHandler = Handler()
    private val sotckList = mutableListOf<String>()
    override fun getLayoutId(): Int {
        return layout.activity_stocks
    }

    @SuppressLint("CheckResult") override fun initView() {

        setContentView(true, true, "Stocks")

        sotckList.add("000001")
        sotckList.add("000905")
        sotckList.add("300014")
        sotckList.add("600309")
        sotckList.add("300059")
        sotckList.add("002841")
        sotckList.add("603160")
//        sotckList.add("300782")
        sotckList.add("002214")
        sotckList.add("300702")
        sotckList.add("601100 ")



    }

    private val runable:Runnable = object : Runnable {
        @SuppressLint("CheckResult") override fun run() {

            var data = StringBuffer()
            sotckList.forEach {
                data.append(changeCode(it))
                data.append(",")
            }

            RetrofitManager.builder()
                    .getStocks(data.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ result: ResponseBody ->
                        val str = result.string()
                        resultTV.text = str
                        format(str)
                    }) { throwable: Throwable -> LogUtils.d(throwable.toString()) }
            myHandler.postDelayed(this, 5 * 1000.toLong())
        }
    }

    private fun changeCode(code: String): String {
        if (code.startsWith("60") || code.startsWith("000") ) {
            return "sh$code";
        }

        if (code.startsWith("00") || code.startsWith("300") ) {
            return "sz$code";
        }

        return code

    }

    override fun onStart() {
        super.onStart()
        myHandler.post(runable)
    }

    override fun onStop() {
        super.onStop()
        myHandler.post(runable)
    }

    @SuppressLint("DefaultLocale") private fun format(str: String) {
        val result = StringBuilder()

        //var hq_str_sz002307="北新路桥,5.780,5.990,5.720,5.910,5.700,5.710,5.720,23804478,137944096.340,205540,5.710,647400,5.700,82500,5.690,171700,5.680,37400,5.670,92300,5.720,131200,5.730,43005,5.740,22200,5.750,26200,5.760,2020-03-09,15:00:03,00";
        //var hq_str_sh000001="上证指数,2987.1805,3034.5113,2943.2907,2989.2051,2940.7138,0,0,414560736,438143854610,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2020-03-09,15:02:08,00,";
        val list = str.split(";")
                .toTypedArray()
        for (item in list) {
            if (item.length > 30) {
                val data = item.trim { it <= ' ' }.substring(21)
                val dataArray = data.split(",").toTypedArray()
                val priceNow = dataArray[3].toDouble()
                val priceYestaday = dataArray[2].toDouble()
                val change = 100 * (priceNow - priceYestaday) / priceYestaday
                val ifGrow = change > 0
                val name = dataArray[0]
                result.append(name)
                        .append(" , ")
                        .append(String.format("%.2f", priceNow))
                        .append(" , ")
                        .append(String.format("%.2f", change))
                        .append("%")
                        .append(" , ")
                        .append("\n")
            }
        }
        resultTV.text = result.toString()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}