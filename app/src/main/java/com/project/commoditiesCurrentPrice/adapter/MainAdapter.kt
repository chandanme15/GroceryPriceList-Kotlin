package com.project.commoditiesCurrentPrice.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.project.commoditiesCurrentPrice.callbacks.RecordDiffCallback;
import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.R;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import java.util.ArrayList;
import java.util.Collections;

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    val mData = ArrayList<Record>()

    @NonNull
    override fun onCreateViewHolder(@NonNull parent : ViewGroup, viewType : Int) : ViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.row_item,parent,false);
        return ViewHolder(mView);
    }

    override fun onBindViewHolder(@NonNull mViewHolder : ViewHolder, position : Int) {
        mViewHolder.bind(mData[position]);
    }

    override fun getItemCount(): Int = mData.size

    fun getData() = mData

    fun clearData() {
        mData.clear()
        notifyDataSetChanged()
    }

    fun addData(recordList : List<Record>) {
        mData.addAll(recordList);
        notifyItemRangeInserted(mData.size, recordList.size);
    }

    fun sortDataByState() {
        GlobalScope.launch(Dispatchers.Main) {
            sort(mData, false).dispatchUpdatesTo(this@MainAdapter)
        }
    }

    fun sortDataByModalPrice() {
        GlobalScope.launch(Dispatchers.Main) {
            sort(mData, true).dispatchUpdatesTo(this@MainAdapter)
        }
    }

    private suspend fun sort(list : List<Record>, flag : Boolean) : DiffUtil.DiffResult {
        return withContext(Dispatchers.Default) {
            val oldList = ArrayList(list);
            if (flag) {
                oldList.sortWith { o1, o2 ->
                    (o1.modal_price?.toInt() ?: -1).compareTo(o2.modal_price?.toInt() ?: -1)
                }
            } else {
                oldList.sortWith { o1, o2 ->
                    o2.state?.let { o1.state?.compareTo(it) } ?: 0
                }
            }
            val diffCallback = RecordDiffCallback(oldList, list);
            DiffUtil.calculateDiff(diffCallback)
        }
    }

    class ViewHolder(val mView : View) : RecyclerView.ViewHolder(mView) {
        private val mMarket = mView.findViewById(R.id.market) as TextView
        private val mCommodity = mView.findViewById(R.id.commodity) as TextView
        private val mRegion = mView.findViewById(R.id.region) as TextView
        private val mModalPrice = mView.findViewById(R.id.modalPrice) as TextView
        private val mMinPrice = mView.findViewById(R.id.minPrice) as TextView
        private val mMaxPrice = mView.findViewById(R.id.maxPrice) as TextView

        val mSubItem = mView.findViewById(R.id.subItem) as LinearLayout

        fun bind(record : Record) {
            record.market?.let { mMarket.setText(it) }
            record.commodity?.let { mCommodity.setText(it) }
            record.district?.let { district -> record.state?.let { state -> {mRegion.setText("${district}, ${state}")} }}
            record.min_price?.let { mMinPrice.setText(it) }
            record.max_price?.let { mMaxPrice.setText(it) }
            record.modal_price?.let { mModalPrice.setText(it) }
        }
    }
}
