package com.google.samples.quickstart.admobexample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd

/**
 * Created by soul on 2017. 7. 23..
 */
class MainActivityKt : AppCompatActivity() {

    private val TAG : String = "MainActivityKt"

    private val mAdView by lazy {
        findViewById(R.id.adView) as AdView
    }

    private val mInterstitialAd by lazy {
        InterstitialAd(this)
    }

    private val mLoadInterstitialButton by lazy {
        findViewById(R.id.load_interstitial_button)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var adRequest: AdRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mInterstitialAd.adUnitId = getString(R.string.interstitial_ad_unit_id)

        mInterstitialAd.adListener = object : AdListener(){
            override fun onAdClosed() {
                requestNewInterstitial()
                beginSecondActivity()
            }

            override fun onAdLoaded() {
                if(mLoadInterstitialButton != null){
                    mLoadInterstitialButton.isEnabled = true
                }
            }

            override fun onAdFailedToLoad(i: Int) {
                Log.w(TAG, "onAdFailedToLoad:"+i)
            }
        }

        // [END create_interstitial_ad_listener]

        // [START display_interstitial_ad]
        mLoadInterstitialButton.setOnClickListener {
            if(mInterstitialAd.isLoaded){
                mInterstitialAd.show()
            }else{
                beginSecondActivity()
            }
        }
        // [END display_interstitial_ad]

        // Disable button if an interstitial ad is not loaded yet.
        mLoadInterstitialButton.isEnabled = mInterstitialAd.isLoaded

    }

    private fun requestNewInterstitial() {
        var adRequest = AdRequest.Builder().build()
        mInterstitialAd.loadAd(adRequest)
    }

    private fun beginSecondActivity() {
        var intent :Intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        if(mAdView != null){
            mAdView.destroy()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if(mAdView != null){
            mAdView.resume()
        }
        if(!mInterstitialAd.isLoaded){
            requestNewInterstitial()
        }
    }

    override fun onDestroy() {
        if(mAdView != null){
            mAdView.destroy()
        }
        super.onDestroy()
    }

    fun getAdView() : AdView{
        return mAdView
    }
}

