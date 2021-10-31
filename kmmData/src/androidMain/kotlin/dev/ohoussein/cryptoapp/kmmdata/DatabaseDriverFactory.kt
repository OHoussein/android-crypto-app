package dev.ohoussein.cryptoapp.kmmdata

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
            AndroidSqliteDriver(CryptoDB.Schema, context, "CryptoDatabase.db")
}

fun initDatabase(context: Context): CryptoDB {
    val driver = DatabaseDriverFactory(context).createDriver()
    return CryptoDB(driver)
}
