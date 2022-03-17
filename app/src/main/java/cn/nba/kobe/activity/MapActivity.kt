package cn.nba.kobe.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
import cn.nba.kobe.utils.addMarker
import cn.nba.kobe.utils.moveMap
import cn.nba.kobe.utils.setCameraClick
import cn.nba.kobe.utils.setCurrentLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.sevenheaven.segmentcontrol.SegmentControl
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_map.*

class MapActivity : AppCompatActivity() {

    private val mapBox by lazy {
        mapView.getMapboxMap()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView.setCurrentLocation()
        radioButton.setDirection(SegmentControl.Direction.HORIZONTAL)
        radioButton.setOnSegmentControlClickListener {
            when (it) {
                0 -> {
                    mapBox.loadStyleUri(Style.OUTDOORS)
                }
                1 -> {
                    mapBox.loadStyleUri(Style.SATELLITE_STREETS)
                }
                2 -> {
                    mapBox.loadStyleUri(Style.LIGHT)

                }
                3 -> {
                    mapBox.loadStyleUri(Style.TRAFFIC_DAY)
                }
            }
        }
    }
}