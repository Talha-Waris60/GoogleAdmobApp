package com.devdroiddev.admobapp.listeners

import com.google.android.gms.ads.LoadAdError

interface AdsListener {
    fun onAdClicked()
    fun onAdClosed()
    fun onAdFailedToLoad(error: LoadAdError)
}