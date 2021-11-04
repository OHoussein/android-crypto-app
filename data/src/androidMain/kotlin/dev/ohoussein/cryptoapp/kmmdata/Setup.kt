package dev.ohoussein.cryptoapp.kmmdata

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

actual fun initLogger() {
    Napier.base(DebugAntilog())
    Napier.d("Wiss: InitLogger")
}
