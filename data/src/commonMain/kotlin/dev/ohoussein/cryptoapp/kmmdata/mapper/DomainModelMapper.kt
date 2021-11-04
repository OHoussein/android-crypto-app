package dev.ohoussein.cryptoapp.kmmdata.mapper

import dev.ohoussein.cryptoapp.domain.model.DomainCrypto
import dev.ohoussein.cryptoapp.domain.model.DomainCryptoDetails
import dev.ohoussein.cryptoapp.kmmdata.model.CryptoDetailsResponse
import dev.ohoussein.cryptoapp.kmmdata.model.TopCryptoResponse

class DomainModelMapper(private val language: String) {

    fun convert(data: List<TopCryptoResponse>): List<DomainCrypto> =
            data.mapIndexed { index, item -> convert(item, index) }

    fun convert(data: TopCryptoResponse, index: Int) = DomainCrypto(
            id = data.id,
            symbol = data.symbol,
            name = data.name,
            imageUrl = data.image,
            price = data.currentPrice,
            priceChangePercentIn24h = data.priceChangePercentIn24h,
            order = index,
    )

    fun convert(data: CryptoDetailsResponse) = DomainCryptoDetails(
            id = data.id,
            symbol = data.symbol,
            name = data.name,
            imageUrl = data.image.large,
            hashingAlgorithm = data.hashingAlgorithm,
            homePageUrl = data.links.homepage.firstOrNull(),
            blockchainSite = data.links.blockchainSite.firstOrNull(),
            mainRepoUrl = data.links.reposUrl.firstNotNullOfOrNull { entry -> entry.value.firstOrNull { it.isNotEmpty() } },
            sentimentUpVotesPercentage = data.sentimentUpVotesPercentage,
            sentimentDownVotesPercentage = data.sentimentDownVotesPercentage,
            description = data.description[language] ?: data.description[DEFAULT_LANG] ?: "",
    )

    companion object {
        const val DEFAULT_LANG = "en"
    }

}
