package com.project.commoditiesCurrentPrice.viewModel

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewModelScope

import com.project.commoditiesCurrentPrice.model.Record;
import com.project.commoditiesCurrentPrice.model.RecordsModel;
import com.project.commoditiesCurrentPrice.repository.Repository;
import com.project.commoditiesCurrentPrice.utils.Constants;
import com.project.commoditiesCurrentPrice.utils.SharedPreferencesHelper;
import com.project.commoditiesCurrentPrice.utils.Util;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

class MainViewModel(private val repository: Repository?, private val listener: DatabaseProcessInterface) : ViewModel() {

    val apiData = MutableLiveData<MutableList<Record>>()
    val error = MutableLiveData<Boolean>()

    fun loadRecords() {
        repository?.updateApiQueryMap();
        repository?.subscribe(
                repository.getRecordsFromAPI()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it?.records != null) {
                                apiData.postValue(it.records);
                                error.postValue(false);
                            } else {
                                error.postValue(true);
                            }
                        }, { error.postValue(true) })
        )
    }

    fun clearRepo() {
        repository?.unsubscribe()
        repository?.closeDB()
    }

    fun purgeDatabase() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository?.deleteAllRecordsFromDB();
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    fun insertRecordsToDatabase(recordList: List<Record>, pageCount: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                if (pageCount == 1) {
                    SharedPreferencesHelper.dBEntryTime = Util.getCurrentDateAndTime()
                    repository?.deleteAllRecordsFromDB();
                }
                repository?.insertRecordsToDB(recordList);
            } catch (e: Exception) {
                e.printStackTrace();
            }
        }
    }

    fun bIsLoadDataFromDatabase() {
        listener.onReadRecordFromDBCompleted(repository?.readRecordsFromDB());
    }

    fun bIsDatabaseExpired(): Boolean {
        val dbEntryTime = SharedPreferencesHelper.dBEntryTime
        if (dbEntryTime != null) {
            val lastSavedTime = dbEntryTime.toLong()
            val currentTime = Util.getCurrentDateAndTime().toLong()
            return (currentTime - lastSavedTime) > Constants.DB_EXPIRY_TIME;
        }
        return true;
    }

    interface DatabaseProcessInterface {

        fun onReadRecordFromDBCompleted(recordList: List<Record>?)

    }
}

