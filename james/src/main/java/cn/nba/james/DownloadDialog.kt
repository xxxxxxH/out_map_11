package cn.nba.james

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.allattentionhere.fabulousfilter.AAH_FabulousFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DownloadDialog(fab: FloatingActionButton):AAH_FabulousFragment() {
    var currentTv: TextView? = null
    var progressBar: ProgressBar? = null
    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = View.inflate(context, R.layout.fragment_common, null)
        v.findViewById<TextView>(R.id.dialogTitle).text = "Download"
        v.findViewById<TextView>(R.id.dialogContent).apply {
            visibility = View.GONE
        }
        v.findViewById<LinearLayout>(R.id.dialogProgressLl).apply {
            visibility = View.VISIBLE
        }
        v.findViewById<Button>(R.id.dialogBtn).apply {
            visibility = View.GONE
        }
        currentTv = v.findViewById(R.id.downloadTv)
        progressBar = v.findViewById(R.id.progress)
        isCancelable = false
        setViewMain(v.findViewById(R.id.main));
        setMainContentView(v)
        super.setupDialog(dialog, style)
    }

    @SuppressLint("SetTextI18n")
    fun setProgress(progress: Int) {
        currentTv?.let {
            it.text = "current: $progress%"
        }
        progressBar?.let {
            it.progress = progress
        }
    }
}