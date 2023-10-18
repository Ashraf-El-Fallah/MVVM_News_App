package com.af.newsapp.db

import androidx.room.TypeConverters
import com.af.newsapp.models.Source

class Converters {
    @TypeConverters
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverters
    fun toSource(name: String): Source {
        return Source(name, name)
    }

}