package com.appat.sendmoney.data.repository

import android.content.Context
import com.appat.sendmoney.R
import com.appat.sendmoney.data.models.FormData
import com.appat.sendmoney.utils.readRawJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SendMoneyRepository() {
    fun getFormData(context: Context) = flow<FormData?> {
        emit(context.readRawJson(R.raw.data))
    }.catch {
        it.printStackTrace()
        emit(null)
    }.flowOn(Dispatchers.IO)
}