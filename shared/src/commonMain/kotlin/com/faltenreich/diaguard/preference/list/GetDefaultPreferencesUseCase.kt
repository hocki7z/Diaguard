package com.faltenreich.diaguard.preference.list

import diaguard.shared.generated.resources.*
import com.faltenreich.diaguard.navigation.NavigateToScreenUseCase
import com.faltenreich.diaguard.navigation.screen.MeasurementPropertyListScreen
import com.faltenreich.diaguard.navigation.screen.TagListScreen
import com.faltenreich.diaguard.preference.ColorScheme
import com.faltenreich.diaguard.preference.StartScreen
import com.faltenreich.diaguard.preference.list.item.PreferenceListItem
import com.faltenreich.diaguard.preference.list.item.PreferenceListListItem
import com.faltenreich.diaguard.preference.list.item.preferences
import com.faltenreich.diaguard.preference.store.GetPreferenceUseCase
import com.faltenreich.diaguard.preference.store.SetPreferenceUseCase
import com.faltenreich.diaguard.shared.config.GetAppVersionUseCase
import com.faltenreich.diaguard.shared.localization.getString
import com.faltenreich.diaguard.shared.networking.UrlOpener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class GetDefaultPreferencesUseCase(
    private val urlOpener: UrlOpener,
    private val getPreference: GetPreferenceUseCase,
    private val setPreference: SetPreferenceUseCase,
    private val getAppVersion: GetAppVersionUseCase,
    private val navigateToScreen: NavigateToScreenUseCase,
) {

    operator fun invoke(): Flow<List<PreferenceListItem>> {
        return combine(
            getPreference(ColorScheme.Preference),
            getPreference(StartScreen.Preference),
            getAppVersion(),
        ) { colorScheme, startScreen, appVersion ->
            preferences {
                list {
                    title = Res.string.color_scheme
                    subtitle = getString(colorScheme.labelResource)
                    options = ColorScheme.entries.map { value ->
                        PreferenceListListItem.Option(
                            label = { getString(value.labelResource) },
                            isSelected = value == colorScheme,
                            onSelected = { setPreference(ColorScheme.Preference, value) },
                        )
                    }
                }
                list {
                    title = Res.string.start_screen
                    subtitle = getString(startScreen.labelResource)
                    options = StartScreen.entries.map { value ->
                        PreferenceListListItem.Option(
                            label = { getString(value.labelResource) },
                            isSelected = value == startScreen,
                            onSelected = { setPreference(StartScreen.Preference, value) },
                        )
                    }
                }
                category {
                    title = Res.string.data
                    icon = Res.drawable.ic_data
                }
                action {
                    title = Res.string.measurement_properties
                    onClick = { navigateToScreen(MeasurementPropertyListScreen) }
                }
                action {
                    title = Res.string.tags
                    onClick = { navigateToScreen(TagListScreen) }
                }
                category {
                    title = Res.string.contact
                    icon = Res.drawable.ic_contact
                }
                action {
                    title = Res.string.homepage
                    subtitle = getString(Res.string.homepage_url_short)
                    onClick = { urlOpener.open(getString(Res.string.homepage_url)) }
                }
                action {
                    title = Res.string.mail
                    subtitle = getString(Res.string.mail_url_short)
                    onClick = { urlOpener.open(getString(Res.string.mail_url)) }
                }
                action {
                    title = Res.string.facebook
                    subtitle = getString(Res.string.facebook_url_short)
                    onClick = { urlOpener.open(getString(Res.string.facebook_url)) }
                }
                category {
                    title = Res.string.about
                    icon = Res.drawable.ic_about
                }
                action {
                    title = Res.string.source_code
                    subtitle = getString(Res.string.source_code_url_short)
                    onClick = { urlOpener.open(getString(Res.string.source_code_url)) }
                }
                action {
                    title = Res.string.licenses
                    onClick = { TODO() }
                }
                action {
                    title = Res.string.privacy_policy
                    onClick = { urlOpener.open(getString(Res.string.privacy_policy_url)) }
                }
                action {
                    title = Res.string.terms_and_conditions
                    onClick = { urlOpener.open(getString(Res.string.terms_and_conditions_url)) }
                }
                action {
                    title = Res.string.version
                    subtitle = appVersion
                    onClick = {}
                }
            }
        }
    }
}