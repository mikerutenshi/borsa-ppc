package com.android.borsappc.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.borsappc.data.model.RemoteKey
import com.android.borsappc.data.model.Work

@Database(entities = [
    Work::class,
    RemoteKey::class],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean): AppDatabase {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            } else {
                Room.databaseBuilder(context, AppDatabase::class.java, "borsa_ppc.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    abstract fun workDao(): WorkDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}