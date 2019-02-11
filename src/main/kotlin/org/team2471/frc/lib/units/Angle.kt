package org.team2471.frc.lib.units

inline class Angle(val asRadians: Double) {
    operator fun plus(other: Angle) = Angle(asRadians + other.asRadians)

    operator fun minus(other: Angle) = Angle(asRadians - other.asRadians)

    operator fun times(factor: Double) = Angle(asRadians * factor)

    operator fun div(factor: Double) = Angle(asRadians / factor)

    operator fun rem(other: Angle) = Angle(asRadians % other.asRadians)

    operator fun unaryPlus() = this

    operator fun unaryMinus() = Angle(-asRadians)

    operator fun compareTo(other: Angle) = asRadians.compareTo(other.asRadians)

    override fun toString() = "$asRadians radians"

    fun sin() = Angle.sin(this)

    fun cos() = Angle.cos(this)

    fun tan() = Angle.tan(this)

    fun wrap() = Angle(Math.IEEEremainder(asRadians, 2 * Math.PI))

    companion object {
        @JvmStatic
        fun sin(angle: Angle) = Math.sin(angle.asRadians)

        @JvmStatic
        fun cos(angle: Angle) = Math.cos(angle.asRadians)

        @JvmStatic
        fun tan(angle: Angle) = Math.tan(angle.asRadians)

        @JvmStatic
        fun asin(value: Double) = Angle(Math.asin(value))

        @JvmStatic
        fun acos(value: Double) = Angle(Math.acos(value))

        @JvmStatic
        fun atan(value: Double) = Angle(Math.atan(value))

        @JvmStatic
        fun atan2(y: Double, x: Double) = Angle(Math.atan2(y, x))

        @JvmStatic
        fun atan2(y: Length, x: Length) = Angle(Math.atan2(y.asInches, x.asInches))
    }
}

// constructors
inline val Number.radians get() = Angle(this.toDouble())
inline val Number.degrees get() = Angle(Math.toRadians(this.toDouble()))

// destructors
inline val Angle.asDegrees get() = Math.toDegrees(asRadians)