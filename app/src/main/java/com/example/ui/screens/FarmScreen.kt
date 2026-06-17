package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FarmScreen() {
    val farmAnimals = listOf("Cattle (Cows, Bulls)", "Goats & Sheep", "Poultry (Chickens, Ducks)", "Swine (Pigs)", "Horses")

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Farm Animal Care") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                titleContentColor = MaterialTheme.colorScheme.onSecondary
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    "Livestock Management",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text("Specialized guidance for herd health and farm productivity.")
            }

            items(farmAnimals) { animal ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(animal, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Routine vaccination info, herd health monitoring, and specialized diet plans for $animal.")
                        Spacer(modifier = Modifier.height(8.dp))
                        TextButton(onClick = { }) {
                            Text("View Care Guide →")
                        }
                    }
                }
            }
        }
    }
}
