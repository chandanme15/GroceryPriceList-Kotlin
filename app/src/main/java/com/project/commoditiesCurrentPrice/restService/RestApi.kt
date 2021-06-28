package com.project.commoditiesCurrentPrice.restService

import com.project.commoditiesCurrentPrice.model.RecordsModel
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

interface RestApi {

    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070")
    fun getRecords(@QueryMap filter : Map<String, String>) : Observable<RecordsModel>

}