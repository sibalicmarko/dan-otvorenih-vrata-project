package sibalic.mathapp.model

import android.R
import kotlinx.serialization.Serializable

@Serializable
data class QuizData(
    val kategorije: List<Kategorija>
)

@Serializable
data class Kategorija(
    val naziv: String,
    val uvod: Uvod,
    val pitanja: List<Pitanje>
)

@Serializable
data class Uvod(
    val tekst: String,
    val formule: List<String>,
    val tips: List<String>
)

@Serializable
data class Pitanje(
    val tezina: String,
    val slika: String? = null,
    val tekst: String,
    val odgovori: List<String>,
    val tocan_odgovor: Int
)