package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.theme.VetAssistTheme
import com.example.ui.screens.*

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      VetAssistTheme {
        MainScreen()
      }
    }
  }
}

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
  object Home : Screen("home", "Home", Icons.Default.Home)
  object Food : Screen("food", "Nutrition", Icons.Default.Restaurant)
  object Chat : Screen("chat", "AI Chat", Icons.Default.Chat)
  object Records : Screen("records", "Records", Icons.Default.Pets)
  object Emergency : Screen("emergency", "Emergency", Icons.Default.Warning)
  object Consult : Screen("consult", "Consult", Icons.Default.VideoCall)
  object Medicines : Screen("medicines", "Meds", Icons.Default.MedicalServices)
  object Farm : Screen("farm", "Farm", Icons.Default.Agriculture)
}

@Composable
fun MainScreen() {
  val navController = rememberNavController()
  val navItems = listOf(
    Screen.Home,
    Screen.Food,
    Screen.Chat,
    Screen.Records,
    Screen.Emergency
  )

  Scaffold(
    bottomBar = {
      NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        navItems.forEach { screen ->
          NavigationBarItem(
            icon = { Icon(screen.icon, contentDescription = screen.label) },
            label = { Text(screen.label) },
            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
            onClick = {
              navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                  saveState = true
                }
                launchSingleTop = true
                restoreState = true
              }
            }
          )
        }
      }
    }
  ) { innerPadding ->
    NavHost(
      navController = navController,
      startDestination = Screen.Home.route,
      modifier = Modifier.padding(innerPadding)
    ) {
      composable(Screen.Home.route) { 
          HomeScreen(onCategoryClick = { route -> navController.navigate(route) }) 
      }
      composable(Screen.Food.route) { FoodScreen() }
      composable(Screen.Chat.route) { ChatScreen() }
      composable(Screen.Records.route) { RecordsScreen() }
      composable(Screen.Emergency.route) { EmergencyScreen() }
      composable(Screen.Consult.route) { ConsultScreen() }
      composable(Screen.Medicines.route) { MedicinesScreen() }
      composable(Screen.Farm.route) { FarmScreen() }
    }
  }
}
