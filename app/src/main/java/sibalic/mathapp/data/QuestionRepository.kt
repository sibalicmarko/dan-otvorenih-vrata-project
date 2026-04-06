package sibalic.mathapp.data

import android.content.Context
import kotlinx.serialization.json.Json
import sibalic.mathapp.model.Pitanje
import sibalic.mathapp.model.QuizData

class QuestionRepository(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }

    fun loadQuestions(): QuizData? {
        return try {
            val jsonString = context.assets.open("questions.json")
                .bufferedReader()
                .use { it.readText() }

            json.decodeFromString<QuizData>(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getRandomQuestions(tema: String, tezina: String, brojPitanja: Int): List<Pitanje> {
        val data = loadQuestions() ?: return emptyList()
        val kategorija = data.kategorije.find { it.naziv == tema }

        return kategorija?.pitanja
            ?.filter { it.tezina.equals(tezina, ignoreCase = true) }
            ?.shuffled()
            ?.take(brojPitanja)
            ?: emptyList()
    }
}