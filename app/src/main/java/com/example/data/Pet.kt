package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val species: String,
    val breed: String? = null,
    val age: Int? = null,
    val weight: Float? = null,
    val vaccinationDate: Long? = null
)
