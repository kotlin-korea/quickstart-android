package com.google.firebase.quickstart.auth

import android.app.ProgressDialog
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    @VisibleForTesting
    val mProgressDialog : ProgressDialog by lazy {
        ProgressDialog(this).apply {
            isIndeterminate = true
        }
    }

    fun showProgressDialog() {
        mProgressDialog.run {
            setMessage(getString(R.string.loading))
            show()
        }
    }

    fun hideProgressDialog() {
        mProgressDialog.takeIf { it.isShowing }?.dismiss()
    }

    public override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }
}