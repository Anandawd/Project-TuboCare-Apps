package com.project.tubocare.core.presentation.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.core.presentation.components.CustomPrimaryButton
import com.project.tubocare.core.domain.model.pages
import com.project.tubocare.core.presentation.onboarding.component.OnBoardingPage
import com.project.tubocare.core.presentation.onboarding.component.PageIndicator
import com.project.tubocare.ui.theme.TuboCareTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(
    onEvent: (OnBoardingEvent) -> Unit,
    navigateToLogin: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState(initialPage = 0) {
            pages.size
        }
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.logo_horizontal),
            contentDescription = null
        )
        HorizontalPager(state = pagerState) { index ->
            OnBoardingPage(page = pages[index])
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageIndicator(
                modifier = Modifier.width(80.dp),
                pageSize = pages.size,
                selectedPage = pagerState.currentPage
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center
            ) {
                if (pagerState.currentPage == 3) {
                    CustomPrimaryButton(
                        value = stringResource(id = R.string.btn_get_started),
                        onClick = {
                            navigateToLogin()
                            scope.launch {
                                onEvent(OnBoardingEvent.SaveAppEntry)
                            }
                        },
                        modifier = Modifier.testTag("get_started_button")
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun OnBoardingScreenPreview() {
    TuboCareTheme {
        OnBoardingScreen(
            onEvent = {},
            navigateToLogin = {}
        )
    }
}