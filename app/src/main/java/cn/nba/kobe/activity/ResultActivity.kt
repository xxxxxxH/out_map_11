package cn.nba.kobe.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
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
            it.apply {
                val listener = object : OnIndicatorPositionChangedListener {
                    override fun onIndicatorPositionChanged(point: Point) {
                        setTag(R.id.appViewMyLocationId, point)
                        getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
                        gestures.focalPoint = getMapboxMap().pixelForCoordinate(point)
                        location.removeOnIndicatorPositionChangedListener(this)
                    }
                }
                location.addOnIndicatorPositionChangedListener(listener)
                it.getMapboxMap().addOnMapClickListener {
                    this.getMapboxMap().flyTo(cameraOptions {
                        center(Point.fromLngLat(lng, lat))
                        zoom(14.0)
                    })
                    true
                }
                this.getMapboxMap().loadStyleUri(Style.OUTDOORS)
                Point.fromLngLat(lng, lat).let {

                }
            }

        }
    }
}