package cn.nba.kobe.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import cn.nba.kobe.R
import cn.nba.kobe.adapter.Item1
import cn.nba.kobe.entity.DataEntity
import cn.nba.kobe.utils.addMarker
import cn.nba.kobe.utils.moveMap
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_details.recycler
import kotlinx.android.synthetic.main.activity_inter.*

class DetailsActivity : AppCompatActivity() {

    val url = "https://www.google.com/streetview/feed/gallery/collection/"

    private val bigPlace by lazy {
        intent.getSerializableExtra("data") as DataEntity
    }

    private val mapBox by lazy {
        mapview.getMapboxMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        if (bigPlace == null)finish()
        mapview.let {
            mapBox.addOnMapClickListener {
                mapBox.flyTo(cameraOptions {
                    center(it)
                    zoom(14.0)
                })
                true
            }
            mapBox.loadStyleUri(Style.OUTDOORS)
        }
        OkGo.get<String>(url + bigPlace.key + ".json").execute(object :StringCallback(){
            override fun onSuccess(response: Response<String>?) {
                val data = ArrayList<DataEntity>()
                val map: Map<String, DataEntity> =
                    JSON.parseObject(response?.body()?.toString(), object : TypeReference<Map<String, DataEntity>>() {})
                val m: Set<Map.Entry<String, DataEntity>> = map.entries
                val it: Iterator<Map.Entry<String, DataEntity>> = m.iterator()
                do {
                    val en: Map.Entry<String, DataEntity> = it.next()
                    val json = JSON.toJSON(en.value)
                    val entity1: DataEntity =
                        JSON.parseObject(json.toString(), DataEntity::class.java)
                    entity1.pannoId = entity1.panoid
                    if (bigPlace.fife) {
                        entity1.imageUrl =
                            "https://lh4.googleusercontent.com/" + entity1.pannoId + "/w400-h300-fo90-ya0-pi0/"
                    } else {
                        entity1.imageUrl =
                            "https://geo0.ggpht.com/cbk?output=thumbnail&thumb=2&panoid=" + entity1.panoid
                    }
                    data.add(entity1)
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
                recycler.layoutManager = LinearLayoutManager(this@DetailsActivity)
                fastAdapter.onClickListener = {view, adapter, item, position ->
                    val entity = data[position]
                    mapview.moveMap(Point.fromLngLat(entity.lng, entity.lat))
                    mapview.addMarker(Point.fromLngLat(entity.lng, entity.lat))
                    false
                }
            }
        })
    }
}