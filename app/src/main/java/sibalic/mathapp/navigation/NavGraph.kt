package sibalic.mathapp.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import sibalic.mathapp.data.QuestionRepository
import sibalic.mathapp.model.Kategorija
import sibalic.mathapp.model.QuizData
import sibalic.mathapp.screens.home.HomeScreen
import sibalic.mathapp.screens.quiz.QuizScreen
import sibalic.mathapp.screens.quiz.QuizViewModel
import sibalic.mathapp.screens.results.ResultScreen
import sibalic.mathapp.screens.splash.SplashScreen
import sibalic.mathapp.screens.topic.TopicSummaryScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val repository = remember { QuestionRepository(context) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(route = Screen.Splash.route) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }

        composable(route = Screen.Home.route) {
            HomeScreen(
                onTopicClick = { topicName ->
                    navController.navigate(Screen.Topic.createRoute(topicName))
                }
            )
        }

        composable(
            route = Screen.Topic.route,
            arguments = listOf(navArgument("topicName") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicName = backStackEntry.arguments?.getString("topicName") ?: ""
            val quizData: QuizData? = repository.loadQuestions()
            val trazenaLekcija: Kategorija? = quizData?.kategorije?.find { it.naziv == topicName }

            TopicSummaryScreen(
                topicTitle = trazenaLekcija?.naziv ?: topicName,
                uvodTekst = trazenaLekcija?.uvod?.tekst ?: "Podaci nisu pronađeni. Provjeri poklapaju li se imena!",
                formule = trazenaLekcija?.uvod?.formule ?: emptyList(),
                tips = trazenaLekcija?.uvod?.tips ?: emptyList(),
                onBackClick = {
                    navController.popBackStack()
                },
                onStartQuizClick = {
                    navController.navigate(Screen.Quiz.createRoute(topicName))
                }
            )
        }

        composable(
            route = Screen.Quiz.route,
            arguments = listOf(navArgument("topicName") { type = NavType.StringType })
        ) { backStackEntry ->
            val topicName = backStackEntry.arguments?.getString("topicName") ?: ""
            val viewModel = remember { QuizViewModel(repository, topicName) }

            QuizScreen(
                viewModel = viewModel,
                onFinish = { score, total ->
                    navController.navigate(Screen.Result.createRoute(topicName, score, total)) {
                        popUpTo(Screen.Home.route)
                    }
                },
                onBackExit = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val topicName = backStackEntry.arguments?.getString("topicName") ?: ""
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 10

            ResultScreen(
                topicName = topicName,
                score = score,
                total = total,
                onReturnHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onTryAgain = {
                    navController.navigate(Screen.Quiz.createRoute(topicName)) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }
    }
}