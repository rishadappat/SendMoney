package com.appat.sendmoney.data.repository

import androidx.room.Query
import com.appat.sendmoney.data.dao.TransactionsDao
import com.appat.sendmoney.data.entities.Transactions
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionsRepository @Inject constructor(private val transactionsDao: TransactionsDao) {

    suspend fun insertTransaction(transaction: Transactions): Long = transactionsDao.insertTransaction(transaction)

    fun getAllTransaction(): Flow<List<Transactions>> = transactionsDao.getAllTransaction()
}