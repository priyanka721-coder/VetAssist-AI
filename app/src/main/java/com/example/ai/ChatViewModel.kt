package com.example.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class Message(val text: String, val isUser: Boolean, val imageBase64: String? = null)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(listOf(
        Message("Hello! I'm VetAssist AI. How can I help you and your animal friends today?", false)
    ))
    val messages: StateFlow<List<Message>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(text: String, imageBase64: String? = null) {
        if (text.isBlank() && imageBase64 == null) return
        
        _messages.value = _messages.value + Message(text, true, imageBase64)
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val apiKey = com.example.BuildConfig.GEMINI_API_KEY
                val systemPrompt = """
                    You are VetAssist AI, an expert veterinary assistant.
                    
                    CRITICAL CAPABILITIES:
                    1. SYMPTOM CHECKER: Analyze symptoms users describe. Categorize urgency as LOW, MEDIUM, HIGH, or CRITICAL.
                    2. DISEASE DETECTION & BREED RECOGNITION: If an image is provided, identify the animal's breed and analyze any visible conditions (wounds, skin issues).
                    3. LIVESTOCK & PETS: Support dogs, cats, cows, goats, horses, rabbits, and poultry.
                    4. NUTRITION: Provide age and weight specific diet plans.
                    5. EMERGENCY: For breathing issues, seizures, or heavy bleeding, advise IMMEDIATE veterinary care.
                    6. VET RECOMMENDATION: If users search for vets, recommend finding local specialists based on animal type.
                    7. MEDICINE INFO: Provide general medicine guidance, side effects, and precautions.
                    
                    CONSTRAINTS:
                    - NEVER prescribe specific medication dosages.
                    - ALWAYS include: "Disclaimer: Consult a licensed veterinarian before administering medication or treatment."
                    - Be professional, empathetic, and support multiple languages (English, Hindi, Marathi).
                """.trimIndent()

                val contents = _messages.value.map { msg ->
                    val parts = mutableListOf<Part>()
                    if (msg.text.isNotBlank()) parts.add(Part(text = msg.text))
                    if (msg.imageBase64 != null) {
                        parts.add(Part(inlineData = InlineData(mimeType = "image/jpeg", data = msg.imageBase64)))
                    }
                    Content(parts = parts)
                }

                val request = GenerateContentRequest(
                    contents = contents,
                    systemInstruction = Content(parts = listOf(Part(text = systemPrompt)))
                )

                val response = RetrofitClient.service.generateContent(apiKey, request)
                val aiResponse = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "I'm sorry, I couldn't process that request."
                
                _messages.value = _messages.value + Message(aiResponse, false)
            } catch (e: Exception) {
                _messages.value = _messages.value + Message("I'm having trouble connecting right now. Please try again later. Error: ${e.message}", false)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
