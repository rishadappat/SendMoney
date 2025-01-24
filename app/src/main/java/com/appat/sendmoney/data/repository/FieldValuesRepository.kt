package com.appat.sendmoney.data.repository

import androidx.room.Query
import androidx.room.Upsert
import com.appat.sendmoney.data.dao.FieldValuesDao
import com.appat.sendmoney.data.entities.FieldValues
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FieldValuesRepository @Inject constructor(private val fieldValuesDao: FieldValuesDao) {
    suspend fun insertFieldValues(fieldValues: FieldValues): Long = fieldValuesDao.insertFieldValues(fieldValues)

    suspend fun insertAll(fieldValues: List<FieldValues>) = fieldValuesDao.insertAll(fieldValues)

    fun getFieldValues(transactionId: Long): Flow<List<FieldValues>> = fieldValuesDao.getFieldValues(transactionId)
}