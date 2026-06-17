package com.example.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HealthInfo(val score: Int, val suggestedVaccines: List<String>, val suggestions: List<String>)

class HealthViewModel : ViewModel() {
    private val _healthInfo = MutableStateFlow<Map<Int, HealthInfo>>(emptyMap())
    val healthInfo: StateFlow<Map<Int, HealthInfo>> = _healthInfo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun analyzeHealth(pet: Pet) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                val prompt = """
                    Analyze the health of this animal:
                    Name: ${pet.name}
                    Species: ${pet.species}
                    Breed: ${pet.breed ?: "Unknown"}
                    Age: ${pet.age ?: "Unknown"} months/years
                    Weight: ${pet.weight ?: "Unknown"} kg
                    
                    Provide the response in this EXACT format:
                    SCORE: [0-100]
                    VACCINES: [Comma separated list]
                    SUGGESTIONS: [Semicolon separated list]
                """.trimIndent()

                val request = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = prompt))))
                )

                val response = RetrofitClient.service.generateContent(apiKey, request)
                val text = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                
                val score = text.lineSequence().find { it.startsWith("SCORE:") }?.removePrefix("SCORE:")?.trim()?.toIntOrNull() ?: 50
                val vaccines = text.lineSequence().find { it.startsWith("VACCINES:") }?.removePrefix("VACCINES:")?.trim()?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
                val suggestions = text.lineSequence().find { it.startsWith("SUGGESTIONS:") }?.removePrefix("SUGGESTIONS:")?.trim()?.split(";")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()
                
                val newMap = _healthInfo.value.toMutableMap()
                newMap[pet.id] = HealthInfo(score, vaccines, suggestions)
                _healthInfo.value = newMap
            } catch (e: Exception) {
                // Ignore error for now in UI
            } finally {
                _isLoading.value = false
            }
        }
    }
}
