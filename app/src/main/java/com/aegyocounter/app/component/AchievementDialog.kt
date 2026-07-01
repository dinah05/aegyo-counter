package com.aegyocounter.app.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.aegyocounter.app.R
import com.aegyocounter.app.ui.theme.Ongeulip
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AchievementDialog(
    count: Int,
    title: String,
    description: String,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + scaleIn(
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = 350f
                )
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .background(
                        Color(0xFFFFF4F7),
                        RoundedCornerShape(28.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 상단
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.dialog_alert),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )

                    Text(
                        text = "애교 경보",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = Ongeulip,
                        color = Color(0xFF9B4038)
                    )

                    Image(
                        painter = painterResource(R.drawable.dialog_alert),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )

                }

                Text(
                    text = "오늘 친구의 애교를",
                    fontFamily = Ongeulip,
                    modifier = Modifier.padding(top = 12.dp),
                    fontSize = 28.sp
                )

                Text(
                    text = "${count}번",
                    modifier = Modifier.padding(top = 2.dp),
                    fontFamily = Ongeulip,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFF5E8A)
                )

                Text(
                    text = "이나 견뎌냈습니다.",
                    modifier = Modifier.offset(y = (-2).dp),
                    fontFamily = Ongeulip,
                    fontSize = 28.sp
                )

                Image(
                    painter = painterResource(R.drawable.dialog_character),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(top = 12.dp, bottom = 18.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {

                    Image(
                        painter = painterResource(R.drawable.bg_achievement),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(
                                start = 114.dp,
                                top = 16.dp,
                                end = 20.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {

                        Text(
                            text = title,
                            fontFamily = Ongeulip,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFF5E8A),
                            textAlign = TextAlign.Center,
                            lineHeight = 28.sp,
                            maxLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = description,
                            fontFamily = Ongeulip,
                            fontSize = 20.sp,
                            color = Color(0xFF9B5A66),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                }

                Image(
                    painter = painterResource(R.drawable.btn_confirm),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .padding(top = 18.dp)
                        .clickable(onClick = onDismiss)
                )

            }

        }

    }

}