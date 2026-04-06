package sibalic.mathapp.components

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.core.CorePlugin
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import sibalic.mathapp.ui.theme.MathNeutral

@Composable
fun MathText(
    text: String,
    modifier: Modifier = Modifier,
    textColor: Color = MathNeutral,
    textSizeSp: Float = 15f
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).apply {
                setTextColor(textColor.toArgb())
                textSize = textSizeSp
            }
        },
        update = { textView ->
            val markwon = Markwon.builder(textView.context)
                .usePlugin(CorePlugin.create())
                .usePlugin(MarkwonInlineParserPlugin.create())
                .usePlugin(JLatexMathPlugin.create(textView.textSize * 0.95f) { builder ->
                    builder.inlinesEnabled(true)
                })
                .build()

            val formattedText = text.replace("$", "$$").replace("$$$$", "$$")

            markwon.setMarkdown(textView, formattedText)
        }
    )
}