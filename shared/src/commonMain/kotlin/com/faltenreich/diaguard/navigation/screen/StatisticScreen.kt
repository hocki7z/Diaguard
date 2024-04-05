package com.faltenreich.diaguard.navigation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.navigation.top.TopAppBarStyle
import com.faltenreich.diaguard.shared.di.getViewModel
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.statistic.Statistic

data object StatisticScreen : Screen {

    override val topAppBarStyle: TopAppBarStyle
        get() = TopAppBarStyle.CenterAligned {
            Text(getString(Res.string.statistic))
        }

    @Composable
    override fun Content() {
        Statistic(viewModel = getViewModel())
    }
}