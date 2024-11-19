package com.faltenreich.diaguard.backup.user.read

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.faltenreich.diaguard.AppTheme
import diaguard.shared.generated.resources.Res
import diaguard.shared.generated.resources.backup_read_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReadBackupForm(
    modifier: Modifier = Modifier,
    viewModel: ReadBackupFormViewModel,
) {
    Column(
        modifier = modifier.padding(all = AppTheme.dimensions.padding.P_3),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.padding.P_2),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.backup_read_description))
    }
}