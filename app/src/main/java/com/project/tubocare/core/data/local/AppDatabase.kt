package com.project.tubocare.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.project.tubocare.article.data.local.ArticleDao
import com.project.tubocare.article.data.local.ArticleEntity
import com.project.tubocare.auth.data.local.UserDao
import com.project.tubocare.auth.data.local.UserEntity

@Database(
    entities =
    [
        UserEntity::class,
        ArticleEntity::class,
    ],
    version = 6,
    exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun articleDao(): ArticleDao
}