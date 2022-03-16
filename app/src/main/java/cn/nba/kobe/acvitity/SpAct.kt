package cn.nba.kobe.acvitity

import android.content.Intent
import android.os.Bundle
import cn.nba.james.ActivityB
import cn.nba.james.funcCreateLoading
import cn.nba.james.requestPermissions
import cn.nba.kobe.R

class SpAct : ActivityB(R.layout.layout_1) {

    private val loadingView by lazy {
        funcCreateLoading()
    }

    override fun default() {
        func1()
    }

    override fun func() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun func1() {
        loadingView.show()
    }

    override fun func2() {
        loadingView.hide()
    }
}