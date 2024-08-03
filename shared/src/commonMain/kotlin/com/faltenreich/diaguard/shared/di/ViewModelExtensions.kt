/**
 * Workaround for: Duplicate JVM class name
 * https://youtrack.jetbrains.com/issue/KT-21186
 */
@file:JvmName("ViewModelExtensionsJvm")

package com.faltenreich.diaguard.shared.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.ParametersDefinition
import kotlin.jvm.JvmName

@Composable
inline fun <reified T : ViewModel> viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    noinline parameters: ParametersDefinition? = null,
): T {
    return koinViewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        parameters = parameters,
    )
}

@Composable
inline fun <reified T : ViewModel> sharedViewModel(
    noinline parameters: ParametersDefinition? = null,
): T {
    return viewModel(
        viewModelStoreOwner = LocalSharedViewModelStoreOwner.current,
        parameters = parameters,
    )
}

@Composable
expect fun rememberViewModelStoreOwner(): ViewModelStoreOwner

@Deprecated("Use Koin instead", ReplaceWith("viewModel"))
@Composable
inline fun <reified T : ViewModel> getViewModel(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    key: String? = null,
    noinline initializer: CreationExtras.() -> T,
): T {
    TODO()
}

@Deprecated("Use Koin instead", ReplaceWith("viewModel"))
@Composable
inline fun <reified T : ViewModel> getViewModel(
    owner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current),
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras =
        if (owner is HasDefaultViewModelProviderFactory) owner.defaultViewModelCreationExtras
        else CreationExtras.Empty,
): T {
    TODO()
}