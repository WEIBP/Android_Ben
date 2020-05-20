package com.ben.core.personal.stocks

import android.widget.TextView
import com.ben.R
import com.ben.base.BaseActivity

/**
 * author: Ben
 * created on: 2020/3/9 17:26
 * description:
 */
class StocksKotlinActivity : BaseActivity() {

    var resultTV :  TextView ?= null
    private val myHandler = android.os.Handler()


    override fun getLayoutId(): Int {
        return R.layout.activity_stocks
    }

    override fun initView() {
        resultTV = findViewById(R.id.resultTV)
    }
}