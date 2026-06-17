package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.Pet
import com.example.data.PetViewModel

import com.example.ai.HealthViewModel
import androidx.compose.animation.AnimatedVisibility

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(
    petViewModel: PetViewModel = viewModel(),
    healthViewModel: HealthViewModel = viewModel()
) {
    val pets by petViewModel.pets.collectAsState()
    val healthInfo by healthViewModel.healthInfo.collectAsState()
    val isLoadingHealth by healthViewModel.isLoading.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Pet")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Text(
                "Health Records",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("No pets added yet.", style = MaterialTheme.typography.bodyLarge)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pets) { pet ->
                    PetCard(
                        pet = pet,
                        onDelete = { petViewModel.deletePet(pet) },
                        healthInfo = healthInfo[pet.id],
                        onAnalyze = { healthViewModel.analyzeHealth(pet) },
                        isAnalyzing = isLoadingHealth
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        AddPetDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, species, breed, age, weight ->
                petViewModel.addPet(Pet(
                    name = name, 
                    species = species, 
                    breed = breed,
                    age = age,
                    weight = weight
                ))
                showAddDialog = false
            }
        )
    }
}

@Composable
fun PetCard(
    pet: Pet, 
    onDelete: () -> Unit, 
    healthInfo: com.example.ai.HealthInfo?,
    onAnalyze: () -> Unit,
    isAnalyzing: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(pet.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${pet.species} • ${pet.breed ?: "Unknown Breed"}", style = MaterialTheme.typography.bodyMedium)
                }
                Row {
                    if (healthInfo == null) {
                        IconButton(onClick = onAnalyze, enabled = !isAnalyzing) {
                            Icon(Icons.Default.Insights, contentDescription = "AI Analyze", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
            
            if (healthInfo != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Health Score: ", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${healthInfo.score}/100", 
                        style = MaterialTheme.typography.titleMedium, 
                        fontWeight = FontWeight.Bold,
                        color = if (healthInfo.score > 70) Color(0xFF4CAF50) else if (healthInfo.score > 40) Color(0xFFFF9800) else Color(0xFFF44336)
                    )
                }
            }

            AnimatedVisibility(visible = expanded && healthInfo != null) {
                Column {
                    if (healthInfo?.suggestedVaccines?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("💉 Suggested Vaccines:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        healthInfo.suggestedVaccines.forEach { vaccine ->
                            Text("• $vaccine", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    if (healthInfo?.suggestions?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("💡 AI Advice:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        healthInfo.suggestions.forEach { suggestion ->
                            Text("• $suggestion", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDialog(onDismiss: () -> Unit, onConfirm: (String, String, String, Int?, Float?) -> Unit) {
    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Pet") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                OutlinedTextField(value = species, onValueChange = { species = it }, label = { Text("Species (e.g. Dog, Cat, Cow)") })
                OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Breed (Optional)") })
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight") }, modifier = Modifier.weight(1f))
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                if (name.isNotBlank()) {
                    onConfirm(name, species, breed, age.toIntOrNull(), weight.toFloatOrNull())
                }
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
