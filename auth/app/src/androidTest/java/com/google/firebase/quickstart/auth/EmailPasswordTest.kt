package com.google.firebase.quickstart.auth


import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.IdlingResource
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.test.suitebuilder.annotation.LargeTest
import android.view.View
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@LargeTest
@RunWith(AndroidJUnit4::class)
class EmailPasswordTest {

    private var mActivityResource: IdlingResource? = null

    @Rule
    var mActivityTestRule = ActivityTestRule(EmailPasswordActivity::class.java)

    @Before
    fun setUp() {
        mActivityResource?.let { Espresso.unregisterIdlingResources(it) }

        // Register Activity as idling resource
        mActivityResource = BaseActivityIdlingResource(mActivityTestRule.activity).apply {
            Espresso.registerIdlingResources(this)
        }
    }

    @After
    fun tearDown() {
        mActivityResource?.let { Espresso.unregisterIdlingResources(it) }
    }

    @Test
    fun failedSignInTest() {
        val email = "test@test.com"
        val password = "123456"

        // Make sure we're signed out
        signOutIfPossible()

        // Enter email
        enterEmail(email)

        // Enter password
        enterPassword(password)

        // Click sign in
        val appCompatButton = onView(
                allOf<View>(withId(R.id.email_sign_in_button), withText(R.string.sign_in),
                        withParent(withId(R.id.email_password_buttons)),
                        isDisplayed()))
        appCompatButton.perform(click())

        // Check that auth failed
        onView(withText(R.string.auth_failed))
                .check(matches(isDisplayed()))
    }

    @Test
    fun successfulSignUpAndSignInTest() {
        val email = "user" + randomInt() + "@example.com"
        val password = "password" + randomInt()

        // Make sure we're signed out
        signOutIfPossible()

        // Enter email
        enterEmail(email)

        // Enter password
        enterPassword(password)

        // Click sign up
        val appCompatButton = onView(
                allOf<View>(withId(R.id.email_create_account_button), withText(R.string.create_account),
                        withParent(withId(R.id.email_password_buttons)),
                        isDisplayed()))
        appCompatButton.perform(click())

        // Sign out button shown
        onView(allOf<View>(withId(R.id.sign_out_button), withText(R.string.sign_out), isDisplayed()))

        // User email shown
        val emailString = mActivityTestRule.activity
                .getString(R.string.emailpassword_status_fmt, email)
        onView(withText(emailString))
                .check(matches(isDisplayed()))

        // Sign out
        signOutIfPossible()

        // Sign back in with the email and password
        enterEmail(email)
        enterPassword(password)

        // Click sign in
        val signInButton = onView(
                allOf<View>(withId(R.id.email_sign_in_button), withText(R.string.sign_in),
                        withParent(withId(R.id.email_password_buttons)),
                        isDisplayed()))
        signInButton.perform(click())

        // User email shown
        onView(withText(emailString))
                .check(matches(isDisplayed()))
    }

    private fun signOutIfPossible() {
        try {
            onView(allOf<View>(withId(R.id.sign_out_button), withText(R.string.sign_out), isDisplayed()))
                    .perform(click())
        } catch (e: NoMatchingViewException) {
            // Ignore
        }

    }

    private fun enterEmail(email: String) {
        val emailField = onView(
                allOf<View>(withId(R.id.field_email),
                        withParent(withId(R.id.email_password_fields)),
                        isDisplayed()))
        emailField.perform(replaceText(email))
    }

    private fun enterPassword(password: String) {
        val passwordField = onView(
                allOf<View>(withId(R.id.field_password),
                        withParent(withId(R.id.email_password_fields)),
                        isDisplayed()))
        passwordField.perform(replaceText(password))
    }

    private fun randomInt(): String {
        return Random().nextInt(100000).toString()
    }

}
