package cn.nba.kobe.acvitity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.james.Allen
import cn.nba.kobe.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Allen.get().iverson(this)
    }
}