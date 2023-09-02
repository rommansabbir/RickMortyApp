package com.rommansabbir.rickmortyapp.base.composeext

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rommansabbir.rickmortyapp.R


@Composable
fun SimpleToolbar(
    title: String,
    showBackButton: Boolean,
    showLoading: Boolean,
    onBackAction: () -> Unit = {}
) {
    Column(
        modifier = FillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            FillMaxWidth().height(56.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton){
                Column(
                    Modifier
                        .fillMaxSize()
                        .weight(.15F),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { onBackAction() }) {
                            Icon(
                                contentDescription = "",
                                painter = painterResource(id = R.drawable.baseline_arrow_back_24)
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(if (showBackButton).85F else 1F),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = FillMaxWidth().padding(start = 16.dp)
                )
            }
        }
        if (showLoading) {
            LinearProgressIndicator(FillMaxWidth().height(3.dp), Color(0xFF32CD32))
        } else {
            Column(FillMaxWidth().height(3.dp)) {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SimpleToolbarPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        SimpleToolbar(title = "Hello...", showBackButton = true, showLoading = true)
    }
}