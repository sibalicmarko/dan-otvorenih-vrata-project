package sibalic.mathapp.screens.quiz

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import sibalic.mathapp.data.QuestionRepository
import sibalic.mathapp.model.Pitanje

class QuizViewModel (
    private val repository: QuestionRepository,
    private val topicName: String
) : ViewModel() {
    var questions by mutableStateOf<List<Pitanje>>(emptyList())
    var currentIndex by mutableStateOf(0)

    var selectedAnswers = mutableStateMapOf<Int, Int>()

    init {
        loadSpecificQuiz()
    }

    private fun loadSpecificQuiz() {
        val easy = repository.getRandomQuestions(topicName, "Lako", 5)
        val medium = repository.getRandomQuestions(topicName, "Srednje", 4)
        val hard = repository.getRandomQuestions(topicName, "Teško", 1)

        questions = (easy + medium + hard).shuffled()
    }

    fun selectAnswer(questionIndex: Int, answerIndex: Int) {
        selectedAnswers[questionIndex] = answerIndex
    }

    fun calculateScore(): Int {
        var score = 0

        questions.forEachIndexed { index, pitanje ->
            if (selectedAnswers[index] == pitanje.tocan_odgovor) { score++ }
        }
        return score
    }
}