package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    val asteroidList = asteroidRepository.asteroids

    private val _navigateToDetailScreen = MutableLiveData<Asteroid>()

    val navigateToDetailScreen: LiveData<Asteroid>
        get() = _navigateToDetailScreen

    fun navigationDone() {
        _navigateToDetailScreen.value = null
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToDetailScreen.value = asteroid
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private fun getPictureDay() {
        viewModelScope.launch {
            try {
                val pictureOfDay =
                    AsteroidApi.retrofitServiceMoshi.getImageOfDay(BuildConfig.ApiKey)
                if (pictureOfDay.mediaType == "image") {
                    _pictureOfDay.value = pictureOfDay
                } else {
                    _pictureOfDay.value = PictureOfDay("", "", "")
                }
            }catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    init {
        _navigateToDetailScreen.value = null
        getPictureDay()
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
            } catch (e: UnknownHostException) {
                Timber.e(e)
            }
        }
    }
}