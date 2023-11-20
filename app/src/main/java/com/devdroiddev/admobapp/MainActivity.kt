package com.devdroiddev.admobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.BindingAdapter
import com.devdroiddev.admobapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    companion object {
        const val APP_TAG = "Admob_App"
    }
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Ads
        MobileAds.initialize(this)
        loadAdsRequest()
        initListener()
    }

    private fun initListener() {

        binding.bannerAdView.adListener = object: AdListener() {
            override fun onAdClicked() {
                Log.d(APP_TAG, "onAdClicked")
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                Log.d(APP_TAG, "onAdClosed")
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Log.d(APP_TAG, "Message -> $adError")
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

    private fun loadAdsRequest() {

        val adRequest = AdRequest.Builder().build()
        binding.bannerAdView.loadAd(adRequest)

    }
}