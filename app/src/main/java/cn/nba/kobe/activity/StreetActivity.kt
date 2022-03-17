package cn.nba.kobe.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
import cn.nba.kobe.utils.setCamera
import cn.nba.kobe.utils.setCameraChangeListener
import cn.nba.kobe.utils.setCurrentLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import kotlinx.android.synthetic.main.activity_street.*

@SuppressLint("ClickableViewAccessibility")
class StreetActivity : AppCompatActivity() {
    private var isTopTouch = false
    private var isBottomTouch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street)
        mapview1.let {
            it.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    isTopTouch = true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    isTopTouch = false
                }
                false
            }
            it.setCurrentLocation()
            it.setCameraChangeListener { d, d2 ->
                if (isTopTouch && !isBottomTouch) {
                    mapview2.setCamera(Point.fromLngLat(d, d2))
                }
            }
            it.getMapboxMap().loadStyleUri(Style.OUTDOORS)
        }

        mapview2.let {
            it.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    isBottomTouch = true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    isBottomTouch = false
                }
                false
            }

            it.setCurrentLocation()
            it.setCameraChangeListener { lng, lat ->
                if (!isTopTouch && isBottomTouch) {
                    mapview1.setCamera(Point.fromLngLat(lng, lat))
                }
            }
            it.getMapboxMap().loadStyleUri(Style.OUTDOORS)
        }
    }
}