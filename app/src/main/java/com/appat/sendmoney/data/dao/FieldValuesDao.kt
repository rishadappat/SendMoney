package com.appat.sendmoney.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.appat.sendmoney.data.entities.FieldValues
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldValuesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertFieldValues(fieldValues: FieldValues): Long

    @Upsert
    @JvmSuppressWildcards
    suspend fun insertAll(fieldValues: List<FieldValues>)

    @Query("SELECT * from FieldValues where transactionId = :transactionId")
    fun getFieldValues(transactionId: Long): Flow<List<FieldValues>>
}