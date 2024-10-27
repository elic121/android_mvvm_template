package com.example.template.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.template.model.entity.ExampleEntity

@Dao
interface ExampleDao {
    @Query("SELECT * FROM example")
    suspend fun getExamples(): List<ExampleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exampleEntity: ExampleEntity)
}