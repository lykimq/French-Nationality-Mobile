package com.lykimq_uyen.french_nationality.core.billing

import com.android.billingclient.api.ProductDetails

data class DonationProduct(
    val productId: String,
    val formattedPrice: String,
    val productDetails: ProductDetails,
)

sealed interface DonationUiState {
    data object Loading : DonationUiState

    data object Unavailable : DonationUiState

    data class Ready(
        val product: DonationProduct,
    ) : DonationUiState

    data object Processing : DonationUiState
}

sealed interface DonationMessage {
    data object ThankYou : DonationMessage

    data object Cancelled : DonationMessage

    data class Error(val text: String) : DonationMessage
}
