package com.google.firebase.quickstart.auth

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.startsWith
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AnonymousTest {

    private var mActivityResource: IdlingResource? = null

    @Rule
    var mActivityTestRule = ActivityTestRule(AnonymousAuthActivity::class.java)

    @Before
    fun setUp() {
        mActivityResource?.let { Espresso.unregisterIdlingResources(it) }

        // Register Activity as idling resource
        mActivityResource = BaseActivityIdlingResource(mActivityTestRule.activity)
        Espresso.registerIdlingResources(mActivityResource!!)
    }

    @After
    fun tearDown() {
        mActivityResource?.let { Espresso.unregisterIdlingResources(it) }
    }

    @Test
    fun anonymousSignInTest() {
        // Sign out if possible
        signOutIfPossible()

        // Click sign in
        onView(allOf<View>(withId(R.id.button_anonymous_sign_in),
                withText(R.string.sign_in), isDisplayed())).perform(click())

        // Make sure userID and email look right
        val idString = mActivityTestRule.activity.getString(R.string.id_fmt, "")
        val emailString = mActivityTestRule.activity.getString(R.string.email_fmt, "")

        onView(withText(startsWith(idString)))
                .check(matches(isDisplayed()))

        onView(withText(startsWith(emailString)))
                .check(matches(isDisplayed()))
    }

    private fun signOutIfPossible() {
        try {
            onView(allOf<View>(withId(R.id.button_anonymous_sign_out), isDisplayed()))
                    .perform(click())
        } catch (e: NoMatchingViewException) {
            // Ignore
        }

    }
}
