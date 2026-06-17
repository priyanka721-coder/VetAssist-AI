package com.example.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    private val _dietPlan = MutableStateFlow<String?>(null)
    val dietPlan: StateFlow<String?> = _dietPlan.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun generateDietPlan(species: String, breed: String, age: String, weight: String, activity: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                val prompt = """
                    As a veterinary nutritionist, create a personalized diet plan for:
                    Species: $species
                    Breed: $breed
                    Age: $age
                    Weight: $weight
                    Activity Level: $activity
                    
                    Please provide:
                    1. Daily calorie requirements (Estimated)
                    2. Recommended macro ratios (Protein, Fat, Fiber)
                    3. Specific food type recommendations
                    4. Feeding schedule
                    5. Important dietary precautions
                    
                    Include a disclaimer that this is an AI estimate and to consult a vet.
                """.trimIndent()

                val request = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = prompt))))
                )

                val response = RetrofitClient.service.generateContent(apiKey, request)
                _dietPlan.value = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "Could not generate plan."
            } catch (e: Exception) {
                _dietPlan.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearPlan() {
        _dietPlan.value = null
    }
}
