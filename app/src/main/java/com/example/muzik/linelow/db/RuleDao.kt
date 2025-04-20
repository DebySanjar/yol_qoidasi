package com.example.muzik.linelow.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface RuleDao {
    @Insert
    suspend fun insert(rule: Rule)

    @Update
    suspend fun update(rule: Rule)

    @Delete
    suspend fun delete(rule: Rule)

    @Query("SELECT * FROM rules WHERE category = :category")
    suspend fun getRulesByCategory(category: String): List<Rule>

    @Query("SELECT * FROM rules WHERE isLiked = 1")
    suspend fun getLikedRules(): List<Rule>
}