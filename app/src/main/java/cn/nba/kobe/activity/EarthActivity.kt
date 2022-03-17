package cn.nba.kobe.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.kobe.R
import cn.nba.kobe.utils.*
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Camera
import gov.nasa.worldwind.layer.BackgroundLayer
import gov.nasa.worldwind.layer.BlueMarbleLandsatLayer
import kotlinx.android.synthetic.main.activity_earth.*
import kotlinx.android.synthetic.main.activity_earth.mapView

class EarthActivity : AppCompatActivity() {
    private var currentLat = 0.0
    private var currentLng = 0.0
    private var currentAltitude = 20000000.0

    private var uriStyle = true
    private val camera: Camera by lazy {
        Camera()
    }
    private val worldWindow: WorldWindow by lazy {
        WorldWindow(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_earth)
        mapView.setCurrentLocation()
        mapView.setCameraClick{lng,lat->
            mapView.moveMap(Point.fromLngLat(lng,lat))
        }
        mapView.loadStyle(Style.OUTDOORS)
        mapView.setCameraChangeListener { lng, lat ->
            currentLng = lng
            currentLat = lat
            resetEarthCamera(camera, worldWindow, currentLat, currentLng, currentAltitude)
        }
        mapView.loadStyle(Style.OUTDOORS)
        changeUriStyle()
        loadDataAndInit()
    }

    private fun changeUriStyle() {
        if (uriStyle) {
            mapView.loadStyle(Style.OUTDOORS)
        } else {
            mapView.loadStyle(Style.SATELLITE_STREETS)
        }
        uriStyle = !uriStyle
    }

    private fun loadDataAndInit() {
        worldWindow.layers.let {
            it.addLayer(BackgroundLayer())
            it.addLayer(BlueMarbleLandsatLayer())
        }
        earthLayout.addView(worldWindow)
        resetEarthCamera(camera, worldWindow, currentLat, currentLng, currentAltitude)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

}