package org.sert2521.powerup.util

object Vision {
    var alive: Boolean? = false
    var found: Boolean? = false
    var time: Long? = 0
    var xOffset: Int? = null
    var yOffset: Int? = null
}

data class VisionData(val alive: Boolean? = null,
                      val found: Boolean? = null,
                      val time: Long? = null,
                      val xOffset: Int? = null,
                      val yOffset: Int? = null)
