package com.eslam.jetadmobapp.commons.addmob

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdMobManager(private val context: Context) {
    // Variable to hold the interstitial ad
    private var mInterstitialAd: InterstitialAd? = null

    // Variable to hold rewardedAd
    private var rewardedAd: RewardedAd? = null

    // Ad unit ID for rewardedAd
    private val rewardedAdTestUnitId = "ca-app-pub-3940256099942544/5224354917"

    // Ad unit ID for interstitial
    private val interstitialTestUnitId = "ca-app-pub-3940256099942544/1033173712"

    // Ad unit ID for banner test
    private val adBannerTestUnitId = "ca-app-pub-3940256099942544/9214589741"

    // Initialize AdMob
    init {
        MobileAds.initialize(context) {
            Log.d("AdMob", "AdMob initialized")
        }
    }

    // Create an ad request
    private var adRequest = AdRequest.Builder().build()

    // Function to load an interstitial ad
    fun loadInterstitialAd(
        onAdFailedToLoad: (LoadAdError) -> Unit = {},
        onAdLoaded: () -> Unit = {},
    ) {
        // Load the interstitial ad with specified ad unit ID and ad request
        InterstitialAd.load(
            context,
            interstitialTestUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                // Called when ad fails to load
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    when (adError.code) {
                        //code(0) Something happened internally; for instance, an invalid response was received from the ad server.
                        AdRequest.ERROR_CODE_INTERNAL_ERROR -> {
                            Log.d("AdMobManager", "Internal error: ${adError.message}")
                        }
                        //code(1) The ad request was invalid; for instance, the ad unit ID was incorrect.
                        AdRequest.ERROR_CODE_INVALID_REQUEST -> {
                            Log.d("AdMobManager", "Invalid request: ${adError.message}")
                        }
                        //code(2) The ad request was unsuccessful due to network connectivity.
                        AdRequest.ERROR_CODE_NETWORK_ERROR -> {
                            Log.d("AdMobManager", "Network error: ${adError.message}")
                        }
                        //code(3) The ad request was successful, but no ad was returned due to lack of ad inventory.
                        AdRequest.ERROR_CODE_NO_FILL -> {
                            Log.d("AdMobManager", "No fill: ${adError.message}")
                        }

                        else -> {
                            Log.d("AdMobManager", "Unknown error: ${adError.message}")
                        }
                    }
                    mInterstitialAd = null
                    onAdFailedToLoad.invoke(adError)
                }

                // Called when ad is loaded successfully
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    onAdLoaded.invoke()
                }
            })
    }

    // Function to show the interstitial ad
    fun showInterstitialAd(
        activity: Activity,
        onAdClicked: () -> Unit = {},
        onAdDismissedFullScreenContent: () -> Unit = {},
        onAdImpression: () -> Unit = {},
        onAdShowedFullScreenContent: () -> Unit = {},
    ) {
        // Check if interstitial ad is loaded
        mInterstitialAd?.let { ad ->
            // Set full-screen content callback for ad events
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when a click is recorded for an ad.
                    onAdClicked.invoke()
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    mInterstitialAd = null
                    // Load a new ad for next time
                    loadInterstitialAd()
                    onAdDismissedFullScreenContent.invoke()
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    onAdImpression.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    onAdShowedFullScreenContent.invoke()
                }
            }
            // Show the interstitial ad
            ad.show(activity)
        } ?: run {
            // Log message if ad wasn't ready
            Log.d("AdMobManager", "The interstitial ad wasn't ready yet.")
        }
    }

    // Function to load a banner ad
    fun loadBannerAd(): AdView {
        // Create and return an AdView configured with ad size and unit ID
        return AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = adBannerTestUnitId
            loadAd(AdRequest.Builder().build())
        }
    }

    // Function to load a rewarded ad
    fun loadRewardedAd() {
        // Create an ad request
        val adRequest = AdRequest.Builder().build()

        // Load the rewarded ad with the specified ad unit ID
        RewardedAd.load(
            context,
            rewardedAdTestUnitId,  // Ad unit ID for the rewarded ad
            adRequest,  // Ad request containing the ad parameters
            object : RewardedAdLoadCallback() {  // Callback to handle ad loading events
                override fun onAdLoaded(ad: RewardedAd) {
                    // Called when the ad is successfully loaded
                    rewardedAd = ad
                    Log.d("AdMobManager", "Rewarded ad loaded.")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Called when the ad fails to load
                    rewardedAd = null
                    Log.d("AdMobManager", "Failed to load rewarded ad: ${adError.message}")
                }
            }
        )
    }

    // Function to show the rewarded ad
    fun showRewardedAd(
        activity: Activity,
        onUserEarnedReward: (RewardItem) -> Unit = {},  // Callback for when the user earns a reward
        onAdClicked: () -> Unit = {},  // Callback for when the ad is clicked
        onAdDismissedFullScreenContent: () -> Unit = {},  // Callback for when the ad is dismissed
        onAdImpression: () -> Unit = {},  // Callback for when an impression is recorded
        onAdShowedFullScreenContent: () -> Unit = {},  // Callback for when the ad is shown
        onAdFailedToShow: (AdError) -> Unit = {}  // Callback for when the ad fails to show
    ) {
        // Check if the rewarded ad is loaded and ready to be shown
        rewardedAd?.let { ad ->
            // Set the full-screen content callback to handle ad events
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    // Called when the ad is clicked
                    onAdClicked.invoke()
                }

                override fun onAdDismissedFullScreenContent() {
                    // Called when the ad is dismissed
                    rewardedAd = null
                    // Optionally load a new ad for the next time
                    loadRewardedAd()
                    onAdDismissedFullScreenContent.invoke()
                }

                override fun onAdImpression() {
                    // Called when an impression is recorded
                    onAdImpression.invoke()
                }

                override fun onAdShowedFullScreenContent() {
                    // Called when the ad is shown
                    onAdShowedFullScreenContent.invoke()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    // Called when the ad fails to show
                    onAdFailedToShow.invoke(adError)
                }
            }

            // Show the rewarded ad and handle the reward event
            ad.show(activity) { rewardItem ->
                onUserEarnedReward.invoke(rewardItem)
            }
        } ?: run {
            // Log message if the ad wasn't ready to be shown
            Log.d("AdMobManager", "The rewarded ad wasn't ready yet.")
        }
    }
}