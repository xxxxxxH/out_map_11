package cn.nba.kobe.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import cn.nba.kobe.R
import cn.nba.kobe.adapter.Item2
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_near.*

class NearActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near)
        val data = ArrayList<String>()
        data.add("airport")
        data.add("atm")
        data.add("bakery")
        data.add("bank")
        data.add("bus")
        data.add("cafe")
        data.add("church")
        data.add("cloth")
        data.add("dentist")
        data.add("doctor")
        data.add("fire station")
        data.add("gas station")
        data.add("hospital")
        data.add("hotel")
        data.add("jewelry")
        data.add("mall")
        data.add("mosque")
        data.add("park")
        data.add("pharmacy")
        data.add("police")
        data.add("post office")
        data.add("salon")
        data.add("shoe")
        data.add("stadium")
        data.add("university")
        data.add("zoo")

        val itemAdapter = ItemAdapter<Item2>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        val items = ArrayList<Item2>()
        data.forEach {
            val i = Item2()
            i.title = it
            items.add(i)
        }
        itemAdapter.add(items)
        recycler.adapter = fastAdapter
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            3,
            StaggeredGridLayoutManager.VERTICAL
        )
        recycler.layoutManager = staggeredGridLayoutManager
        fastAdapter.onClickListener = { v, a, i, p ->

            false
        }
    }
}