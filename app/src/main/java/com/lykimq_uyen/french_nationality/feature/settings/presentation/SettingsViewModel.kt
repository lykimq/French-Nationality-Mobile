package com.lykimq_uyen.french_nationality.feature.settings.presentation

import android.app.Activity
import android.app.Application
import android.content.pm.PackageManager
import androidx.lifecycle.AndroidViewModel
import com.lykimq_uyen.french_nationality.core.billing.DonationBillingController
import com.lykimq_uyen.french_nationality.core.billing.DonationMessage
import com.lykimq_uyen.french_nationality.core.billing.DonationUiState
import com.lykimq_uyen.french_nationality.core.di.AppContainer
import com.lykimq_uyen.french_nationality.core.settings.ThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel(
    application: Application,
) : AndroidViewModel(application) {

    private val themeController = AppContainer.appThemeController(application)
    private val studyProgressRepository = AppContainer.studyProgressRepository(application)
    private val donationBillingController: DonationBillingController =
        AppContainer.donationBillingController(application)

    val themeMode: StateFlow<ThemeMode> = themeController.themeMode
    val donationState: StateFlow<DonationUiState> = donationBillingController.donationState
    val donationMessage: StateFlow<DonationMessage?> = donationBillingController.donationMessage

    private val _showResetProgressDialog = MutableStateFlow(false)
    val showResetProgressDialog: StateFlow<Boolean> = _showResetProgressDialog.asStateFlow()

    val appVersionName: String = readAppVersionName(application)

    fun startDonations() {
        donationBillingController.start()
    }

    fun stopDonations() {
        donationBillingController.stop()
    }

    fun purchaseCoffee(activity: Activity) {
        donationBillingController.purchaseCoffee(activity)
    }

    fun clearDonationMessage() {
        donationBillingController.clearMessage()
    }

    fun setThemeMode(themeMode: ThemeMode) {
        themeController.setThemeMode(themeMode)
    }

    fun requestResetProgress() {
        _showResetProgressDialog.value = true
    }

    fun dismissResetProgressDialog() {
        _showResetProgressDialog.value = false
    }

    fun confirmResetProgress() {
        studyProgressRepository.clearAllProgress()
        _showResetProgressDialog.value = false
    }

    private fun readAppVersionName(application: Application): String {
        return try {
            @Suppress("DEPRECATION")
            application.packageManager
                .getPackageInfo(application.packageName, 0)
                .versionName ?: "1.0.0"
        } catch (_: PackageManager.NameNotFoundException) {
            "1.0.0"
        }
    }
}
