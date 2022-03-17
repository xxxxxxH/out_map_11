package cn.nba.kobe.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
import cn.nba.kobe.utils.*
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private val lat by lazy {
        intent.getDoubleExtra("lat", 0.0)
    }
    private val lng by lazy {
        intent.getDoubleExtra("lng", 0.0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        if (lat == 0.0 || lng == 0.0) {
            finish()
        }
        mapView.let {
            it.setCurrentLocation()
            it.setCameraClick { lng, lat ->
                it.moveMap(Point.fromLngLat(lng, lat))
            }
            it.loadStyle(Style.OUTDOORS)
            it.addMarker(Point.fromLngLat(lng, lat))
            it.moveMap(Point.fromLngLat(lng, lat))
        }
    }
}