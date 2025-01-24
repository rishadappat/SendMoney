package com.appat.sendmoney.data.models

import com.appat.sendmoney.utils.currentLanguage
import kotlinx.serialization.*

@Serializable
data class FormData (
    val title: Title? = null,
    val services: List<Service>? = null
)

@Serializable
data class Service (
    val label: Title? = null,
    val name: String? = null,
    val providers: List<Provider>? = null
)

@Serializable
data class Title (
    val en: String? = null,
    val ar: String? = null
) {
    fun get(): String {
        return if (currentLanguage == "ar") {
            return ar ?: en ?: ""
        } else {
            en ?: ar ?: ""
        }
    }
}

@Serializable
data class Provider (
    val name: String? = null,
    val id: String? = null,

    @SerialName("required_fields")
    val requiredFields: List<RequiredField>? = null
)

@Serializable
data class RequiredField (
    val label: Title? = null,
    val name: String? = null,
    val placeholder: Title? = null,
    val type: Type? = null,
    val validation: String? = null,

    @SerialName("max_length")
    val maxLength: Long? = null,

    @SerialName("validation_error_message")
    val validationErrorMessage: Title? = null,

    val options: List<Option>? = null
)

@Serializable
data class Option (
    val label: String? = null,
    val name: String? = null
)

@Serializable
enum class Type(val value: String) {
    @SerialName("msisdn") Msisdn("msisdn"),
    @SerialName("number") Number("number"),
    @SerialName("option") Option("option"),
    @SerialName("text") Text("text");
}