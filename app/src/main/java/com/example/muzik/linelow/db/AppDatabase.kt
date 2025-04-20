package com.example.muzik.linelow.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Rule::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ruleDao(): RuleDao
}