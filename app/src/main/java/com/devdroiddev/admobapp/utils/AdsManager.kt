package com.devdroiddev.admobapp.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.devdroiddev.admobapp.activities.MainActivity
import com.devdroiddev.admobapp.listeners.AdsListener
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdsManager {

    companion object {

        // Declare Variable
        private lateinit var adRequest: AdRequest
        private var _interstitialAd: InterstitialAd? = null
        private var _rewardedAd: RewardedAd? = null

        fun initializeAds(context: Context) {
            MobileAds.initialize(context)
            adRequest = AdRequest.Builder().build()
        }

        fun loadBannerAds(context: Context, bannerAdView: AdView, adsListener: AdsListener) {
            bannerAdView.loadAd(adRequest)
            bannerAdView.adListener = object : AdListener() {
                override fun onAdClicked() {
                    adsListener.onAdClicked()
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdClosed() {
                    adsListener.onAdClosed()
                    // Code to be executed when the user is about to return
                    // to the app after tapping on an ad.
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adsListener.onAdFailedToLoad(adError)
                    // Code to be executed when an ad request fails.
                }

                override fun onAdImpression() {
                    Log.d(MainActivity.APP_TAG, "onAdImpression")
                    // Code to be executed when an impression is recorded
                    // for an ad.
                }

                override fun onAdLoaded() {
                    Log.d(MainActivity.APP_TAG, "onAdLoaded")

                    // Code to be executed when an ad finishes loading.
                }

                override fun onAdOpened() {
                    Log.d(MainActivity.APP_TAG, "onAdOpened")
                    // Code to be executed when an ad opens an overlay that
                    // covers the screen.
                }

            }
        }

        fun loadInterstitialAds(context: Context, adUnitId: String, adsListener: AdsListener) {
            InterstitialAd.load(
                context,
                adUnitId,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        super.onAdLoaded(interstitialAd)
                        _interstitialAd = interstitialAd

                        // CallBacks
                        _interstitialAd?.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdClicked() {
                                    // Called when a click is recorded for an ad.
                                    adsListener.onAdClicked()
                                }

                                override fun onAdDismissedFullScreenContent() {
                                    // Called when ad is dismissed.
                                    adsListener.onAdClosed()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    // Called when ad fails to show.
                                    Log.e(
                                        MainActivity.APP_TAG,
                                        "Ad failed to show fullscreen content."
                                    )
                                }

                                override fun onAdImpression() {
                                    // Called when an impression is recorded for an ad.
                                    Log.d(MainActivity.APP_TAG, "Ad recorded an impression.")
                                }

                                override fun onAdShowedFullScreenContent() {
                                    // Called when ad is shown.
                                    _interstitialAd = null
                                }
                            }
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        adsListener.onAdFailedToLoad(loadAdError)
                    }
                })
        }

        fun showInterstitialAd(context: Context) {
            if (_interstitialAd != null) {
                _interstitialAd?.show(context as Activity)
            } else {
                Log.d(MainActivity.APP_TAG, "Ad is not ready yet")
            }

        }

        // load Rewarded Ads
        fun loadRewardedAds(context: Context, adUnitId: String, adsListener: AdsListener) {
            // Load Rewarded Ads
            RewardedAd.load(
                context,
                adUnitId,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(rewardedAd: RewardedAd) {
                        Log.d(MainActivity.APP_TAG, "Loaded -> $rewardedAd")
                        _rewardedAd = rewardedAd

                        // callback
                        _rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                            override fun onAdClicked() {
                                // Called when a click is recorded for an ad.
                                adsListener.onAdClicked()
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Set the ad reference to null so you don't show the ad a second time.
                                _rewardedAd = null
                                adsListener.onAdClosed()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Log.e(MainActivity.APP_TAG, "Ad failed to show fullscreen content.")
                                _rewardedAd = null
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(MainActivity.APP_TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(MainActivity.APP_TAG, "Ad showed fullscreen content.")
                            }
                        }
                    }
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        adsListener.onAdFailedToLoad(adError)
                    }
                })

        }

        fun showRewardedAds(context: Context) {
            if (_rewardedAd != null) {
                _rewardedAd?.let { ad ->
                    ad.show(context as Activity, OnUserEarnedRewardListener { rewardItem ->
                        // Handle the reward.
                    })
                } ?: run {
                    Log.d(MainActivity.APP_TAG, "The rewarded ad wasn't ready yet.")
                }
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }
    }

}