package com.faltenreich.diaguard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.faltenreich.diaguard.navigation.Screen
import com.faltenreich.diaguard.navigation.bottom.BottomAppBar
import com.faltenreich.diaguard.navigation.bottom.BottomSheetNavigationItem
import com.faltenreich.diaguard.navigation.bottom.bottomAppBarStyle
import com.faltenreich.diaguard.navigation.top.TopAppBar
import com.faltenreich.diaguard.navigation.top.topAppBarStyle
import com.faltenreich.diaguard.shared.view.BottomSheet
import com.faltenreich.diaguard.shared.view.rememberBottomSheetState
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppView() {
    AppTheme {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = AppTheme.colorScheme.background,
        ) {
            Navigator(screen = Screen.Log()) { navigator ->
                Box {
                    val scope = rememberCoroutineScope()
                    val bottomSheetState = rememberBottomSheetState()
                    Scaffold(
                        topBar = {
                            val screen = navigator.lastItem as? Screen ?: return@Scaffold
                            TopAppBar(
                                style = screen.topAppBarStyle(),
                                navigator = navigator,
                            )
                        },
                        content = { padding ->
                            // FIXME: Crashes on pushing same Screen twice
                            FadeTransition(
                                navigator = navigator,
                                modifier = Modifier.padding(padding),
                            )
                        },
                        bottomBar = {
                            val screen = navigator.lastItem as? Screen ?: return@Scaffold
                            BottomAppBar(
                                style = screen.bottomAppBarStyle(),
                                onMenuClick = { scope.launch { bottomSheetState.show() } },
                            )
                        },
                    )
                    if (bottomSheetState.isVisible) {
                        // FIXME: Skips animation, maybe due to shared bottomSheetState
                        BottomSheet(
                            onDismissRequest = { scope.launch { bottomSheetState.hide() } },
                            sheetState = bottomSheetState,
                        ) {
                            Column {
                                BottomSheetNavigationItem(
                                    icon = MR.images.ic_dashboard,
                                    label = MR.strings.dashboard,
                                    isActive = navigator.lastItem is Screen.Dashboard,
                                    onClick = {
                                        scope.launch { bottomSheetState.hide() }
                                        navigator.replaceAll(Screen.Dashboard)
                                    },
                                )
                                BottomSheetNavigationItem(
                                    icon = MR.images.ic_timeline,
                                    label = MR.strings.timeline,
                                    isActive = navigator.lastItem is Screen.Timeline,
                                    onClick = {
                                        scope.launch { bottomSheetState.hide() }
                                        navigator.replaceAll(Screen.Timeline())
                                    },
                                )
                                BottomSheetNavigationItem(
                                    icon = MR.images.ic_log,
                                    label = MR.strings.log,
                                    isActive = navigator.lastItem is Screen.Log,
                                    onClick = {
                                        scope.launch { bottomSheetState.hide() }
                                        navigator.replaceAll(Screen.Log())
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}