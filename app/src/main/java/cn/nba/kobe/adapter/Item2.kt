package cn.nba.kobe.adapter

import android.graphics.Color
import android.view.View
import android.widget.TextView
import cn.nba.kobe.R
import cn.nba.kobe.utils.COLORS
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

class Item2 : AbstractItem<Item2.ViewHolder>() {

    var title: String? = null

    override val type: Int
        get() = R.id.fastadapter_sample_item_id

    override val layoutRes: Int
        get() = R.layout.layout_item2

    class ViewHolder(view: View) : FastAdapter.ViewHolder<Item2>(view) {
        var title = view.findViewById<TextView>(R.id.tv)

        override fun bindView(item: Item2, payloads: List<Any>) {
            title.text = item.title
            title.layoutParams.apply {
                width = (200..400).random()
                height = (300..600).random()
            }
            title.setBackgroundColor(Color.parseColor(COLORS[(0..15).random()]))
        }

        override fun unbindView(item: Item2) {
            title.text = null
        }

    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }
}