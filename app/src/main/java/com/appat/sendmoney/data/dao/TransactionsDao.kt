package com.appat.sendmoney.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.appat.sendmoney.data.entities.Transactions
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @JvmSuppressWildcards
    suspend fun insertTransaction(transaction: Transactions): Long

    @Query("SELECT * from Transactions")
    fun getAllTransaction(): Flow<List<Transactions>>
}