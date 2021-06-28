package com.project.commoditiesCurrentPrice.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RecordsModel {
    @SerializedName("records")
    @Expose
    var records: MutableList<Record>? = null
}