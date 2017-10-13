package org.team2471.frc.lib.control

import org.team2471.frc.lib.math.Circle
import org.team2471.frc.lib.math.Line2D
import org.team2471.frc.lib.math.Point2D
import org.team2471.frc.lib.math.Vector2D
import java.lang.Math.toDegrees

data class Waypoint(val point: Point2D,
                    val radialTolerance: Double,
                    val maxSpeed: Double = Double.MAX_VALUE,
                    val name: String? = null)

// see: http://www.dtic.mil/get-tr-doc/pdf?AD=ADA599492
class PurePursuitController(private val lookahead: Double,
                            private var position: Point2D,
                            private var heading: Double,
                            vararg waypoints: Waypoint) {
    var finished = false
        private set

    private val waypoints = listOf(Waypoint(position, Double.MAX_VALUE), *waypoints)
    var waypointsPassed = 0
        private set

    private val lastWaypoint get() = waypoints[waypointsPassed]
    val currentWaypoint get() = waypoints[waypointsPassed + 1]

    private var lookaheadPoint: Point2D = currentWaypoint.point

    private val error: Double
        get() = (heading - toDegrees(position.vectorTo(lookaheadPoint).angle)) % 360

    fun getCurvature(deltaVector: Vector2D): Double {
        // update robot pose
        position += deltaVector
        heading += toDegrees(deltaVector.angle)

        // check if current waypoint reached
        if (position.distance(currentWaypoint.point) <= currentWaypoint.radialTolerance) {
            if (waypointsPassed + 1 < waypoints.size) waypointsPassed++
            else finished = true
        }

        // update lookahead point
        val waypointLine = Line2D(lastWaypoint.point, currentWaypoint.point)
        val lookaheadPoints = Circle(position, lookahead)
                .intersectingPoints(waypointLine)
                .map { point ->
                    // limit the points to be between the waypoints. this is necessary because Line2Ds are infinite.
                    if (!waypointLine.pointInSegment(point)) point.closestPoint(waypointLine.pointA, waypointLine.pointB)
                    else point
                }

        lookaheadPoint = when (lookaheadPoints.size) {
            1 -> lookaheadPoints[0]
            2 -> if (lookaheadPoints[0].distance(currentWaypoint.point) < lookaheadPoints[1].distance(currentWaypoint.point))
                lookaheadPoints[0] else lookaheadPoints[1]
            else -> lookaheadPoint // this should only happen if there is no intersecting points. there should never be more than 2 points.
        }

        return (2 * error) / lookahead
    }
}
