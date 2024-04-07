package com.faltenreich.diaguard

import com.faltenreich.diaguard.backup.backupModule
import com.faltenreich.diaguard.dashboard.dashboardModule
import com.faltenreich.diaguard.datetime.dateTimeModule
import com.faltenreich.diaguard.entry.entryModule
import com.faltenreich.diaguard.export.exportModule
import com.faltenreich.diaguard.food.foodModule
import com.faltenreich.diaguard.log.logModule
import com.faltenreich.diaguard.main.mainModule
import com.faltenreich.diaguard.measurement.measurementModule
import com.faltenreich.diaguard.navigation.navigationModule
import com.faltenreich.diaguard.preference.preferenceModule
import com.faltenreich.diaguard.shared.architecture.coroutineModule
import com.faltenreich.diaguard.shared.clipboard.clipboardModule
import com.faltenreich.diaguard.shared.config.configModule
import com.faltenreich.diaguard.shared.database.databaseModule
import com.faltenreich.diaguard.shared.database.sqldelight.sqlDelightModule
import com.faltenreich.diaguard.shared.file.fileModule
import com.faltenreich.diaguard.shared.keyvalue.keyValueStoreModule
import com.faltenreich.diaguard.shared.localization.localizationModule
import com.faltenreich.diaguard.shared.networking.networkingModule
import com.faltenreich.diaguard.shared.primitive.primitiveModule
import com.faltenreich.diaguard.shared.serialization.serializationModule
import com.faltenreich.diaguard.shared.theme.themeModule
import com.faltenreich.diaguard.statistic.statisticModule
import com.faltenreich.diaguard.tag.tagModule
import com.faltenreich.diaguard.timeline.timelineModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

object DependencyInjection {

    fun setup(declaration: KoinAppDeclaration = {}) {
        startKoin {
            declaration()
            modules(mainModules())
        }
    }
}

private fun mainModules() = listOf(
    // Common
    primitiveModule(),
    coroutineModule(),
    configModule(),
    localizationModule(),
    clipboardModule(),
    serializationModule(),
    fileModule(),
    keyValueStoreModule(),
    sqlDelightModule(),
    databaseModule(),
    networkingModule(),
    dateTimeModule(),
    // Feature
    themeModule(),
    backupModule(),
    mainModule(),
    exportModule(),
    navigationModule(),
    dashboardModule(),
    logModule(),
    timelineModule(),
    entryModule(),
    measurementModule(),
    foodModule(),
    tagModule(),
    statisticModule(),
    preferenceModule(),
)