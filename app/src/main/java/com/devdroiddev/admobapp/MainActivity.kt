package com.devdroiddev.admobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.devdroiddev.admobapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : AppCompatActivity() {

    companion object {
        const val APP_TAG = "Admob_App"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var adRequest: AdRequest
    private var _interstitialAd: InterstitialAd? = null
    private var _rewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Ads
        init()
        initLiseners()
        showBannerAd()
        // showInterstitialAd()
        // showRewardedAd()
    }


    private fun init() {
        MobileAds.initialize(this)
        adRequest = AdRequest.Builder().build()
    }

    private fun initLiseners() {

        binding.interstitialAd.setOnClickListener {
            showInterstitialAd()
        }

        binding.rewardedAd.setOnClickListener {
            showRewardedAd()
        }
    }


    private fun showInterstitialAd() {

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
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
                                Log.d(APP_TAG, "Ad was clicked.")
                            }

                            override fun onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                Log.d(APP_TAG, "Ad dismissed fullscreen content.")
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                // Called when ad fails to show.
                                Log.e(APP_TAG, "Ad failed to show fullscreen content.")
                            }

                            override fun onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                Log.d(APP_TAG, "Ad recorded an impression.")
                            }

                            override fun onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(APP_TAG, "Ad showed fullscreen content.")
                                _interstitialAd = null
                            }
                        }
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    Log.d(APP_TAG, "Load Error -> $loadAdError")
                }
            })

        Handler().postDelayed({
            if (_interstitialAd != null) {
                Log.d(APP_TAG, "Not Null")
                _interstitialAd?.show(this@MainActivity)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }, 10000)

    }

    private fun showBannerAd() {

        binding.bannerAdView.loadAd(adRequest)

        binding.bannerAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                Log.d(APP_TAG, "onAdClicked")
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                Log.d(APP_TAG, "onAdClosed")
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(APP_TAG, "onAdFailedToLoad")
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                Log.d(APP_TAG, "onAdImpression")
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                Log.d(APP_TAG, "onAdLoaded")

                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                Log.d(APP_TAG, "onAdOpened")
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

        }

    }


    private fun showRewardedAd() {

        // Load Rewarded Ads
        RewardedAd.load(
            this,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(APP_TAG, "Failed -> $adError")
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(APP_TAG, "Loaded -> $rewardedAd")
                    _rewardedAd = rewardedAd

                    // callback
                    _rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(APP_TAG, "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(APP_TAG, "Ad dismissed fullscreen content.")
                            _rewardedAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when ad fails to show.
                            Log.e(APP_TAG, "Ad failed to show fullscreen content.")
                            _rewardedAd = null
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(APP_TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(APP_TAG, "Ad showed fullscreen content.")
                        }
                    }
                }
            })

        Handler().postDelayed({
            if (_rewardedAd != null) {
                _rewardedAd?.let { ad ->
                    ad.show(this, OnUserEarnedRewardListener { rewardItem ->
                        // Handle the reward.
                        Toast.makeText(this@MainActivity, "Money Earned", Toast.LENGTH_SHORT).show()
                    })
                } ?: run {
                    Log.d(APP_TAG, "The rewarded ad wasn't ready yet.")
                }
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }, 10000)

    }

}