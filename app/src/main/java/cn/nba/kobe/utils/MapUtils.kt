package cn.nba.kobe.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cn.nba.kobe.R
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.WorldWindow
import gov.nasa.worldwind.geom.Camera

val COLORS = arrayOf(
    "#E6F44336",
    "#E6E91E63",
    "#E69C27B0",
    "#E6673AB7",
    "#E63F51B5",
    "#E62196F3",
    "#E603A9F4",
    "#E600BCD4",
    "#E6009688",
    "#E64CAF50",
    "#E68BC34A",
    "#E6CDDC39",
    "#E6FFEB3B",
    "#E6FFC107",
    "#E6FF9800",
    "#E6FF5722"
)

fun MapView.addMarker(p: Point) {
    val bitmap: Bitmap = BitmapFactory.decodeResource(this.context.resources, R.mipmap.red_marker)
    annotations.cleanup()
    val markerManager = annotations.createPointAnnotationManager(AnnotationConfig())
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(p)
        .withIconImage(bitmap)
    markerManager.create(pointAnnotationOptions)
}

fun MapView.moveMap(p: Point) {
    getMapboxMap().flyTo(cameraOptions {
        center(p)
        zoom(14.0)
    })
}

fun MapView.setCurrentLocation() {
    val listener = object : OnIndicatorPositionChangedListener {
        override fun onIndicatorPositionChanged(point: Point) {
            setTag(R.id.appViewMyLocationId, point)
            getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
            gestures.focalPoint = getMapboxMap().pixelForCoordinate(point)
            location.removeOnIndicatorPositionChangedListener(this)
        }
    }
    location.addOnIndicatorPositionChangedListener(listener)
}

fun MapView.setCameraClick(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnMapClickListener {
        block(it.longitude(), it.latitude())
        true
    }
}

fun MapView.setCameraChangeListener(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnCameraChangeListener {
        getMapboxMap().cameraState.center.let {
            block(it.longitude(), it.latitude())
        }
    }
}

fun MapView.setCamera(center: Point) {
    getMapboxMap().setCamera(
        cameraOptions {
            center(center)
            zoom(14.0)
        }
    )
}

fun MapView.loadStyle(style: String) {
    getMapboxMap().apply {
        setCamera(cameraOptions {
            zoom(14.0)
        })
        loadStyleUri(style) {
            addMyLocationPoint()
        }
    }
}

private fun MapView.addMyLocationPoint() {
    location.updateSettings {
        enabled = true
        pulsingEnabled = true
    }
}

fun resetEarthCamera(
    camera: Camera,
    worldWindow: WorldWindow,
    currentLat: Double,
    currentLng: Double,
    currentAltitude: Double
) {
    camera.set(currentLat, currentLng, currentAltitude, WorldWind.ABSOLUTE, 0.0, 0.0, 1.0)
    worldWindow.globe?.let {
        worldWindow.navigator.setAsCamera(it, camera)
        worldWindow.requestRedraw()
    }
}
