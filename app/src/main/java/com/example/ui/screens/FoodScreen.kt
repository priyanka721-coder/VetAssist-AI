package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.FoodViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodScreen(foodViewModel: FoodViewModel = viewModel()) {
    val animals = listOf("Dog", "Cat", "Cow", "Goat", "Horse", "Poultry", "Rabbit")
    var selectedAnimal by remember { mutableStateOf<String?>(null) }
    var showAiPlanner by remember { mutableStateOf(false) }
    val dietPlan by foodViewModel.dietPlan.collectAsState()
    val isLoading by foodViewModel.isLoading.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(if (showAiPlanner) "AI Diet Planner" else "Nutrition & Diet") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            navigationIcon = {
                if (showAiPlanner || selectedAnimal != null) {
                    IconButton(onClick = { 
                        if (showAiPlanner) {
                            showAiPlanner = false 
                            foodViewModel.clearPlan()
                        } else {
                            selectedAnimal = null 
                        }
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        )

        if (showAiPlanner) {
            AiDietPlannerForm(foodViewModel, dietPlan, isLoading)
        } else if (selectedAnimal == null) {
            Column {
                Card(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().clickable { showAiPlanner = true },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("🤖", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("AI Personalized Planner", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text("Get a plan based on breed, age & weight", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                Text(
                    "General Guides",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(animals) { animal ->
                        ListItem(
                            headlineContent = { Text(animal) },
                            modifier = Modifier.clickable { selectedAnimal = animal },
                            trailingContent = { Icon(Icons.Default.Info, contentDescription = "Info") }
                        )
                        HorizontalDivider()
                    }
                }
            }
        } else {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text(
                    selectedAnimal!!,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                DietPlanSection("Puppy / Junior", "High protein, frequent meals (3-4 times/day). Focus on calcium for bone growth.")
                DietPlanSection("Adult", "Balanced protein and fat. Maintain ideal weight. 2 meals/day.")
                DietPlanSection("Senior", "Reduced calories, high fiber. Joint support supplements recommended.")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("💡 AI Tip", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Always provide fresh water and avoid sudden diet changes to prevent digestive upset.")
                    }
                }
            }
        }
    }
}

@Composable
fun AiDietPlannerForm(viewModel: FoodViewModel, dietPlan: String?, isLoading: Boolean) {
    var species by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }

    if (dietPlan != null) {
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
            Text("Your AI Diet Plan", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(dietPlan)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.clearPlan() }, modifier = Modifier.fillMaxWidth()) {
                Text("Start New Plan")
            }
        }
    } else {
        Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = species, onValueChange = { species = it }, label = { Text("Species (e.g. Dog, Cat, Cow)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Breed") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = activity, onValueChange = { activity = it }, label = { Text("Activity Level (Low/Med/High)") }, modifier = Modifier.fillMaxWidth())
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { viewModel.generateDietPlan(species, breed, age, weight, activity) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && species.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Generate Plan with AI")
                }
            }
        }
    }
}

@Composable
fun DietPlanSection(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text(description, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }
}
