package dev.ohoussein.cryptoapp.kmmdata

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver = NativeSqliteDriver(CryptoDB.Schema, "CryptoDatabase.db")
}
