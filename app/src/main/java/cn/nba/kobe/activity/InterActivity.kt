package cn.nba.kobe.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.nba.kobe.R
import cn.nba.kobe.adapter.Item1
import cn.nba.kobe.entity.DataEntity
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_inter.*

class InterActivity : AppCompatActivity() {
    val url = "https://www.google.com/streetview/feed/gallery/data.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inter)
        OkGo.get<String>(url).execute(object : StringCallback() {
            override fun onSuccess(response: Response<String>?) {
                val data = ArrayList<DataEntity>()
                val map: Map<String, DataEntity> =
                    JSON.parseObject(response?.body()?.toString(), object : TypeReference<Map<String, DataEntity>>() {})
                val m: Set<Map.Entry<String, DataEntity>> = map.entries
                val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
                do {
                    val en: Map.Entry<String, DataEntity> = it.next()
                    val json = JSON.toJSON(en.value)
                    val entity: DataEntity =
                        JSON.parseObject(json.toString(), DataEntity::class.java)
                    entity.key = en.key
                    if (TextUtils.isEmpty(entity.panoid)) {
                        continue
                    } else {
                        if (entity.panoid == "LiAWseC5n46JieDt9Dkevw") {
                            continue
                        }
                    }
                    if (entity.fife) {
                        entity.imageUrl =
                            "https://lh4.googleusercontent.com/" + entity.panoid + "/w400-h300-fo90-ya0-pi0/"
                        continue
                    } else {
                        entity.imageUrl =
                            "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity.panoid
                    }
                    data.add(entity)
                } while (it.hasNext())
                val itemAdapter = ItemAdapter<Item1>()
                val fastAdapter = FastAdapter.with(itemAdapter)
                val items = ArrayList<Item1>()
                data.forEach {
                    val i = Item1()
                    i.title = it.title
                    i.imgUrl = it.imageUrl
                    items.add(i)
                }
                itemAdapter.add(items)
                recycler.adapter = fastAdapter
                recycler.layoutManager = LinearLayoutManager(this@InterActivity)
                fastAdapter.onClickListener = { view, adapter, item, position ->
                    val i = Intent(this@InterActivity, DetailsActivity::class.java)
                    i.putExtra("data",data[position])
                    startActivity(i)
                    false
                }
            }
        })
    }
}