package com.faltenreich.diaguard

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.onboarding.FirstStart
import com.faltenreich.diaguard.onboarding.OnboardingViewModel
import com.faltenreich.diaguard.onboarding.OnboardingViewState
import com.faltenreich.diaguard.preference.list.usecase.ColorScheme
import com.faltenreich.diaguard.shared.di.inject
import com.faltenreich.diaguard.shared.theme.ThemeViewModel
import com.faltenreich.diaguard.shared.view.LoadingIndicator
import com.faltenreich.diaguard.shared.view.keyboardPadding

@Composable
fun AppView(
    modifier: Modifier = Modifier,
    onboardingViewModel: OnboardingViewModel = inject(),
    themeViewModel: ThemeViewModel = inject(),
) {
    val colorScheme = themeViewModel.viewState.collectAsState().value
    val isDarkColorScheme = colorScheme?.let { it == ColorScheme.DARK } ?: isSystemInDarkTheme()
    AppTheme(isDarkColorScheme = isDarkColorScheme) {
        Surface (
            modifier = modifier.fillMaxSize().keyboardPadding(),
            color = AppTheme.colors.scheme.surface,
        ) {
            when (onboardingViewModel.viewState.collectAsState().value) {
                is OnboardingViewState.Loading -> LoadingIndicator()
                is OnboardingViewState.FirstStart -> FirstStart(modifier = modifier)
                is OnboardingViewState.SubsequentStart -> MainView(modifier = modifier)
            }
        }
    }
}