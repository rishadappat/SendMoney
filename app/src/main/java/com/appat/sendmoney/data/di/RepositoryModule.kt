package com.appat.sendmoney.data.di

import com.appat.sendmoney.data.dao.FieldValuesDao
import com.appat.sendmoney.data.dao.TransactionsDao
import com.appat.sendmoney.data.repository.FieldValuesRepository
import com.appat.sendmoney.data.repository.SendMoneyRepository
import com.appat.sendmoney.data.repository.TransactionsRepository
import com.appat.sendmoney.data.roomdb.SaveMoneyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Singleton
    @Provides
    fun providesFieldValuesDao(sendMoneyDatabase: SaveMoneyDatabase): FieldValuesDao {
        return sendMoneyDatabase.fieldValuesDao()
    }

    @Singleton
    @Provides
    fun providesFieldValuesRepository(fieldValuesDao: FieldValuesDao): FieldValuesRepository {
        return FieldValuesRepository(fieldValuesDao)
    }

    @Singleton
    @Provides
    fun providesTransactionsDao(sendMoneyDatabase: SaveMoneyDatabase): TransactionsDao {
        return sendMoneyDatabase.transactionsDao()
    }

    @Singleton
    @Provides
    fun providesTransactionsRepository(transactionsDao: TransactionsDao): TransactionsRepository {
        return TransactionsRepository(transactionsDao)
    }

    @Singleton
    @Provides
    fun providesSendMoneyRepository(): SendMoneyRepository {
        return SendMoneyRepository()
    }
}