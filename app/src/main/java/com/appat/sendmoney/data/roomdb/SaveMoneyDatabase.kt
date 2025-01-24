package com.appat.sendmoney.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appat.sendmoney.data.dao.FieldValuesDao
import com.appat.sendmoney.data.dao.TransactionsDao
import com.appat.sendmoney.data.entities.FieldValues
import com.appat.sendmoney.data.entities.Transactions

@Database(
    entities = [Transactions::class, FieldValues::class],
    version = 1,
    exportSchema = false
)
abstract class SaveMoneyDatabase: RoomDatabase() {
    abstract fun fieldValuesDao(): FieldValuesDao
    abstract fun transactionsDao(): TransactionsDao

    companion object {
        @Volatile private var instance: SaveMoneyDatabase? = null
        private const val DATABASE_NAME = "savemoney"

        fun getInstance(context: Context): SaveMoneyDatabase {
            if (instance == null) {
                try {
                    instance = Room.databaseBuilder(context, SaveMoneyDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                    return instance as SaveMoneyDatabase
                }
            }
            return instance as SaveMoneyDatabase
        }
    }
}