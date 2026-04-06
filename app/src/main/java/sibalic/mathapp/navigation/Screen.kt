package sibalic.mathapp.navigation

sealed class Screen(val route: String) {
    object Splash: Screen("splash_screen")

    object Home: Screen("home_screen")

    object Topic: Screen("topic_screen/{topicName}") {
        fun createRoute(topicName: String) = "topic_screen/$topicName"
    }

    object Quiz: Screen("quiz_screen/{topicName}") {
        fun createRoute(topicName: String) = "quiz_screen/$topicName"
    }

    object Result : Screen("result_screen/{topicName}/{score}/{total}") {
        fun createRoute(topicName: String, score: Int, total: Int) = "result_screen/$topicName/$score/$total"
    }
}