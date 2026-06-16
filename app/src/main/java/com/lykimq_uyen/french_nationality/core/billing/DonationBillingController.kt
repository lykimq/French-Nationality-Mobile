package com.lykimq_uyen.french_nationality.core.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DonationBillingController(
    context: Context,
) {
    private val appContext = context.applicationContext

    private val _donationState = MutableStateFlow<DonationUiState>(DonationUiState.Loading)
    val donationState: StateFlow<DonationUiState> = _donationState.asStateFlow()

    private val _donationMessage = MutableStateFlow<DonationMessage?>(null)
    val donationMessage: StateFlow<DonationMessage?> = _donationMessage.asStateFlow()

    private var cachedProduct: DonationProduct? = null
    private var billingClient: BillingClient? = null
    private var isStarted = false

    private val purchasesUpdatedListener = PurchasesUpdatedListener { billingResult, purchases ->
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases.orEmpty().forEach(::handlePurchase)
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _donationState.value = cachedProduct?.let(DonationUiState::Ready)
                    ?: DonationUiState.Unavailable
                _donationMessage.value = DonationMessage.Cancelled
            }
            else -> {
                _donationState.value = cachedProduct?.let(DonationUiState::Ready)
                    ?: DonationUiState.Unavailable
                _donationMessage.value = DonationMessage.Error(
                    billingResult.debugMessage.ifBlank {
                        "Le paiement n'a pas abouti."
                    },
                )
            }
        }
    }

    fun start() {
        if (isStarted) {
            return
        }
        isStarted = true
        _donationState.value = DonationUiState.Loading
        connectBillingClient()
    }

    fun stop() {
        isStarted = false
        billingClient?.endConnection()
        billingClient = null
        cachedProduct = null
        _donationState.value = DonationUiState.Loading
    }

    fun clearMessage() {
        _donationMessage.value = null
    }

    fun purchaseCoffee(activity: Activity) {
        val product = cachedProduct
        if (product == null) {
            _donationMessage.value = DonationMessage.Error(
                "Le don n'est pas disponible pour le moment.",
            )
            return
        }

        val client = billingClient
        if (client == null || !client.isReady) {
            _donationMessage.value = DonationMessage.Error(
                "Google Play n'est pas disponible.",
            )
            return
        }

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(product.productDetails)
            .build()
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        _donationState.value = DonationUiState.Processing
        val launchResult = client.launchBillingFlow(activity, billingFlowParams)
        if (launchResult.responseCode != BillingClient.BillingResponseCode.OK) {
            _donationState.value = DonationUiState.Ready(product)
            _donationMessage.value = DonationMessage.Error(
                launchResult.debugMessage.ifBlank {
                    "Impossible d'ouvrir le paiement Google Play."
                },
            )
        }
    }

    private fun connectBillingClient() {
        val client = BillingClient.newBuilder(appContext)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases(
                PendingPurchasesParams.newBuilder()
                    .enableOneTimeProducts()
                    .build(),
            )
            .build()
        billingClient = client

        client.startConnection(
            object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (!isStarted) {
                        return
                    }
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                        queryDonationProduct(client)
                        queryPendingPurchases(client)
                    } else {
                        _donationState.value = DonationUiState.Unavailable
                    }
                }

                override fun onBillingServiceDisconnected() {
                    if (!isStarted) {
                        return
                    }
                    _donationState.value = DonationUiState.Unavailable
                    cachedProduct = null
                }
            },
        )
    }

    private fun queryDonationProduct(client: BillingClient) {
        val productList = DonationProductIds.all.map { productId ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        client.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (!isStarted) {
                return@queryProductDetailsAsync
            }
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                _donationState.value = DonationUiState.Unavailable
                return@queryProductDetailsAsync
            }

            val productDetails = productDetailsList.firstOrNull()
            val offer = productDetails?.oneTimePurchaseOfferDetails
            if (productDetails == null || offer == null) {
                _donationState.value = DonationUiState.Unavailable
                cachedProduct = null
                return@queryProductDetailsAsync
            }

            val donationProduct = DonationProduct(
                productId = productDetails.productId,
                formattedPrice = offer.formattedPrice,
                productDetails = productDetails,
            )
            cachedProduct = donationProduct
            _donationState.value = DonationUiState.Ready(donationProduct)
        }
    }

    private fun queryPendingPurchases(client: BillingClient) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()
        client.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                purchases.forEach(::handlePurchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            return
        }
        if (!DonationProductIds.all.contains(purchase.products.firstOrNull())) {
            return
        }

        val client = billingClient ?: return
        val consumeParams = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        client.consumeAsync(consumeParams) { billingResult, _ ->
            cachedProduct?.let { product ->
                _donationState.value = DonationUiState.Ready(product)
            }
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                _donationMessage.value = DonationMessage.ThankYou
            } else {
                _donationMessage.value = DonationMessage.Error(
                    billingResult.debugMessage.ifBlank {
                        "Le don a été reçu mais n'a pas pu être confirmé."
                    },
                )
            }
        }
    }
}
