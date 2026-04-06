package sibalic.mathapp.screens.results

import android.R
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sibalic.mathapp.ui.theme.AppBackground
import sibalic.mathapp.ui.theme.MathNeutral
import sibalic.mathapp.ui.theme.MathPrimary
import androidx.core.content.edit
import sibalic.mathapp.ui.theme.MathQuaternary
import sibalic.mathapp.ui.theme.MathTertiary

@Composable
fun ResultScreen(
    topicName: String,
    score: Int,
    total: Int,
    onReturnHome: () -> Unit,
    onTryAgain: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("MathAppPrefs", Context.MODE_PRIVATE) }

    val bestScoreKey = "best_score_$topicName"

    var bestScore by remember { mutableStateOf(prefs.getInt(bestScoreKey, 0)) }
    val isNewBest = score > bestScore

    LaunchedEffect(score) {
        if (isNewBest) {
            prefs.edit { putInt(bestScoreKey, score) }
            bestScore = score
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(AppBackground).padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("KVIZ ZAVRŠEN!", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = MathNeutral)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Tvoj rezultat je:", fontSize = 18.sp, color = MathNeutral.copy(alpha = 0.7f))

        Text("$score / $total", fontSize = 64.sp, fontWeight = FontWeight.Black, color = MathPrimary)

        Spacer(modifier = Modifier.height(8.dp))

        if (isNewBest) {
            Text("NOVI NAJBOLJI REZULTAT!", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MathQuaternary)
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onTryAgain,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MathTertiary)
        ) {
            Text("POKUŠAJ PONOVNO", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onReturnHome,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MathQuaternary)
        ) {
            Text("POVRATAK NA POČETNU", fontWeight = FontWeight.Bold)
        }
    }
}