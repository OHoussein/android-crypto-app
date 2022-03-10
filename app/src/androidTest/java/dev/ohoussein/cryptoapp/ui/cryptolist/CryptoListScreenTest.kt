package dev.ohoussein.cryptoapp.ui.cryptolist

import android.content.res.Resources
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.hilt.navigation.compose.hiltViewModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.ohoussein.cryptoapp.R
import dev.ohoussein.cryptoapp.commonTest.mock.TestDataFactory
import dev.ohoussein.cryptoapp.domain.model.DomainCrypto
import dev.ohoussein.cryptoapp.domain.repo.ICryptoRepository
import dev.ohoussein.cryptoapp.test.di.CoreModule
import dev.ohoussein.cryptoapp.test.di.DataRepoModule
import dev.ohoussein.cryptoapp.ui.activity.RootActivity
import dev.ohoussein.cryptoapp.ui.feature.cryptolist.components.CryptoItemTestTag
import dev.ohoussein.cryptoapp.ui.feature.cryptolist.components.CryptoListScreen
import dev.ohoussein.cryptoapp.ui.feature.cryptolist.components.CryptoListTestTag
import dev.ohoussein.cryptoapp.ui.feature.cryptolist.viewmodel.HomeViewModel
import dev.ohoussein.cryptoapp.ui.navigation.NavPath
import dev.ohoussein.cryptoapp.ui.testutil.TestNavHost
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltAndroidTest
@UninstallModules(value = [CoreModule::class, DataRepoModule::class])
class CryptoListScreenTest {

    @Inject
    internal lateinit var cryptoRepo: ICryptoRepository

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<RootActivity>()

    private val res: Resources
        get() = composeTestRule.activity.resources

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun should_show_list_crypto() {
        givenListOfCrypto { data ->
            //When
            setupContent {
                //Then
                thenCryptoListShouldBeDisplayed(data)
            }
        }
    }

    @Test
    fun should_show_error_screen_and_retry() {
        //Given error
        givenErrorGetListOfCrypto { retry ->
            //When
            setupContent {
                //Then
                thenShouldDisplayError()
                retry()
                givenListOfCrypto { data ->
                    composeTestRule.onNodeWithText(res.getString(R.string.retry))
                        .performClick()
                    thenCryptoItemShouldBeDisplayed(data.first())
                }
            }
        }
    }

    @Test
    fun should_show_error_message_and_retry_when_refreshing() {
        givenListOfCrypto { data ->
            //When
            setupContent {
                //check just the first item
                thenCryptoItemShouldBeDisplayed(data.first())
                givenErrorGetListOfCrypto {
                    swipeToRefreshList()
                    // Then should display error and still see the list
                    thenShouldDisplayError()
                    thenCryptoItemShouldBeDisplayed(data.first())
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // private methods
    ///////////////////////////////////////////////////////////////////////////

    private fun setupContent(
        next: ComposeContentTestRule.() -> Unit
    ) {
        composeTestRule.setContent {
            TestNavHost(path = NavPath.HOME) {
                val viewModel = hiltViewModel<HomeViewModel>()
                CryptoListScreen(
                    viewModel = viewModel,
                    errorMessageMapper = composeTestRule.activity.errorMessageMapper,
                    onClick = {},
                )
            }
        }
        next(composeTestRule)
    }

    private fun swipeToRefreshList() {
        composeTestRule.onNodeWithTag(CryptoListTestTag).performGesture {
            swipeDown()
        }
    }

    private fun givenListOfCrypto(next: (List<DomainCrypto>) -> Unit) {
        val data = TestDataFactory.makeCryptoList(20)
        whenever(cryptoRepo.getTopCryptoList(any())).thenReturn(flowOf(data))
        runBlocking { whenever(cryptoRepo.refreshTopCryptoList(any())).thenReturn(Unit) }
        next(data)
    }

    private fun givenErrorGetListOfCrypto(next: (() -> Unit) -> Unit) {
        val flow = MutableSharedFlow<List<DomainCrypto>>()
        whenever(cryptoRepo.getTopCryptoList(any())).thenReturn(flow)
        runBlocking { whenever(cryptoRepo.refreshTopCryptoList(any())).thenAnswer { throw IOException() } }
        val retry: () -> Unit = {
            runBlockingTest { flow.emit(TestDataFactory.makeCryptoList(20)) }
        }
        next(retry)
    }

    private fun thenCryptoListShouldBeDisplayed(data: List<DomainCrypto>) {
        data.forEach { item ->
            thenCryptoItemShouldBeDisplayed(item)
            composeTestRule.onNodeWithTag(CryptoItemTestTag + item.id).performGesture {
                swipeUp()
            }
        }
    }

    private fun thenCryptoItemShouldBeDisplayed(item: DomainCrypto) {
        composeTestRule.onNodeWithText(
            item.name,
            useUnmergedTree = true
        ).assertExists()
    }

    private fun thenShouldDisplayError() {
        composeTestRule.onNodeWithText(res.getString(R.string.retry)).assertIsDisplayed()
    }
}
