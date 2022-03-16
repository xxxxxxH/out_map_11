package cn.nba.james

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

abstract class ActivityB(id:Int):AppCompatActivity(id) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions {
            if (it){
                default()
                funcGetId()
                funcInitSDK()
                funcCountDown {
                    func2()
                    func()
                }
            }
        }
    }
    abstract fun default()
    abstract fun func()
    abstract fun func1()
    abstract fun func2()
}