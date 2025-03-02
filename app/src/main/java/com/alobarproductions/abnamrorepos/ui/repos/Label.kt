package com.alobarproductions.abnamrorepos.ui.repos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alobarproductions.abnamrorepos.ui.theme.AbnAmroReposTheme

@Composable
fun Label(
    text: String,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
) {
    Text(
        modifier = modifier
            .border(
                width = 1.dp,
                color = textColor,
                shape = shape,
            )
            .clip(shape)
            .background(backgroundColor)
            .padding(
                horizontal = 10.dp,
                vertical = 0.dp,
            ),
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = textColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Preview
@Composable
private fun Preview() {
    AbnAmroReposTheme {
        Label("Foo bar")
    }
}