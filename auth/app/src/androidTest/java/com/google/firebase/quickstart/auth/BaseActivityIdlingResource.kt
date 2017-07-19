package com.google.firebase.quickstart.auth

import android.support.test.espresso.IdlingResource

/**
 * Monitor Activity idle status by watching ProgressDialog.
 */
class BaseActivityIdlingResource(private val mActivity: BaseActivity) : IdlingResource {
    private var mCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "BaseActivityIdlingResource:" + mActivity.localClassName
    }

    override fun isIdleNow(): Boolean {
        val dialog = mActivity.mProgressDialog
        val idle = dialog == null || !dialog.isShowing

        mCallback.takeIf { idle }?.run { onTransitionToIdle() }

        return idle
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        mCallback = callback
    }
}
