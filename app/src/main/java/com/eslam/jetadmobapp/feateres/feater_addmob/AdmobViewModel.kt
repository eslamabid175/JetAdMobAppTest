package com.eslam.jetadmobapp.feateres.feater_addmob
import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.eslam.jetadmobapp.commons.addmob.AdMobManager
import com.eslam.jetadmobapp.feateres.feater_addmob.AdmobViewIntent
import com.eslam.jetadmobapp.feateres.feater_addmob.AdmobViewIntent.AdMob

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class AdmobViewModel : ViewModel(), KoinComponent {
    // Inject an instance of AdMobManager using Koin
    private val adMobManager: AdMobManager by inject()

    // MutableStateFlow to hold the state of the AdmobState
    private var _state = MutableStateFlow(AdmobState())

    // Public immutable state flow for observers
    val state = _state.asStateFlow()

    // Function to handle various Admob view actions
    fun dispatchViewAction(viewAction: AdmobViewIntent) {
        when (viewAction) {
            // Action to load an interstitial ad
            AdMob.LoadInterstitial -> loadInterstitial()
            // Action to show an interstitial ad
            is AdMob.ShowInterstitial -> showInterstitial(activity = viewAction.activity)
            // Action to load a video ad
            AdMob.LoadVideo -> loadRewardedAd()
            // Action to show a video ad
            is AdMob.ShowVideo -> showRewardedAd(activity = viewAction.activity)
            // Action to load a banner ad
            AdMob.LoadBanner -> loadBanner()
        }
    }

    // Function to load a banner ad
    private fun loadBanner() {
        // Load banner ad using AdMobManager and update the state with the loaded adView
        val adView = adMobManager.loadBannerAd()
        _state.update {
            it.copy(adView = adView)
        }
    }

    // Function to load an interstitial ad
    private fun loadInterstitial() {
        // Load interstitial ad using AdMobManager with callbacks for success and failure
        adMobManager.loadInterstitialAd(
            onAdLoaded = {
                Log.d("AdMobManager", "loadInterstitial: onAdLoaded")
            },
            onAdFailedToLoad = {
                Log.d("AdMobManager", "loadInterstitial: onAdFailedToLoad")
            }
        )
    }

    // Function to show an interstitial ad
    private fun showInterstitial(activity: Activity) {
        // Show interstitial ad using AdMobManager with various callbacks for ad events
        adMobManager.showInterstitialAd(
            activity = activity,
            onAdClicked = {
                Log.d("AdMobManager", "showInterstitial: onAdClicked")
            },
            onAdDismissedFullScreenContent = {
                Log.d("AdMobManager", "showInterstitial: onAdDismissedFullScreenContent")
            },
            onAdImpression = {
                Log.d("AdMobManager", "showInterstitial: onAdImpression")
            },
            onAdShowedFullScreenContent = {
                Log.d("AdMobManager", "showInterstitial: onAdShowedFullScreenContent")
            }
        )
    }

    // Function to load a rewarded video ad
    private fun loadRewardedAd() {
        // Load video ad using AdMobManager with callbacks for success and failure
        adMobManager.loadRewardedAd()
    }

    // Function to show  a rewarded video ad
    private fun showRewardedAd(activity: Activity) {
        adMobManager.showRewardedAd(
            activity = activity,
            onUserEarnedReward = { reward ->
                Log.d("AdMobManager", "User earned reward: ${reward.type} (${reward.amount})")
            },
            onAdClicked = {
                Log.d("AdMobManager", "Ad clicked.")
            },
            onAdDismissedFullScreenContent = {
                Log.d("AdMobManager", "Ad dismissed.")
            },
            onAdImpression = {
                Log.d("AdMobManager", "Ad impression recorded.")
            },
            onAdShowedFullScreenContent = {
                Log.d("AdMobManager", "Ad showed full screen.")
            },
            onAdFailedToShow = { error ->
                Log.d("AdMobManager", "Failed to show ad: $error")
            }
        )
    }
}