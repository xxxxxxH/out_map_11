package cn.nba.kobe

import android.Manifest
import cn.nba.james.BaseApp
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.search.MapboxSearchSdk
import com.tencent.mmkv.MMKV
import java.util.*

class IApplication:BaseApp() {
    override fun getAppId(): String {
        return "361"
    }

    override fun getAppName(): String {
        return "net.basicmodel"
    }

    override fun getUrl(): String {
        return "https://smallfun.xyz/worldweather361/"
    }

    override fun getAesPassword(): String {
        return "VPWaTtwYVPS1PeQP"
    }

    override fun getAesHex(): String {
        return "jQ4GbGckQ9G7ACZv"
    }

    override fun getToken(): String {
        var token = ""
        token = if (MMKV.defaultMMKV()!!.decodeString("token","") == ""){
            UUID.randomUUID().toString()
        }else{
            MMKV.defaultMMKV()!!.decodeString("token","")!!
        }
        return token
    }

    override fun getPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun init() {
        super.init()
        MapboxSearchSdk.initialize(
            application = this,
            accessToken = getString(R.string.mapbox_access_token),
            locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        )
    }
}