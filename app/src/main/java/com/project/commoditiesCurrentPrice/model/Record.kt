package com.project.commoditiesCurrentPrice.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "RecordTABLE")
class Record {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "state")
    @SerializedName("state")
    @Expose
    var state: String? = null

    @ColumnInfo(name = "district")
    @SerializedName("district")
    @Expose
    var district: String? = null

    @ColumnInfo(name = "market")
    @SerializedName("market")
    @Expose
    var market: String? = null

    @ColumnInfo(name = "commodity")
    @SerializedName("commodity")
    @Expose
    var commodity: String? = null

    @ColumnInfo(name = "variety")
    @SerializedName("variety")
    @Expose
    var variety: String? = null

    @ColumnInfo(name = "arrival_date")
    @SerializedName("arrival_date")
    @Expose
    var arrival_date: String? = null

    @ColumnInfo(name = "min_price")
    @SerializedName("min_price")
    @Expose
    var min_price: String? = null

    @ColumnInfo(name = "max_price")
    @SerializedName("max_price")
    @Expose
    var max_price: String? = null

    @ColumnInfo(name = "modal_price")
    @SerializedName("modal_price")
    @Expose
    var modal_price: String? = null

    var isSubItemVisible = false
}