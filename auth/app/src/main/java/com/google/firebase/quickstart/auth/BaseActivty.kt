package com.google.firebase.quickstart.auth

/**
 * Created by cwdoh on 2017. 7. 17..
 */
import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

open class BaseActivity : AppCompatActivity() {
    @VisibleForTesting
    var mProgressDialog : ProgressDialog? = null

    fun showProgressDialog() {
        (mProgressDialog ?: ProgressDialog(this)).also {
            it.setMessage(getString(R.string.loading))
            it.isIndeterminate = true
            it.show()

            mProgressDialog = it
        }
    }

    fun hideProgressDialog() {
        mProgressDialog?.let {
            when (it.isShowing) {
                true -> it.dismiss()
            }
        }
    }

    public override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }
}