package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class Category(
    val name: String,
    val subText: String,
    val iconEmoji: String,
    val bgColor: Color,
    val textColor: Color,
    val accentColor: Color,
    val route: String
)

@Composable
fun HomeScreen(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        Category("Pet Care", "Dogs, Cats, Rabbits", "🐕", CardPetCare, VibrantText, VibrantPrimary, "records"),
        Category("Farm Care", "Cattle, Poultry, Goats", "🐄", CardFarmCare, VibrantText, Color(0xFF4F5B92), "farm"),
        Category("Medicines", "Guide & Dosages", "💊", CardMedicines, VibrantText, VibrantSecondary, "medicines"),
        Category("Nutrition", "Diet Planners", "🥩", CardNutrition, VibrantText, VibrantSecondary, "food"),
        Category("Consult Vet", "Chat or Video Call", "👨⚕️", CardConsult, VibrantText, Color(0xFF005FAF), "consult"),
        Category("Records", "Vaccines & History", "📖", CardRecords, VibrantText, Color(0xFF825500), "records")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(VibrantBackgroundStart, VibrantBackgroundEnd)
                )
            )
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "VetAssist AI",
                    style = MaterialTheme.typography.headlineMedium,
                    color = VibrantPrimary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Text(
                    text = "Expert care for every creature",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF5C635E),
                    fontWeight = FontWeight.Medium
                )
            }
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VibrantPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text("JS", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        // Search Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceSearch)
                .border(1.dp, SurfaceSearchBorder, RoundedCornerShape(16.dp))
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = VibrantPrimary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Symptoms, medicines, or diet...", color = Color(0xFF5C635E), style = MaterialTheme.typography.bodyMedium)
            }
        }

        // Categories Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(categories) { category ->
                VibrantCategoryCard(category) { onCategoryClick(category.route) }
            }
        }

        // AI Banner
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { onCategoryClick("chat") },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = VibrantPrimary)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🤖", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "VETASSIST AI",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            "Analyze symptoms instantly",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }

        // Emergency Button
        Button(
            onClick = { onCategoryClick("emergency") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = VibrantSecondary)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🚨", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "EMERGENCY CARE HELP",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun VibrantCategoryCard(category: Category, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() }
            .border(1.dp, Color.White.copy(alpha = 0.4f), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = category.bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(category.iconEmoji, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = category.textColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = category.subText,
                    style = MaterialTheme.typography.labelSmall,
                    color = category.textColor.copy(alpha = 0.7f),
                    lineHeight = 12.sp
                )
            }
            Text(
                "Open →",
                style = MaterialTheme.typography.labelSmall,
                color = category.accentColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}
