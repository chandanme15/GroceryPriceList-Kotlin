package com.project.commoditiesCurrentPrice.callbacks

import androidx.recyclerview.widget.DiffUtil;

import com.project.commoditiesCurrentPrice.model.Record;

class RecordDiffCallback(private var oldList: List<Record>, private var newList: List<Record>) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition : Int, newItemPosition : Int) = oldList[oldItemPosition] == newList[newItemPosition]

    override fun areContentsTheSame(oldItemPosition : Int, newItemPosition : Int) = oldList[oldItemPosition] == newList[newItemPosition]
}