package sibalic.mathapp.screens.quiz

import android.R
import android.view.RoundedCorner
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sibalic.mathapp.components.MathText
import sibalic.mathapp.ui.theme.AppBackground
import sibalic.mathapp.ui.theme.MathNeutral
import sibalic.mathapp.ui.theme.MathPrimary
import sibalic.mathapp.ui.theme.MathQuaternary
import sibalic.mathapp.ui.theme.MathSecondary
import sibalic.mathapp.ui.theme.MathTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    onFinish: (Int, Int) -> Unit,
    onBackExit: () -> Unit
) {
    val currentQuestion = viewModel.questions.getOrNull(viewModel.currentIndex)
    val totalQuestions = viewModel.questions.size

    val progressTarget = if (totalQuestions > 0) {
        (viewModel.currentIndex + 1).toFloat() / totalQuestions.toFloat()
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 300),
        label = "QuizProgressAnimation"
    )

    Scaffold(
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackExit) {
                        Icon(Icons.Default.Close, contentDescription = "Izađi", tint = MathNeutral)
                    }

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp)
                            .height(10.dp),
                        color = MathPrimary,
                        trackColor = MathPrimary.copy(alpha = 0.15f),
                        strokeCap = StrokeCap.Round
                    )

                    Text(
                        text = "${viewModel.currentIndex + 1}/$totalQuestions",
                        color = MathNeutral.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        },
        containerColor = AppBackground,
        bottomBar = {
            Surface(
                color = Color.White,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp).navigationBarsPadding(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (viewModel.currentIndex > 0) {
                        TextButton(
                            onClick = { viewModel.currentIndex-- },
                            colors = ButtonDefaults.textButtonColors(contentColor = MathNeutral)
                        ) {
                            Text("NATRAG", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            if (viewModel.currentIndex < totalQuestions - 1) {
                                viewModel.currentIndex++
                            } else {
                                onFinish(viewModel.calculateScore(), totalQuestions)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MathPrimary),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = if (viewModel.currentIndex < totalQuestions - 1) "DALJE" else "ZAVRŠI",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (currentQuestion != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                DifficultyTag(tezina = currentQuestion.tezina)

                Spacer(modifier = Modifier.height(12.dp))

                MathText(
                    text = "**${currentQuestion.tekst}**",
                    textSizeSp = 19f,
                    textColor = MathNeutral
                )

                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier.fillMaxWidth().selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    currentQuestion.odgovori.forEachIndexed { index, odgovor ->
                        val isSelected = viewModel.selectedAnswers[viewModel.currentIndex] == index

                        AnswerOption(
                            text = odgovor,
                            isSelected = isSelected,
                            onSelect = { viewModel.selectAnswer(viewModel.currentIndex, index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DifficultyTag(tezina: String) {
    val (backgroundColor, textColor) = when (tezina.lowercase()) {
        "lako" -> MathPrimary.copy(alpha = 0.15f) to MathPrimary
        "srednje" -> MathTertiary.copy(alpha = 0.15f) to MathTertiary
        "teško", "tesko" -> MathQuaternary.copy(alpha = 0.15f) to MathQuaternary
        else -> MathNeutral.copy(alpha = 0.1f) to MathNeutral
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, textColor.copy(alpha = 0.5f))
    ) {
        Text(
            text = tezina.uppercase(),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AnswerOption(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onSelect,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MathPrimary.copy(alpha = 0.1f) else Color.White,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MathPrimary else Color(0xFFE5E5E5)
        ),
        tonalElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = MathPrimary,
                    unselectedColor = MathNeutral.copy(alpha = 0.4f)
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            MathText(
                text = text,
                textSizeSp = 16f,
                textColor = if (isSelected) MathPrimary else MathNeutral
            )
        }
    }
}