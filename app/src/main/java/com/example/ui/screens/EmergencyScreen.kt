package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyScreen() {
    val emergencies = listOf(
        EmergencyInfo("Poisoning", "Induce vomiting if safe, call vet immediately. Keep the substance packaging.", Color(0xFFF44336)),
        EmergencyInfo("Injury / Bleeding", "Apply firm pressure with clean cloth. Keep the animal calm and still.", Color(0xFFFF5722)),
        EmergencyInfo("Breathing Issues", "Ensure airway is clear. Do not muzzle. Seek urgent care.", Color(0xFFE91E63)),
        EmergencyInfo("High Fever", "Cool down with damp towels (not ice). Monitor temperature.", Color(0xFFFF9800)),
        EmergencyInfo("Seizures", "Clear the area of hazards. Do not touch their mouth. Note the duration.", Color(0xFF9C27B0))
    )

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("🚨 Emergency Help") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFB71C1C),
                titleContentColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Emergency Call", tint = Color(0xFFB71C1C))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Local Emergency Vet", fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
                            Text("Call: 1-800-VET-HELP", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }

            items(emergencies) { emergency ->
                EmergencyItem(emergency)
            }
        }
    }
}

data class EmergencyInfo(val title: String, val action: String, val color: Color)

@Composable
fun EmergencyItem(info: EmergencyInfo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(info.color, RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(info.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(info.action, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
