package com.google.firebase.quickstart.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.TwitterAuthProvider
import com.twitter.sdk.android.Twitter
import com.twitter.sdk.android.core.*
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_twitter.*

class TwitterLoginActivity : BaseActivity(), View.OnClickListener {

    // [START declare_auth]
    private val mAuth: FirebaseAuth by lazy {
        // [START initialize_auth]
        // Initialize Firebase Auth
        FirebaseAuth.getInstance()
        // [END initialize_auth]
    }
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Twitter SDK
        val authConfig = TwitterAuthConfig(
                getString(R.string.twitter_consumer_key),
                getString(R.string.twitter_consumer_secret))
        Fabric.with(this, Twitter(authConfig))

        // Inflate layout (must be done after Twitter is configured)
        setContentView(R.layout.activity_twitter)

        button_twitter_signout.setOnClickListener(this)

        // [START initialize_twitter_login]
        button_twitter_login.callback = object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                Log.d(TAG, "twitterLogin:success" + result)
                handleTwitterSession(result.data)
            }

            override fun failure(exception: TwitterException) {
                Log.w(TAG, "twitterLogin:failure", exception)
                updateUI(null)
            }
        }
        // [END initialize_twitter_login]
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.currentUser?.let { updateUI(it) }
    }
    // [END on_start_check_user]

    // [START on_activity_result]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result to the Twitter login button.
        button_twitter_login.onActivityResult(requestCode, resultCode, data)
    }
    // [END on_activity_result]

    // [START auth_with_twitter]
    private fun handleTwitterSession(session: TwitterSession) {
        Log.d(TAG, "handleTwitterSession:" + session)
        // [START_EXCLUDE silent]
        showProgressDialog()
        // [END_EXCLUDE]

        val credential = TwitterAuthProvider.getCredential(
                session.authToken.token,
                session.authToken.secret)

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    when (task.isSuccessful) {
                        true -> {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success")
                            mAuth.currentUser?.let { updateUI(it) }
                        }
                        else -> {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.exception)
                            Toast.makeText(this@TwitterLoginActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
    }
    // [END auth_with_twitter]

    private fun signOut() {
        mAuth.signOut()
        Twitter.logOut()

        updateUI(null)
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()

        if (user != null) {
            status.text = getString(R.string.twitter_status_fmt, user.displayName)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            button_twitter_login.visibility = View.GONE
            button_twitter_signout.visibility = View.VISIBLE
        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            button_twitter_login.visibility = View.VISIBLE
            button_twitter_signout.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_twitter_signout) {
            signOut()
        }
    }

    companion object {
        private val TAG = "TwitterLogin"
    }
}
