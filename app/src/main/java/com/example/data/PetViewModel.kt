package com.example.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "vet-db"
    ).build()

    private val petDao = db.petDao()

    val pets: StateFlow<List<Pet>> = petDao.getAllPets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addPet(pet: Pet) {
        viewModelScope.launch {
            petDao.insertPet(pet)
        }
    }

    fun deletePet(pet: Pet) {
        viewModelScope.launch {
            petDao.deletePet(pet)
        }
    }
}
