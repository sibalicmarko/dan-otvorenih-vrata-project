package sibalic.mathapp.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sibalic.mathapp.R
import sibalic.mathapp.ui.theme.*
import androidx.compose.ui.graphics.lerp

data class TopicItem(
    val title: String,
    val jsonName: String,
    val iconRes: Int,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTopicClick: (String) -> Unit
) {
    val topics = listOf(
        TopicItem("OSNOVE MATEMATIKE", "Osnove matematike", R.drawable.basics, MathBasics),
        TopicItem("KVADRATNA FUNKCIJA", "Kvadratna funkcija i nejednadžbe", R.drawable.quadrate, MathPrimary),
        TopicItem("KVADRATNA JEDNADŽBA", "Kvadratna jednadžba", R.drawable.algebra, MathSecondary),
        TopicItem("KRUG I KRUŽNICA", "Krug i kružnica", R.drawable.radius, MathTertiary),
        TopicItem("TRIGONOMETRIJA", "Trigonometrija u planimetriji", R.drawable.sine, MathQuaternary),
        TopicItem("TIJELA", "Tijela", R.drawable.shapes, MathNeutral)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MATHMAGIC",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MathPrimary)
            )
        },
        containerColor = AppBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .drawBehind {
                    val startY = 85.dp.toPx()
                    val endY = size.height - 120.dp.toPx()

                    drawLine(
                        color = CardBorderGray,
                        start = Offset(size.width / 2, startY),
                        end = Offset(size.width / 2, endY),
                        strokeWidth = 24f
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            topics.forEachIndexed { index, topic ->
                TopicNode(
                    topic = topic,
                    index = index,
                    onTopicClick = onTopicClick
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = "© Gimnazija Matije Antuna Reljkovića Vinkovci",
                color = Color.Black.copy(alpha = 0.25f),
                fontWeight = FontWeight.Thin,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun TopicNode(
    topic: TopicItem,
    index: Int,
    onTopicClick: (String) -> Unit
) {
    val offsetX = when (index % 3) {
        1 -> 60.dp
        2 -> (-60).dp
        else -> 0.dp
    }

    val lightIconColor = lerp(topic.color, Color.White, 0.9f)
    val darkBorderColor = lerp(topic.color, Color.Black, 0.2f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .offset(x = offsetX)
            .padding(bottom = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(topic.color)
                .border(4.dp, darkBorderColor, CircleShape)
                .clickable { onTopicClick(topic.jsonName) },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = topic.iconRes),
                contentDescription = topic.title,
                modifier = Modifier.size(45.dp),
                colorFilter = ColorFilter.tint(lightIconColor)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(percent = 50),
            color = Color.White,
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, CardBorderGray)
        ) {
            Text(
                text = topic.title,
                color = MathNeutral,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            )
        }
    }
}