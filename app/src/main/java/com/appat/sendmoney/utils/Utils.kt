package com.appat.sendmoney.utils

import android.app.LocaleManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

suspend inline fun <reified T> Context.readRawJson(@RawRes rawResId: Int): T = withContext(Dispatchers.IO) {
    resources.openRawResource(rawResId).bufferedReader().use {
        Json.decodeFromString(it.readText())
    }
}

var currentLanguage = "en"

fun String.validate(regex: String): Boolean {
    if (this.isEmpty()) {
        return false
    }
    if (regex.isEmpty()) {
        return true
    }
    return Regex(regex).matches(this)
}

fun getTimeStamp(): Long {
    return Date().time
}

fun epochToIso8601(time: Long): String {
    val format = "dd MMM yyyy hh:mm:ss aa" // you can add the format you need
    val sdf = SimpleDateFormat(format, Locale.getDefault()) // default local
    sdf.timeZone = TimeZone.getDefault() // set anytime zone you need
    return sdf.format(Date(time))
}

fun changeLanguage(context: Context){
    val language = context.resources.configuration.locales.get(0).language
    val newLanguage = if (language.contains("en")) {
        "ar"
    } else {
        "en"
    }
    currentLanguage = newLanguage
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(newLanguage)
    }else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLanguage))
    }
}