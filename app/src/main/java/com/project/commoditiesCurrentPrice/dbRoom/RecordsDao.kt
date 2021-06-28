package com.project.commoditiesCurrentPrice.dbRoom

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.commoditiesCurrentPrice.model.Record

@Dao
interface RecordsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecords(recordList: List<Record?>?)

    @Query("DELETE FROM RecordTABLE")
    fun deleteAllRecords()

    @get:Query("SELECT * FROM RecordTABLE")
    val records: List<Record>?
}