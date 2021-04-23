package com.util.library.common.log

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.util.library.R

class LogAdapter(val list: RecyclerView) :
    BaseQuickAdapter<CmdItem, BaseViewHolder>(R.layout.item_log) {

    var isSmoothToLatest = true

    @SuppressLint("ResourceType")
    override fun convert(holder: BaseViewHolder, item: CmdItem) {
        holder.setText(R.id.text_time, item.time)
            .setText(R.id.text_content, item.content)
            .setTextColor(
                R.id.text_content,
                item.textColor
            )

    }

    fun add(content: String, @ColorInt textColor: Int = Color.BLACK) {
        addData(CmdItem(content, textColor))
        if (isSmoothToLatest) {
            list.smoothScrollToPosition(data.size - 1)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }
}