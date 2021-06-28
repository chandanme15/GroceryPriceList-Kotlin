package com.project.commoditiesCurrentPrice.repository

import android.content.Context
import androidx.room.Room
import com.project.commoditiesCurrentPrice.MyApplication
import com.project.commoditiesCurrentPrice.dbRoom.RecordsDB
import com.project.commoditiesCurrentPrice.model.Record
import com.project.commoditiesCurrentPrice.restService.RestClient
import com.project.commoditiesCurrentPrice.utils.Constants
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class Repository private constructor(context : Context) {

    private val recordsDB = Room.databaseBuilder(context, RecordsDB::class.java, RecordsDB::class.simpleName!!).build()
    private val restClient = RestClient()
    private val subscription  = CompositeSubscription()
    private val mQueryMap = initApiQueryMap()

    companion object {
        var instance : Repository? = null
            get() {
                if(field == null) {
                    synchronized (Repository::class.java) {
                        if(null == field) {
                            field = Repository(MyApplication.instance)
                        }
                    }
                }
                return field
            }
            private set
    }

    private fun initApiQueryMap() = HashMap<String, String>().apply {
        put(Constants.QueryMap.ATTRIBUTE_API_KEY, Constants.API_KEY)
        put(Constants.QueryMap.ATTRIBUTE_FORMAT, Constants.API_RESPONSE_FORMAT)
        put(Constants.QueryMap.ATTRIBUTE_LIMIT, Constants.NO_OF_RECORDS_PER_REQUEST)
    }

    fun updateApiQueryMap() = mQueryMap.put(Constants.QueryMap.ATTRIBUTE_OFFSET, "${(Constants.PAGE_COUNT - 1) * 10}")

    fun getRecordsFromAPI() = restClient.restApi.getRecords(mQueryMap)

    fun subscribe(subscription : Subscription) = this.subscription.add(subscription)

    fun unsubscribe() = subscription.unsubscribe()

    fun insertRecordsToDB(recordList : List<Record>) = recordsDB?.recordsDao()?.insertRecords(recordList)

    fun deleteAllRecordsFromDB() = recordsDB?.recordsDao()?.deleteAllRecords()

    fun readRecordsFromDB() = recordsDB?.recordsDao()?.records

    fun closeDB() = recordsDB?.close()
}
