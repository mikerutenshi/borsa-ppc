package com.android.borsappc.data.db

import androidx.room.TypeConverter
import com.android.borsappc.data.model.WorkDone
import com.android.borsappc.data.model.WorkInProgress
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {
    @TypeConverter
    fun fromWorkInProgressList(value: List<WorkInProgress>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WorkInProgress>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWorkInProgressList(value: String): List<WorkInProgress> {
        val gson = Gson()
        val type = object : TypeToken<List<WorkInProgress>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromWorkDoneList(value: List<WorkDone>): String {
        val gson = Gson()
        val type = object : TypeToken<List<WorkDone>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWorkDoneList(value: String): List<WorkDone> {
        val gson = Gson()
        val type = object : TypeToken<List<WorkDone>>() {}.type
        return gson.fromJson(value, type)
    }
}