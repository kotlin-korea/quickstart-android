/**
 * Copyright Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.firebase.quickstart.auth

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Demonstrate Firebase Authentication using a custom minted token. For more information, see:
 * https://firebase.google.com/docs/auth/android/custom-auth
 */
class CustomAuthActivity : AppCompatActivity(), View.OnClickListener {

    // [START declare_auth]
    private val mAuth: FirebaseAuth by lazy {
        // [START initialize_auth]
        FirebaseAuth.getInstance()
        // [END initialize_auth]
    }
    // [END declare_auth]

    private var mCustomToken: String? = null
    private var mTokenReceiver: TokenBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)

        // Button click listeners
        findViewById(R.id.button_sign_in).setOnClickListener(this)

        // Create token receiver (for demo purposes only)
        mTokenReceiver = object : TokenBroadcastReceiver() {
            override fun onNewToken(token: String) {
                Log.d(TAG, "onNewToken:" + token)
                setCustomToken(token)
            }
        }

    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth.currentUser?.let {
            updateUI(it)
        }
    }
    // [END on_start_check_user]

    override fun onResume() {
        super.onResume()
        registerReceiver(mTokenReceiver, TokenBroadcastReceiver.getFilter())
    }


    override fun onPause() {
        super.onPause()
        unregisterReceiver(mTokenReceiver)
    }

    private fun startSignIn() {
        // Initiate sign in with custom token
        // [START sign_in_custom]
        mAuth.signInWithCustomToken(mCustomToken!!)
                .addOnCompleteListener(this) { task ->
                    when (task.isSuccessful) {
                        true -> {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success")
                            mAuth.currentUser.let {
                                updateUI(it)
                            }
                        }
                        else -> {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.exception)
                            Toast.makeText(this@CustomAuthActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
                }
        // [END sign_in_custom]
    }

    private fun updateUI(user: FirebaseUser?) {
        val textView = findViewById(R.id.text_sign_in_status) as TextView

        textView.text =
                if (user != null)
                    "User ID: " + user.uid
                else
                    "Error: sign in failed."
    }

    private fun setCustomToken(token: String) {
        mCustomToken = token

        val status: String =
            if (mCustomToken != null)
                "Token:" + mCustomToken!!
            else
                "Token: null"

        // Enable/disable sign-in button and show the token
        findViewById(R.id.button_sign_in).isEnabled = mCustomToken != null
        (findViewById(R.id.text_token_status) as TextView).text = status
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_sign_in) {
            startSignIn()
        }
    }

    companion object {

        private val TAG = "CustomAuthActivity"
    }
}
