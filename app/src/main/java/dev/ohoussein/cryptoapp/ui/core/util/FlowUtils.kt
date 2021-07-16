package dev.ohoussein.cryptoapp.ui.core.util

import dev.ohoussein.cryptoapp.ui.core.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

fun <T> Flow<T>.asResourceFlow(previousData: T?): Flow<Resource<T>> {
    return this
        .map<T, Resource<T>> {
            Resource.success(it)
        }
        .onStart {
            emit(Resource.loading(previousData))
        }
        .catch {
            Timber.e(it)
            emit(Resource.error(it, previousData))
        }
}
