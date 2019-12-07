package io.github.t3r1jj.pbmap.model.dictionary

import kotlin.math.roundToLong

enum class MeasurementSystem(val unit: String, private val coeff: Double) {
    SI("m", 1.0), US("yd", 1.0936);

    fun fromMeters(meters: Long): Long {
        return (meters * coeff).roundToLong()
    }
}