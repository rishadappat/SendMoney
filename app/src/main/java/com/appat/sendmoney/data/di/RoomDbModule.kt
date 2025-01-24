package com.appat.sendmoney.data.di

import android.content.Context
import com.appat.sendmoney.data.roomdb.SaveMoneyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomDbModule {
    @Singleton
    @Provides
    fun providesSaveMoneyDatabase(@ApplicationContext appContext: Context): SaveMoneyDatabase {
        return SaveMoneyDatabase.getInstance(appContext)
    }
}