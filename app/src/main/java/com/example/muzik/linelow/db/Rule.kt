package com.example.muzik.linelow.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rules")
data class Rule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val imagePath: String,
    val category: String,
    val isLiked: Boolean = false
)