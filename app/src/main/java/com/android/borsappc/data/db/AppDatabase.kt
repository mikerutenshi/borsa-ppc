package com.android.borsappc.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.borsappc.data.model.RemoteKey
import com.android.borsappc.data.model.Work

@Database(entities = [
    Work::class,
    RemoteKey::class],
    version = 1, exportSchema = false)
@TypeConverters(DataConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun workDao(): WorkDao
    abstract fun productDao(): ProductDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}