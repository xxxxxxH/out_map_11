package cn.nba.james

import android.app.Application
import com.haoge.easyandroid.EasyAndroid
import com.lzy.okgo.OkGo
import com.lzy.okgo.https.HttpsUtils
import com.tencent.mmkv.MMKV
import okhttp3.OkHttpClient
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession


abstract class BaseApp : Application() {
    companion object {
        var instance: BaseApp? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this)
        EasyAndroid.init(this)
        val sslParams = HttpsUtils.getSslSocketFactory()
        val builder = OkHttpClient.Builder()
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
            .hostnameVerifier { hostname, session -> true }.build()
        OkGo.getInstance().init(this).okHttpClient = builder
        init()
    }

    open fun init(){

    }

    abstract fun getAppId(): String
    abstract fun getAppName(): String
    abstract fun getUrl(): String
    abstract fun getAesPassword(): String
    abstract fun getAesHex(): String
    abstract fun getToken(): String
    abstract fun getPermissions(): Array<String>
}