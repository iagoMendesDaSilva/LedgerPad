package com.iago.ledgerbook.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromColor(color: Color): Long = color.value.toLong()

    @TypeConverter
    fun toColor(value: Long): Color = Color(value.toULong())

    @TypeConverter
    fun fromImageVector(imageVector: ImageVector): String {
        return gson.toJson(imageVector)
    }

    @TypeConverter
    fun toImageVector(data: String): ImageVector {
        val type = object : TypeToken<ImageVector>() {}.type
        return gson.fromJson(data, type)
    }
}
