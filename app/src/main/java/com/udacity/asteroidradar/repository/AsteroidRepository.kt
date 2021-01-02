package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    private val weekDays = getNextSevenDaysFormattedDates()
    private val startDate = weekDays[0]

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.dao.getAsteroidList()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidsJson = AsteroidApi.retrofitService.getAsteroidsJson(startDate, "", BuildConfig.ApiKey)
            val asteroidList = parseAsteroidsJsonResult(JSONObject(asteroidsJson))

            val asteroidContainer = AsteroidContainer(asteroidList)

            database.dao.insertAll(*asteroidContainer.asDatabaseModel())
        }
    }
}