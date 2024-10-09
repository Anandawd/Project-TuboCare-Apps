package com.project.tubocare.core.presentation.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.project.tubocare.R
import com.project.tubocare.core.domain.model.Page
import com.project.tubocare.core.domain.model.pages
import com.project.tubocare.ui.theme.TuboCareTheme

@Composable
fun OnBoardingPage(
    page: Page
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier.size(320.dp)
        ){
            Image(
                painter = painterResource(id = page.image),
                contentDescription = null,
                modifier = Modifier.fillMaxSize())
        }
        Text(
            text = stringResource(id = page.title),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Box(modifier = Modifier.width(320.dp)){
            Text(
                text = stringResource(id = page.description),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,

                )
        }
        Spacer(modifier = Modifier.heightIn(30.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingPagePreview() {
    TuboCareTheme {
        OnBoardingPage(page = pages[0])
    }
}