package com.rommansabbir.rickmortyapp.feature.characterdetailview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rommansabbir.rickmortyapp.base.composeext.FillMaxWidth

/**
 * Show a character detail to the UI.
 *
 * @param name Character name.
 * @param imageURL Character image url.
 * @param info Character information's.
 * @param totalEpisodes Character's total episodes
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CharacterDetailsUI(
    name: String, imageURL: String, info: MutableList<Pair<String, String>>, totalEpisodes: Int
) {
    Column(
        FillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = FillMaxWidth().padding(16.dp)
        )
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(200.dp)
                .aspectRatio(1F)
                .padding(16.dp)
        ) {
            GlideImage(
                model = imageURL,
                contentDescription = "",
                contentScale = ContentScale.FillBounds
            )
        }

        Card(
            elevation = 4.dp, modifier = FillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Column(
                FillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Information",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = FillMaxWidth().padding(16.dp)
                )
                repeat(info.size) { index ->
                    Row(
                        FillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = info[index].first,
                            modifier = FillMaxWidth()
                                .weight(.5F)
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                            textAlign = TextAlign.Start
                        )
                        Text(
                            text = info[index].second,
                            modifier = FillMaxWidth()
                                .weight(.5F)
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = FillMaxWidth().height(16.dp))
            }
        }
        Card(elevation = 4.dp, modifier = FillMaxWidth().padding(16.dp)) {
            Column(
                FillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                val totalEPs = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("Total Episodes: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Red
                        )
                    ) {
                        append(totalEpisodes.toString())
                    }
                }
                Text(
                    text = totalEPs,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = FillMaxWidth().padding(16.dp)
                )
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailsUIPreview() {
    Column(modifier = Modifier.fillMaxSize()) {
        CharacterDetailsUI(
            "Rick and Morty", "", mutableListOf<Pair<String, String>>(

            ).apply {
                add(Pair("Name", "Md Romman Sabbir"))
                add(Pair("Name", "Md Romman Sabbir"))
                add(Pair("Name", "Md Romman Sabbir"))
                add(Pair("Name", "Md Romman Sabbir"))
            }, 12
        )
    }
}