package uk.co.jakebreen.pokecart.model.stat

enum class Stat(val stat: String) {
    HEALTH("hp"),
    ATTACK("attack"),
    DEFENSE("defense"),
    SPEED("speed");

    companion object {
        fun getStatByName(name: String): Stat? {
            return when (name) {
                HEALTH.stat -> HEALTH
                ATTACK.stat -> ATTACK
                DEFENSE.stat -> DEFENSE
                SPEED.stat -> SPEED
                else -> null
            }
        }
    }

}