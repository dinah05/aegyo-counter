package com.aegyocounter.app

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aegyocounter.app.component.AchievementDialog
import com.aegyocounter.app.ui.component.CharacterSection
import com.aegyocounter.app.ui.component.CounterButtonSection
import com.aegyocounter.app.ui.component.CounterSection
import com.aegyocounter.app.ui.component.Header
import com.aegyocounter.app.ui.component.RageGaugeSection
import com.aegyocounter.app.ui.component.RecordSection
import com.aegyocounter.app.ui.theme.AegyoCounterTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SideEffect.ShowToast ->
                    Toast.makeText(
                        context,
                        effect.message,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    MainContent(
        state = state,
        modifier = modifier,
        onIntent = viewModel::handleIntent,
        onDismissDialog = viewModel::dismissAchievementDialog
    )
}

@Composable
fun MainContent(
    state: CountState,
    modifier: Modifier = Modifier,
    onIntent: (CounterIntent) -> Unit,
    onDismissDialog: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Header()

        Spacer(modifier = Modifier.height(4.dp))

        CounterSection(
            count = state.number
        )

        CharacterSection(
            catMessage = state.catMessage,
            girlMessage = state.girlMessage
        )

        Spacer(modifier = Modifier.height(0.dp))

        RageGaugeSection(
            modifier = Modifier.offset(y = (-32).dp),
            progress = state.number.coerceAtMost(100) / 100f
        )

        Spacer(modifier = Modifier.height(0.dp))

        CounterButtonSection(
            modifier = Modifier.offset(y = (-12).dp),
            onIntent = onIntent
        )

        Spacer(modifier = Modifier.height(20.dp))

        RecordSection(
            modifier = Modifier.offset(y = (-10).dp),
            todayBest = state.todayBest,
            weekBest = state.weekBest,
            allBest = state.allBest
        )

        Spacer(modifier = Modifier.height(100.dp))

        if (state.showAchievementDialog) {
            AchievementDialog(
                count = state.achievementCount,
                title = state.achievementTitle,
                description = state.achievementDescription,
                onDismiss = onDismissDialog
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    AegyoCounterTheme {
        MainContent(
            state = CountState(),
            onIntent = {},
            onDismissDialog = {}
        )
    }
}