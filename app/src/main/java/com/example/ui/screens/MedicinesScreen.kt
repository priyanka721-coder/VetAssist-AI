package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinesScreen() {
    val medicines = listOf(
        MedicineInfo("Carprofen (Rimadyl)", "Pain and inflammation in dogs.", "1-2mg/lb twice daily. Consult vet."),
        MedicineInfo("Amoxicillin", "Bacterial infections.", "5-10mg/lb twice daily. Prescription needed."),
        MedicineInfo("Firocoxib", "Osteoarthritis pain.", "Targeted dosage for dogs and horses."),
        MedicineInfo("Doxycycline", "Tick-borne diseases.", "Prescribed for specific bacterial types.")
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Veterinary Medicines") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search disease or symptoms...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                )
            }

            items(medicines) { med ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(med.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Uses: ${med.use}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Guidance: ${med.guidance}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("⚠️ Disclaimer", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                        Text("Consult a licensed veterinarian before administering any medication. Never prescribe medication dosages yourself.")
                    }
                }
            }
        }
    }
}

data class MedicineInfo(val name: String, val use: String, val guidance: String)
