package com.google.samples.quickstart.config

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

/**
 * Created by cwdoh on 2017. 7. 15..
 */

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    // Remote Config keys
    private val LOADING_PHRASE_CONFIG_KEY = "loading_phrase"
    private val WELCOME_MESSAGE_KEY = "welcome_message"
    private val WELCOME_MESSAGE_CAPS_KEY = "welcome_message_caps"

    private val mFirebaseRemoteConfig : FirebaseRemoteConfig by lazy {
        // Get Remote Config instance.
        FirebaseRemoteConfig.getInstance().apply {
            // Create a Remote Config Setting to enable developer mode, which you can use to increase
            // the number of fetches available per hour during development. See Best Practices in the
            // README for more information.
            // [START enable_dev_mode]
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build()

            setConfigSettings(configSettings)
            // [END enable_dev_mode]

            // Set default Remote Config parameter values. An app uses the in-app default values, and
            // when you need to adjust those defaults, you set an updated value for only the values you
            // want to change in the Firebase console. See Best Practices in the README for more
            // information.
            // [START set_default_values]
            setDefaults(R.xml.remote_config_defaults)
            // [END set_default_values]
        }
    }
    private val mWelcomeTextView : TextView by lazy { welcomeTextView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        fetchButton.setOnClickListener { fetchWelcome() }

        fetchWelcome()
    }

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
    private fun fetchWelcome(): Unit {
        mWelcomeTextView.text = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY)

        var cacheExpiration : Long = 3600
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (mFirebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating the next fetch request
        // will use fetch data from the Remote Config service, rather than cached parameter values,
        // if cached parameter values are more than cacheExpiration seconds old.
        // See Best Practices in the README for more information.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
            .addOnCompleteListener(this) { task ->
                // shortcut to show short length toast message
                val showShortLengthText = fun (message: String) : Unit {
                    Toast.makeText(
                            this@MainActivity,     // https://kotlinlang.org/docs/reference/this-expressions.html
                            message,
                            Toast.LENGTH_SHORT).show()
                }

                if (task.isSuccessful) {
                    showShortLengthText("Fetch Succeeded")

                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    mFirebaseRemoteConfig.activateFetched()
                }
                else {
                    showShortLengthText("Fetch Failed")
                }

                displayWelcomeMessage()
            }
        // [END fetch_config_with_callback]
    }

    /**
     * Display a welcome message in all caps if welcome_message_caps is set to true. Otherwise,
     * display a welcome message as fetched from welcome_message.
     */
    // [START display_welcome_message]
    private fun displayWelcomeMessage() : Unit {
        mWelcomeTextView.run {
            setAllCaps(mFirebaseRemoteConfig.getBoolean(WELCOME_MESSAGE_CAPS_KEY))
            text = mFirebaseRemoteConfig.getString(WELCOME_MESSAGE_KEY)
        }
    }
    // [END display_welcome_message]
}