package com.appat.sendmoney.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


@Serializable
@Entity(tableName = "FieldValues")
data class FieldValues(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    var id: Long? = null,

    @ColumnInfo
    var transactionId: Long,

    @ColumnInfo
    var fieldName: String,

    @ColumnInfo
    var value: String
)