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
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_emailpassword.*

class EmailPasswordActivity : BaseActivity(), View.OnClickListener {

    // [START declare_auth]
    private val mAuth: FirebaseAuth by lazy {
        // [START initialize_auth]
        FirebaseAuth.getInstance()
        // [END initialize_auth]
    }
    // [END declare_auth]

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emailpassword)

        // Buttons
        listOf(email_sign_in_button, email_sign_in_button, sign_out_button, verify_email_button).onEach { it.setOnClickListener(this@EmailPasswordActivity) }
    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        updateUI(mAuth.currentUser)
    }
    // [END on_start_check_user]

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:" + email)
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        updateUI(mAuth.currentUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this@EmailPasswordActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:" + email)
        if (!validateForm()) {
            return
        }

        showProgressDialog()

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        updateUI(mAuth.currentUser)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this@EmailPasswordActivity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)

                        status.setText(R.string.auth_failed)
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog()
                    // [END_EXCLUDE]
                }
        // [END sign_in_with_email]
    }

    private fun signOut() {
        mAuth.signOut()
        updateUI(null)
    }

    private fun sendEmailVerification() {
        // Disable button
        verify_email_button.isEnabled = false

        // Send verification email
        // [START send_email_verification]
        val user = mAuth.currentUser
        user!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    // [START_EXCLUDE]
                    // Re-enable button
                    verify_email_button.isEnabled = true

                    if (task.isSuccessful) {
                        Toast.makeText(this@EmailPasswordActivity,
                                "Verification email sent to " + user.email,
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(this@EmailPasswordActivity,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                    // [END_EXCLUDE]
                }
        // [END send_email_verification]
    }

    private fun validateForm(): Boolean {
        var valid = true

        val checkFieldValidation = fun(field: EditText) {
            val text = field.text.toString()
            if (TextUtils.isEmpty(text)) {
                field.error = "Required."
                valid = false
            } else {
                field.error = null
            }
        }

        checkFieldValidation(field_email)
        checkFieldValidation(field_password)

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {
        hideProgressDialog()
        if (user != null) {
            status.text = getString(R.string.emailpassword_status_fmt,
                    user.email, user.isEmailVerified)
            detail.text = getString(R.string.firebase_status_fmt, user.uid)

            email_password_buttons.visibility = View.GONE
            email_password_fields.visibility = View.GONE
            signed_in_buttons.visibility = View.VISIBLE

            findViewById(R.id.verify_email_button).isEnabled = !user.isEmailVerified
        } else {
            status.setText(R.string.signed_out)
            detail.text = null

            email_password_buttons.visibility = View.VISIBLE
            email_password_fields.visibility = View.VISIBLE
            signed_in_buttons.visibility = View.GONE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.email_create_account_button -> createAccount(field_email!!.text.toString(), field_password!!.text.toString())
            R.id.email_sign_in_button -> signIn(field_email!!.text.toString(), field_password!!.text.toString())
            R.id.email_sign_in_button -> signOut()
            R.id.email_sign_in_button -> sendEmailVerification()
        }
    }

    companion object {
        private val TAG = "EmailPassword"
    }
}
