package cn.nba.james

import android.app.Dialog
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class UpdateDialog(val fab: FloatingActionButton) : AAH_FabulousFragment() {

    private val download by lazy {
        DownloadDialog(fab)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.fragment_common, null)
        v.findViewById<TextView>(R.id.dialogTitle).text = "New Version"
        var msg: ArrayList<String> = ArrayList()
        var s = ""
        if (result.ikey.contains("|")) {
            msg = result.ikey.split("|") as ArrayList<String>
            msg.forEach {
                s = "$s$it\n"
            }
        }
        v.findViewById<TextView>(R.id.dialogContent).text = s
        v.findViewById<Button>(R.id.dialogBtn).apply {
            text = "Download"
            setOnClickListener {
                download.setParentFab(fab)
                download.show((context as AppCompatActivity).supportFragmentManager, download.tag)
                (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO){
                    delay(500)
                    dismiss()
                    withContext(Dispatchers.Main){
                        funcDownload(context, result.path) { progress ->
                            Log.i("xxxxxxH","pro = $progress")
                            download.setProgress(progress)
                            if (progress == 100){
                                funcInstall(context, File(filePath+ fileName))
                                download.dismiss()
                            }
                        }
                    }
                }
            }
        }
        isCancelable = false
        setViewMain(v.findViewById(R.id.main))
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }
}