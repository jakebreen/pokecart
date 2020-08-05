package uk.co.jakebreen.pokecart.model.type

import androidx.room.TypeConverter

class TypeConverter {
    @TypeConverter
    fun fromString(value: String) = Type.getTypeByName(value)

    @TypeConverter
    fun fromList(type: Type) = type.type
}