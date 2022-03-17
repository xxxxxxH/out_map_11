package cn.nba.kobe.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Allen.get().iverson(this)
        activityMainLlLive.setOnClickListener {
            startActivity(Intent(this,MapActivity::class.java))
        }
        activityMainLlStreet.setOnClickListener {
            startActivity(Intent(this,StreetActivity::class.java))
        }
        activityMainLlRoute.setOnClickListener {
            startActivity(Intent(this,SearchActivity::class.java))
        }
        activityMainLlStreetExt.setOnClickListener {
            startActivity(Intent(this,InterActivity::class.java))
        }
        activityMainLlNear.setOnClickListener {
            startActivity(Intent(this,NearActivity::class.java))
        }
    }
}