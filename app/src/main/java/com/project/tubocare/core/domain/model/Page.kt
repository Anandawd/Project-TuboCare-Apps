package com.project.tubocare.core.domain.model

import androidx.annotation.DrawableRes
import com.project.tubocare.R

data class Page(
    val title: Int,
    val description: Int,
    @DrawableRes val image: Int
)

val pages = listOf(
    Page(
        title = R.string.onboarding_title_1,
        description = R.string.onboarding_desc_1,
        image = R.drawable.img_onboarding_1
    ),
    Page(
        title = R.string.onboarding_title_2,
        description = R.string.onboarding_desc_2,
        image = R.drawable.img_onboarding_2
    ),
    Page(
        title = R.string.onboarding_title_3,
        description = R.string.onboarding_desc_3,
        image = R.drawable.img_onboarding_3
    ),
    Page(
        title = R.string.onboarding_title_4,
        description = R.string.onboarding_desc_4,
        image = R.drawable.img_onboarding_4
    )
)
