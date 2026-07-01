package com.aegyocounter.app.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aegyocounter.app.R
import com.aegyocounter.app.ui.theme.Ongeulip

@Composable
fun RecordSection(
    todayBest: Int,
    weekBest: Int,
    allBest: Int,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF4F7)
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = Color(0xFFFF7FA6),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(vertical = 8.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                RecordItem(
                    title = "오늘 최고",
                    count = todayBest,
                    icon = R.drawable.ic_today
                )

                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .width(1.dp)
                        .background(Color(0xFFE9D7D7))
                )

                RecordItem(
                    title = "이번 주",
                    count = weekBest,
                    icon = R.drawable.ic_week
                )

                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .width(1.dp)
                        .background(Color(0xFFE9D7D7))
                )

                RecordItem(
                    title = "전체 최고",
                    count = allBest,
                    icon = R.drawable.ic_best
                )

            }

        }

    }

}

@Composable
private fun RecordItem(
    title: String,
    count: Int,
    @DrawableRes icon: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = title,
            fontSize = 30.sp,
            fontFamily = Ongeulip,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = "${count}회",
            modifier = Modifier.padding(top = 4.dp),
            fontSize = 26.sp,
            fontFamily = Ongeulip,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF9B4038)
        )

        Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
                .padding(top = 4.dp)
                .size(40.dp)
        )
    }
}