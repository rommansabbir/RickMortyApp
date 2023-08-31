package com.rommansabbir.rickmortyapp.feature.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rommansabbir.rickmortyapp.R

@Composable
fun DashboardUI() {

}


@Composable
fun DashboardItem() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.25F)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(100.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.8F),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val textModifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
                Text(
                    text = "Test Name",
                    fontSize = 20.sp,
                    modifier = textModifier,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline
                )
                Text(
                    text = "HUMAN", modifier = textModifier
                )
                Text(
                    text = "Alive | Female", modifier = textModifier
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.15F),
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
        DashboardItem()
    }
}