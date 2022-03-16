package cn.nba.james

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.haoge.easyandroid.easy.EasyToast

class PermissionDialog(val fab:FloatingActionButton):AAH_FabulousFragment() {

    private val updateDialog by lazy {
        UpdateDialog(fab)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.fragment_common, null)
        v.findViewById<TextView>(R.id.dialogTitle).text = "Permission"
        v.findViewById<TextView>(R.id.dialogContent).text = result.pkey
        v.findViewById<ImageView>(R.id.notice).apply {
            visibility = View.VISIBLE
            Glide.with(context).load(result.ukey).into(this)
        }
        v.findViewById<Button>(R.id.dialogBtn).apply {
            text = "ok"
            setOnClickListener {
                if (!context.packageManager.canRequestPackageInstalls()){
                    funcSetting(context)
                }else{
                    updateDialog.setParentFab(fab)
                    updateDialog.show((context as AppCompatActivity).supportFragmentManager, updateDialog.tag)
                    dismiss()
                }
            }
        }
        isCancelable = false
        setViewMain(v.findViewById(R.id.main));
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }

}