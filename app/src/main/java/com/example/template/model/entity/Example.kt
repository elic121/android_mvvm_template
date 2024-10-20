package com.example.template.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "example")
data class Example(
    @PrimaryKey val id: Int? = null,

    @SerializedName("X-Cloud-Trace-Context")
    val xCloudTraceContext: String? = "",

    @SerializedName("traceparent")
    val traceparent: String? = "",

    @SerializedName("User-Agent")
    val userAgent: String? = "",

    @SerializedName("Host")
    val host: String? = "",
)
