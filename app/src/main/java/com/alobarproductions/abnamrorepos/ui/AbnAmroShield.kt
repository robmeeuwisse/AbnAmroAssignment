package com.alobarproductions.abnamrorepos.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alobarproductions.abnamrorepos.R

@Composable
fun AbnAmroShield(
    modifier: Modifier = Modifier,
) {
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.abn_amro_shield),
        contentDescription = null,
    )
}

@Preview
@Composable
private fun Preview() {
    AbnAmroShield(modifier = Modifier.size(48.dp))
}
