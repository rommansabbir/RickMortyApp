package com.rommansabbir.rickmortyapp.feature.charactersview

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.rommansabbir.rickmortyapp.R


/**
 * Rick and Morty Character List UI (item).
 *
 * @param modifier Default [Modifier].
 * @param id Character unique id.
 * @param name Character name.
 * @param imageUrl Character image url.
 * @param species Character species.
 * @param isAlive Is the character alive or not.
 * @param gender Character's gender.
 * @param totalEpisodes Characters total episode.
 * @param onItemDetail Callback to be invoked on character item click.
 */
@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun CharacterView(
    modifier: Modifier = Modifier,
    id: Int,
    name: String,
    imageUrl: String,
    species: String,
    isAlive: Boolean,
    gender: String,
    totalEpisodes: Int,
    onItemDetail: (id: Int) -> Unit
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(10.dp),
        onClick = { onItemDetail.invoke(id) }) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.4F)
                    .padding(4.dp)
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .aspectRatio(1F),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GlideImage(
                    model = imageUrl,
                    contentDescription = "",
                    modifier = Modifier.padding(4.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.5F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textModifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                Text(
                    text = name,
                    fontSize = 18.sp,
                    modifier = textModifier,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = species, modifier = textModifier
                )
                val aliveStatus = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("$gender, (")
                    }
                    withStyle(style = SpanStyle(color = if (isAlive) Color.Blue else Color.Red)) {
                        append(if (isAlive) "Alive" else "Dead")
                    }
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append(")")
                    }
                }
                Text(
                    text = aliveStatus, modifier = textModifier
                )
                val totalEpisodes = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black)) {
                        append("EP(s): ")
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
                    text = totalEpisodes,
                    modifier = textModifier
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.1F),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_arrow_right_alt_24),
                    contentDescription = "",
                    modifier = Modifier
                        .defaultMinSize(100.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardUIPreview() {
    Surface(
        Modifier.background(Color.Gray)
    ) {
        CharacterView(
            modifier = Modifier, id = 1, name = "Md. Romman Sabbir", "", "HUMAN", true, "Male", 15
        ) {}
    }
}