package com.google.samples.quickstart.admobexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd

/**
 * Created by soul on 2017. 7. 23..
 */
class MainActivityKt : AppCompatActivity() {

    private val TAG : String = "MainActivityKt"
    private var mAdView : AdView? = null
    private var mInterstitialAd: InterstitialAd? = null
    private var mLoadInterstitialButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdView = findViewById(R.id.adView) as AdView
        var adRequest:AdRequest = AdRequest.Builder().build()
        mAdView!!.loadAd(adRequest)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd!!.adUnitId = getString(R.string.interstitial_ad_unit_id)

        mInterstitialAd!!.adListener = object :AdListener(){
            override fun onAdClosed() {
                requestNewInterstitial()
                beginSecondActivity()
            }

            override fun onAdLoaded() {
                if(mLoadInterstitialButton != null){
                    mLoadInterstitialButton!!.isEnabled = true
                }
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.w(TAG, "onAdFailedToLoad:"+i)
            }
        }

    }

    private fun beginSecondActivity() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun requestNewInterstitial() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

