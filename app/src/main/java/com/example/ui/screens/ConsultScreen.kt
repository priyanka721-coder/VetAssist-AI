package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsultScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Consult Veterinarian") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("How would you like to connect?", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            ConsultOption("Live Video Call", "Best for visual assessment of wounds or movement.", Icons.Default.VideoCall)
            ConsultOption("Audio Call", "Quick follow-up or routine questions.", Icons.Default.Call)
            ConsultOption("Messaging", "Share reports and photos for non-urgent feedback.", Icons.Default.Chat)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Pre-screening", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Our AI chatbot will ask a few questions to gather symptoms before connecting you to the right specialist.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { /* Navigate to Chat */ }, modifier = Modifier.fillMaxWidth()) {
                        Text("Start Pre-screening")
                    }
                }
            }
        }
    }
}

@Composable
fun ConsultOption(title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = title, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
