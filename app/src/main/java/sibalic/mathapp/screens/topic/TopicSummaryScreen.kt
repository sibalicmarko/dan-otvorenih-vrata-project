package sibalic.mathapp.screens.topic

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sibalic.mathapp.ui.theme.*
import sibalic.mathapp.components.MathText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicSummaryScreen(
    topicTitle: String,
    uvodTekst: String,
    formule: List<String>,
    tips: List<String>,
    onBackClick: () -> Unit,
    onStartQuizClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topicTitle.uppercase(),
                        color = MathNeutral,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Natrag", tint = MathNeutral)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AppBackground)
            )
        },
        containerColor = AppBackground,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            ) {
                Button(
                    onClick = onStartQuizClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MathPrimary),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("ZAPOČNI KVIZ", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Uvod",
                color = MathNeutral,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            MathText(
                text = uvodTekst,
                textColor = MathNeutral.copy(alpha = 0.8f),
                textSizeSp = 15f
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (formule.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MathPrimary.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, MathPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Formule", color = MathPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(16.dp))

                        formule.forEach { formula ->
                            MathText(
                                text = "•  $formula",
                                textColor = MathNeutral,
                                textSizeSp = 15f,
                                modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            if (tips.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MathTertiary.copy(alpha = 0.1f),
                    border = BorderStroke(1.dp, MathTertiary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Lightbulb, contentDescription = "Tip", tint = MathTertiary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Trikovi i savjeti", color = MathTertiary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        tips.forEach { tip ->
                            MathText(
                                text = "•  $tip",
                                textColor = MathNeutral,
                                textSizeSp = 15f,
                                modifier = Modifier.padding(bottom = 12.dp, start = 4.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}