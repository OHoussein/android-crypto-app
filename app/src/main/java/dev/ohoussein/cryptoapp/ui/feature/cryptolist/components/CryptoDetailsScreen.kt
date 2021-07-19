@file:Suppress("LongParameterList")

package dev.ohoussein.cryptoapp.ui.feature.cryptolist.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ohoussein.cryptoapp.debug.DataPreview.previewCryptoDetails
import dev.ohoussein.cryptoapp.ui.base.CryptoAppScaffold
import dev.ohoussein.cryptoapp.ui.base.StateError
import dev.ohoussein.cryptoapp.ui.base.StateLoading
import dev.ohoussein.cryptoapp.ui.core.mapper.ErrorMessageMapper
import dev.ohoussein.cryptoapp.ui.core.model.CryptoDetails
import dev.ohoussein.cryptoapp.ui.core.model.Resource
import dev.ohoussein.cryptoapp.ui.core.model.Status
import dev.ohoussein.cryptoapp.ui.core.util.ExternalNavigator
import dev.ohoussein.cryptoapp.ui.feature.cryptolist.viewmodel.CryptoDetailsViewModel
import dev.ohoussein.cryptoapp.ui.theme.CryptoAppTheme

@Composable
fun CryptoDetails(
    modifier: Modifier = Modifier,
    crypto: CryptoDetails,
    onHomePageClicked: (CryptoDetails) -> Unit,
    onBlockchainSiteClicked: (CryptoDetails) -> Unit,
    onSourceCodeClicked: (CryptoDetails) -> Unit,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier
            .padding(horizontal = 12.dp)
            .verticalScroll(scrollState),
    ) {
        CryptoDetailsHeader(
            Modifier.padding(top = 12.dp),
            crypto = crypto,
        )
        CryptoLinks(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            crypto = crypto,
            onHomePageClicked = onHomePageClicked,
            onBlockchainSiteClicked = onBlockchainSiteClicked,
            onSourceCodeClicked = onSourceCodeClicked,
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
fun CryptoDetailsStateScreen(
    modifier: Modifier = Modifier,
    cryptoDetailsState: Resource<CryptoDetails>,
    errorMessageMapper: ErrorMessageMapper,
    onRefresh: () -> Unit,
    onHomePageClicked: (CryptoDetails) -> Unit,
    onBlockchainSiteClicked: (CryptoDetails) -> Unit,
    onSourceCodeClicked: (CryptoDetails) -> Unit,
) {
    when {
        cryptoDetailsState.data != null -> {
            CryptoDetails(
                modifier = modifier,
                crypto = cryptoDetailsState.data,
                onHomePageClicked = onHomePageClicked,
                onBlockchainSiteClicked = onBlockchainSiteClicked,
                onSourceCodeClicked = onSourceCodeClicked,
            )
        }
        cryptoDetailsState.status == Status.ERROR -> {
            StateError(
                modifier = modifier,
                message = errorMessageMapper.map(cryptoDetailsState.error),
                onRetryClick = onRefresh,
            )
        }
        else -> {
            StateLoading(modifier = modifier)
        }
    }
}

@Composable
fun CryptoDetailsScreen(
    viewModel: CryptoDetailsViewModel,
    cryptoId: String,
    errorMessageMapper: ErrorMessageMapper,
    externalNavigator: ExternalNavigator,
    onBackClicked: () -> Unit,
) {

    val state: Resource<CryptoDetails> by viewModel.cryptoDetails.observeAsState(Resource.loading())

    LaunchedEffect(cryptoId) {
        viewModel.load(cryptoId)
    }

    CryptoAppScaffold(onBackButton = onBackClicked) {
        CryptoDetailsStateScreen(
            Modifier.fillMaxSize(),
            errorMessageMapper = errorMessageMapper,
            cryptoDetailsState = state,
            onRefresh = { viewModel.load(cryptoId) },
            onHomePageClicked = { crypto ->
                crypto.homePageUrl?.let { externalNavigator.openWebUrl(it) }
            },
            onBlockchainSiteClicked = { crypto ->
                crypto.blockchainSite?.let { externalNavigator.openWebUrl(it) }
            },
            onSourceCodeClicked = { crypto ->
                crypto.mainRepoUrl?.let { externalNavigator.openWebUrl(it) }
            },
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PreviewCryptoList() {
    CryptoAppTheme {
        CryptoDetails(
            crypto = previewCryptoDetails,
            onHomePageClicked = {},
            onBlockchainSiteClicked = {},
            onSourceCodeClicked = {},
        )
    }
}