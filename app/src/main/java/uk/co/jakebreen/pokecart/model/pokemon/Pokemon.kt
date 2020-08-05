package uk.co.jakebreen.pokecart.model.pokemon

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.co.jakebreen.pokecart.model.type.Type

@Entity(tableName = "pokemon_table")
data class Pokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    val health: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    @ColumnInfo(name = "type_primary")
    val typePrimary: Type,
    @ColumnInfo(name = "type_secondary")
    val typeSecondary: Type
)

