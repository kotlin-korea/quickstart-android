/**
 * Copyright 2016 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.quickstart.auth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_anonymous_auth.*

/**
 * Activity to demonstrate anonymous login and account linking (with an email/password account).
 */
class AnonymousAuthActivity : BaseActivity(), View.OnClickListener {

    // [START declare_auth]
    private val mAuth: FirebaseAuth by lazy {
        // [START initialize_auth]
        FirebaseAuth.getInstance()
        // [END initialize_auth]
    }
    // [END declare_auth]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anonymous_auth)

        // Click listeners
        button_anonymous_sign_in.setOnClickListener(this)
        button_anonymous_sign_out.setOnClickListener(this)
        button_link_account.setOnClickListener(this)
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(mAuth.currentUser)
    }
    // [END on_start_check_user]

    private fun signInAnonymously() {
        showProgressDialog()
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInAnonymously:success")
                        updateUI(mAuth.currentUser)
                    }
                    else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInAnonymously:failure", task.exception)
                        Toast.makeText(this@AnonymousAuthActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
        // [END signin_anonymously]
    }

    private fun signOut() {
        mAuth.signOut()
        updateUI(null)
    }

    private fun linkAccount() {
        // Make sure form is valid
        if (!validateLinkForm()) {
            return
        }

        // Get email and password from form
        val email = field_email.text.toString()
        val password = field_password.text.toString()

        // Create EmailAuthCredential with email and password
        val credential = EmailAuthProvider.getCredential(email, password)

        // Link the anonymous user to the email credential
        showProgressDialog()

        // [START link_credential]
        mAuth.currentUser!!.linkWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "linkWithCredential:success")
                        val user = task.result.user
                        updateUI(user)
                    }
                    else {
                        Log.w(TAG, "linkWithCredential:failure", task.exception)
                        Toast.makeText(this@AnonymousAuthActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
        // [END link_credential]
    }

    private fun validateLinkForm(): Boolean {
        var valid = true

        val setField = fun (field: EditText) {
            if (TextUtils.isEmpty(field.text.toString())) {
                field.error = "Required."
                valid = false
            } else {
                field.error = null
            }
        }

        setField(field_email)
        setField(field_password)

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()

        val isSignedIn = user != null

        // Status text
        if (isSignedIn) {
            user?.let {
                anonymous_status_id.text = getString(R.string.id_fmt, it.uid)
                anonymous_status_email.text = getString(R.string.email_fmt, it.email)
            }
        }
        else {
            anonymous_status_id.setText(R.string.signed_out)
            anonymous_status_email.text = null
        }

        // Button visibility
        button_anonymous_sign_in.isEnabled = !isSignedIn
        button_anonymous_sign_in.isEnabled = !isSignedIn
        button_anonymous_sign_out.isEnabled = isSignedIn
        button_link_account.isEnabled = isSignedIn
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_anonymous_sign_in -> signInAnonymously()
            R.id.button_anonymous_sign_out -> signOut()
            R.id.button_link_account -> linkAccount()
        }
    }

    companion object {
        private val TAG = "AnonymousAuth"
    }
}
