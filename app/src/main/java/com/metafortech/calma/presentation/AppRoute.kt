package com.metafortech.calma.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class AppRoute {
    @Serializable
    object LanguageScreen : AppRoute()

    @Serializable
    object AuthNav : AppRoute()

    @Serializable
    object LoginScreen : AppRoute()

    @Serializable
    object RegisterScreen : AppRoute()

    @Serializable
    data class InterestSelectionScreen(val userToken: String? = null) : AppRoute()

    @Serializable
    data class SportSelectionScreen(
        val interestId: Int,
        val selectedLang: String,
        val userToken: String? = null
    ) : AppRoute()

    @Serializable
    data class VerificationScreen(
        val phoneNumber: String,
        val userToken: String? = null
    ) : AppRoute()


    @Serializable
    data class HomeNav(
        val userImageUrl: String? = null,
        val userName: String? = null,
    ) : AppRoute()

    @Serializable
    object HomeScreen : AppRoute()

    @Serializable
    object SportsFacilitiesScreen : AppRoute()

    @Serializable
    object ReelsScreen : AppRoute()

    @Serializable
    object StoreScreen : AppRoute()

    @Serializable
    object ChattingScreen : AppRoute()

}