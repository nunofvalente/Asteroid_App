package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.DatabaseAsteroid

data class AsteroidContainer(val asteroid: List<Asteroid>)

fun AsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroid.map { DatabaseAsteroid (
        id = it.id,
        codename = it.codename,
        closeApproachDate = it.closeApproachDate,
        absoluteMagnitude = it.absoluteMagnitude,
        estimatedDiameter = it.estimatedDiameter,
        relativeVelocity = it.relativeVelocity,
        distanceFromEarth = it.distanceFromEarth,
        isPotentiallyHazardous = it.isPotentiallyHazardous
            )}.toTypedArray()
}