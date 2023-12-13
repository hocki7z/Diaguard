package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import com.faltenreich.diaguard.navigation.screen.Screen
import com.faltenreich.diaguard.shared.architecture.ViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

@Composable
actual inline fun <reified T : ViewModel<*>> Screen.getViewModel(
    qualifier: Qualifier?,
    noinline parameters: ParametersDefinition?,
): T {
    TODO("Not yet implemented")
}