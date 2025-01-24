package com.appat.sendmoney.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "Transactions")
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long? = null,

    @ColumnInfo
    var timeStamp: Long,

    @ColumnInfo
    var service: String,

    @ColumnInfo
    var provider: String,
)