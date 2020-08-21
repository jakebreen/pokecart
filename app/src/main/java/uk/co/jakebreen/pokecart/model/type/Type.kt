package uk.co.jakebreen.pokecart.model.type

import uk.co.jakebreen.pokecart.R

enum class Type(val type: String) {
    NORMAL("normal"),
    FIGHTING("fighting"),
    FLYING("flying"),
    POISON("poison"),
    GROUND("ground"),
    ROCK("rock"),
    BUG("bug"),
    GHOST("ghost"),
    FIRE("fire"),
    WATER("water"),
    GRASS("grass"),
    ELECTRIC("electric"),
    PSYCHIC("psychic"),
    ICE("ice"),
    DRAGON("dragon"),
    FAIRY("fairy"),
    STEEL("steel"),
    NONE("");

    companion object {
        fun getTypeByName(name: String): Type? {
            return when (name) {
                NORMAL.type -> NORMAL
                FIGHTING.type -> FIGHTING
                FLYING.type -> FLYING
                POISON.type -> POISON
                GROUND.type -> GROUND
                ROCK.type -> ROCK
                BUG.type -> BUG
                GHOST.type -> GHOST
                FIRE.type -> FIRE
                WATER.type -> WATER
                GRASS.type -> GRASS
                ELECTRIC.type -> ELECTRIC
                PSYCHIC.type -> PSYCHIC
                ICE.type -> ICE
                DRAGON.type -> DRAGON
                FAIRY.type -> FAIRY
                STEEL.type -> STEEL
                else -> NONE
            }
        }

        fun getResourceDrawableByType(type: Type): Int {
            return when (type) {
                NORMAL -> R.drawable.ic_type_normal_128
                FIGHTING -> R.drawable.ic_type_fighting_128
                FLYING -> R.drawable.ic_type_flying_128
                POISON -> R.drawable.ic_type_poison_128
                GROUND -> R.drawable.ic_type_ground_128
                ROCK -> R.drawable.ic_type_rock_128
                BUG -> R.drawable.ic_type_bug_128
                GHOST -> R.drawable.ic_type_ghost_128
                FIRE -> R.drawable.ic_type_fire_128
                WATER -> R.drawable.ic_type_water_128
                GRASS -> R.drawable.ic_type_grass_128
                ELECTRIC -> R.drawable.ic_type_electric_128
                PSYCHIC -> R.drawable.ic_type_psychic_128
                ICE -> R.drawable.ic_type_ice_128
                DRAGON -> R.drawable.ic_type_dragon_128
                FAIRY -> R.drawable.ic_type_fairy_128
                STEEL -> R.drawable.ic_type_steel_128
                NONE -> R.drawable.type_none_drawable
            }
        }

        fun getResourceIdByType(type: Type): Int {
            return when(type) {
                NORMAL -> R.id.type_normal
                FIGHTING -> R.id.type_fighting
                FLYING -> R.id.type_flying
                POISON -> R.id.type_poison
                GROUND -> R.id.type_ground
                ROCK -> R.id.type_rock
                BUG -> R.id.type_bug
                GHOST -> R.id.type_ghost
                FIRE -> R.id.type_fire
                WATER -> R.id.type_water
                GRASS -> R.id.type_grass
                ELECTRIC -> R.id.type_electric
                PSYCHIC -> R.id.type_psychic
                ICE -> R.id.type_ice
                DRAGON -> R.id.type_dragon
                FAIRY -> R.id.type_fairy
                STEEL -> R.id.type_steel
                NONE -> R.id.type_none
            }
        }

        fun getTypeByResourceId(id: Int): Type {
            return when(id) {
                R.id.type_normal -> NORMAL
                R.id.type_fighting -> FIGHTING
                R.id.type_flying -> FLYING
                R.id.type_poison -> POISON
                R.id.type_ground -> GROUND
                R.id.type_rock -> ROCK
                R.id.type_bug -> BUG
                R.id.type_ghost -> GHOST
                R.id.type_fire -> FIRE
                R.id.type_water -> WATER
                R.id.type_grass -> GRASS
                R.id.type_electric -> ELECTRIC
                R.id.type_psychic -> PSYCHIC
                R.id.type_ice -> ICE
                R.id.type_dragon -> DRAGON
                R.id.type_fairy -> FAIRY
                R.id.type_steel -> STEEL
                else -> NONE
            }
        }
    }

}