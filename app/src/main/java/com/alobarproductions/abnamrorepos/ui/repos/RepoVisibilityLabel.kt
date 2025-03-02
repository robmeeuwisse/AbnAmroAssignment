package com.alobarproductions.abnamrorepos.ui.repos

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alobarproductions.abnamrorepos.R
import com.alobarproductions.abnamrorepos.core.Repo
import com.alobarproductions.abnamrorepos.ui.theme.AbnAmroReposTheme

@Composable
fun RepoVisibilityLabel(
    visibility: Repo.Visibility,
) {
    val textRes = when (visibility) {
        Repo.Visibility.Public -> R.string.repo_visibility_label_public
        Repo.Visibility.Private -> R.string.repo_visibility_label_private
        Repo.Visibility.Unknown -> null
    }
    if (textRes != null) {
        Label(stringResource(textRes))
    }
}

@Composable
private fun Label(
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
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            RepoVisibilityLabel(Repo.Visibility.Public)
            RepoVisibilityLabel(Repo.Visibility.Private)
            RepoVisibilityLabel(Repo.Visibility.Unknown) // Renders as gone
        }
    }
}
