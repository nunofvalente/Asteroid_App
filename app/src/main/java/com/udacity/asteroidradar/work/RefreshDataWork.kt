package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWork(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object {
        const val WORK_NAME = "RefreshAsteroidWork"
    }

    override suspend fun doWork(): Result {
        val databaseAsteroid = getDatabase(applicationContext)
        val repository = AsteroidRepository(databaseAsteroid)

        return try {
            repository.refreshAsteroids()
            return Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}