package com.faltenreich.diaguard.dashboard.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.dashboard.DashboardViewState
import com.faltenreich.diaguard.shared.localization.getString

@Composable
fun TodayDashboardItem(
    data: DashboardViewState.Revisit.Today?,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Box(modifier = Modifier.padding(all = AppTheme.dimensions.padding.P_3)) {
            Column(verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_3)) {
                Text(
                    text = getString(MR.strings.today),
                    color = AppTheme.colors.scheme.primary,
                )
                Row {
                    Text(
                        text = getString(MR.strings.measurements),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.totalCount?.toString() ?: getString(MR.strings.placeholder))
                }
                Row {
                    Text(
                        text = getString(MR.strings.hyper),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.hyperCount?.toString() ?: getString(MR.strings.placeholder))
                }
                Row {
                    Text(
                        text = getString(MR.strings.hypo),
                        modifier = Modifier.weight(1f),
                    )
                    Text(data?.hypoCount?.toString() ?: getString(MR.strings.placeholder))
                }
            }
        }
    }
}