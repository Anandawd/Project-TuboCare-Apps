package com.project.tubocare.core.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.project.tubocare.medication.domain.model.ChecklistEntry
import java.time.LocalTime
import java.util.Date

class Converters {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let{ Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromString(value: String): Map<String, List<LocalTime>> {
        val mapType = object : TypeToken<Map<String, List<LocalTime>>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: Map<String, List<LocalTime>>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun fromChecklistString(value: String): Map<String, ChecklistEntry> {
        val mapType = object : TypeToken<Map<String, ChecklistEntry>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun fromChecklistMap(map: Map<String, ChecklistEntry>): String {
        return Gson().toJson(map)
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromTimeDtoToString(timeDto: TimeDto?): String? {
        return timeDto.let {
            Gson().toJson(timeDto)
        }
    }

    @TypeConverter
    fun toTimeDtoFromString(timeDtoString: String?): TimeDto? {
        return timeDtoString?.let {
            Gson().fromJson(it, TimeDto::class.java)
        }
    }
}