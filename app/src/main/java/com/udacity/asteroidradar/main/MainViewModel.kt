package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber
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

    init {
      //  _navigateToDetailScreen.value = null
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroids()
            } catch (e: UnknownHostException) {
                Timber.e(e)
            }
        }
    }
}