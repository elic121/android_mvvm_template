package com.example.template.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "example")
data class Example(
    @PrimaryKey val id: Int? = null,

    @SerializedName("X-Cloud-Trace-Context")
    val xCloudTraceContext: String? = "",

    @SerializedName("Accept")
    val accept: String? = "",

    @SerializedName("Upgrade-Insecure-Requests")
    val upgradeInsecureRequests: String? = "",

    @SerializedName("traceparent")
    val traceparent: String? = "",

    @SerializedName("User-Agent")
    val userAgent: String? = "",

    @SerializedName("Host")
    val host: String? = "",

    @SerializedName("Accept-Language")
    val acceptLanguage: String? = ""
)
