@file:Suppress("LongParameterList")

package dev.ohoussein.crypto.presentation.components

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
import dev.ohoussein.crypto.presentation.debug.DataPreview.previewCryptoDetails
import dev.ohoussein.crypto.presentation.model.CryptoDetails
import dev.ohoussein.crypto.presentation.viewmodel.CryptoDetailsViewModel
import dev.ohoussein.cryptoapp.common.navigation.ExternalRouter
import dev.ohoussein.cryptoapp.common.resource.Resource
import dev.ohoussein.cryptoapp.common.resource.Status
import dev.ohoussein.cryptoapp.core.designsystem.base.CryptoAppScaffold
import dev.ohoussein.cryptoapp.core.designsystem.base.StateError
import dev.ohoussein.cryptoapp.core.designsystem.base.StateLoading
import dev.ohoussein.cryptoapp.core.designsystem.theme.CryptoAppTheme
import dev.ohoussein.cryptoapp.core.formatter.ErrorMessageFormatter

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
    errorMessageMapper: ErrorMessageFormatter,
    onRefresh: () -> Unit,
    onHomePageClicked: (CryptoDetails) -> Unit,
    onBlockchainSiteClicked: (CryptoDetails) -> Unit,
    onSourceCodeClicked: (CryptoDetails) -> Unit,
) {
    cryptoDetailsState.data?.let { data ->
        CryptoDetails(
            modifier = modifier,
            crypto = data,
            onHomePageClicked = onHomePageClicked,
            onBlockchainSiteClicked = onBlockchainSiteClicked,
            onSourceCodeClicked = onSourceCodeClicked,
        )
        return
    }

    when (cryptoDetailsState.status) {
        Status.ERROR -> {
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
    errorMessageFormatter: ErrorMessageFormatter,
    externalRouter: ExternalRouter,
    onBackClicked: () -> Unit,
) {

    val state: Resource<CryptoDetails> by viewModel.cryptoDetails.observeAsState(Resource.loading())

    LaunchedEffect(cryptoId) {
        viewModel.load(cryptoId)
    }

    CryptoAppScaffold(onBackButton = onBackClicked) {
        CryptoDetailsStateScreen(
            Modifier.fillMaxSize(),
            errorMessageMapper = errorMessageFormatter,
            cryptoDetailsState = state,
            onRefresh = { viewModel.load(cryptoId) },
            onHomePageClicked = { crypto ->
                crypto.homePageUrl?.let { externalRouter.openWebUrl(it) }
            },
            onBlockchainSiteClicked = { crypto ->
                crypto.blockchainSite?.let { externalRouter.openWebUrl(it) }
            },
            onSourceCodeClicked = { crypto ->
                crypto.mainRepoUrl?.let { externalRouter.openWebUrl(it) }
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
