package com.example.template.model.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.template.model.entity.ExampleEntity
import com.example.template.model.local.dao.ExampleDao

@Database(version = 1, exportSchema = false, entities = [ExampleEntity::class])

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun exampleDao(): ExampleDao
}
