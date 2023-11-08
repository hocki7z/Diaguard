package com.faltenreich.diaguard.preference.list

import com.faltenreich.diaguard.MR
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyListScreen
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceListListItem
import com.faltenreich.diaguard.preference.list.item.preferences
import com.faltenreich.diaguard.preference.store.about.GetAppVersionUseCase
import com.faltenreich.diaguard.preference.store.color.ColorScheme
import com.faltenreich.diaguard.preference.store.color.GetColorSchemeUseCase
import com.faltenreich.diaguard.preference.store.color.SetColorSchemeUseCase
import com.faltenreich.diaguard.preference.store.screen.GetStartScreenUseCase
import com.faltenreich.diaguard.preference.store.screen.SetStartScreenUseCase
import com.faltenreich.diaguard.preference.store.screen.StartScreen
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.networking.UrlOpener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDefaultPreferencesUseCase(
    private val urlOpener: UrlOpener,
    private val getColorScheme: GetColorSchemeUseCase,
    private val setColorScheme: SetColorSchemeUseCase,
    private val getStartScreen: GetStartScreenUseCase,
    private val setStartScreen: SetStartScreenUseCase,
    private val getAppVersion: GetAppVersionUseCase,
) {

    operator fun invoke(): Flow<List<PreferenceListItem>> {
        return combine(
            getColorScheme(),
            getStartScreen(),
            getAppVersion(),
        ) { colorScheme, startScreen, appVersion ->
            preferences {
                list {
                    title = MR.strings.color_scheme
                    subtitle = getString(colorScheme.labelResource)
                    options = ColorScheme.entries.map { value ->
                        PreferenceListListItem.Option(
                            label = { getString(value.labelResource) },
                            isSelected = value == colorScheme,
                            onSelected = { setColorScheme(value) },
                        )
                    }
                }
                list {
                    title = MR.strings.start_screen
                    subtitle = getString(startScreen.labelResource)
                    options = StartScreen.entries.map { value ->
                        PreferenceListListItem.Option(
                            label = { getString(value.labelResource) },
                            isSelected = value == startScreen,
                            onSelected = { setStartScreen(value) },
                        )
                    }
                }
                category {
                    title = MR.strings.data
                    icon = MR.images.ic_data
                }
                action {
                    title = MR.strings.measurement_properties
                    onClick = { it.push(MeasurementPropertyListScreen) }
                }
                category {
                    title = MR.strings.contact
                    icon = MR.images.ic_contact
                }
                action {
                    title = MR.strings.homepage
                    subtitle = getString(MR.strings.homepage_url_short)
                    onClick = { urlOpener.open(getString(MR.strings.homepage_url)) }
                }
                action {
                    title = MR.strings.mail
                    subtitle = getString(MR.strings.mail_url_short)
                    onClick = { urlOpener.open(getString(MR.strings.mail_url)) }
                }
                action {
                    title = MR.strings.facebook
                    subtitle = getString(MR.strings.facebook_url_short)
                    onClick = { urlOpener.open(getString(MR.strings.facebook_url)) }
                }
                category {
                    title = MR.strings.about
                    icon = MR.images.ic_about
                }
                action {
                    title = MR.strings.source_code
                    subtitle = getString(MR.strings.source_code_url_short)
                    onClick = { urlOpener.open(getString(MR.strings.source_code_url)) }
                }
                action {
                    title = MR.strings.licenses
                    onClick = { TODO() }
                }
                action {
                    title = MR.strings.privacy_policy
                    onClick = { urlOpener.open(getString(MR.strings.privacy_policy_url)) }
                }
                action {
                    title = MR.strings.terms_and_conditions
                    onClick = { urlOpener.open(getString(MR.strings.terms_and_conditions_url)) }
                }
                action {
                    title = MR.strings.version
                    subtitle = appVersion
                    onClick = {}
                }
            }
        }
    }
}