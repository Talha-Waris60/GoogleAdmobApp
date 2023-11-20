package com.devdroiddev.admobapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.databinding.adapters.ToolbarBindingAdapter
import com.devdroiddev.admobapp.databinding.ActivityMainBinding
import com.devdroiddev.admobapp.listeners.AdsListener
import com.devdroiddev.admobapp.utils.AdsManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class MainActivity : AppCompatActivity(), AdsListener {

    companion object {
        const val APP_TAG = "Admob_App"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Ads
        AdsManager.initializeAds(this)

        // Show banner ads
        AdsManager.loadBannerAds(this, binding.bannerAdView, this)

        // Show Interstitial Ads
        AdsManager.loadInterstitialAds(this, "ca-app-pub-3940256099942544/1033173712", this)

        // Show Rewarded Ads
        AdsManager.loadRewardedAds(this, "ca-app-pub-3940256099942544/5224354917", this)

        initListeners()
    }

    private fun initListeners() {

        binding.interstitialAd.setOnClickListener {
            AdsManager.showInterstitialAd(this)
        }

        binding.rewardedAd.setOnClickListener {
            AdsManager.showRewardedAds(this)
        }
    }

    override fun onAdClicked() {
        Toast.makeText(this, "Ad Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onAdClosed() {
        Toast.makeText(this, "Ad Closed", Toast.LENGTH_SHORT).show()
    }

    override fun onAdFailedToLoad(error: LoadAdError) {
        Log.d(APP_TAG, "Error -> $error")
        Toast.makeText(this, "Ad Failed", Toast.LENGTH_SHORT).show()
    }

}