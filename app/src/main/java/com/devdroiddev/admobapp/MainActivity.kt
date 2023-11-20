package com.devdroiddev.admobapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.databinding.BindingAdapter
import com.devdroiddev.admobapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {

    companion object {
        const val APP_TAG = "Admob_App"
    }
    private lateinit var binding : ActivityMainBinding
    private lateinit var adRequest : AdRequest
    private var _interstitialAd: InterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Ads
        init()
        initListener()
    }

    private fun init() {
       MobileAds.initialize(this)
        adRequest  = AdRequest.Builder().build()
    }

    private fun initListener() {

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
                _interstitialAd?.show(this@MainActivity)
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.")
            }
        }, 10000)
    }

}