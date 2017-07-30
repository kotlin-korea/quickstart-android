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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_chooser.*

/**
 * Simple list-based Activity to redirect to one of the other Activities. This Activity does not
 * contain any useful code related to Firebase Authentication. You may want to start with
 * one of the following Files:
 * [GoogleSignInActivity]
 * [FacebookLoginActivity]
 * [TwitterLoginActivity]
 * [EmailPasswordActivity]
 * [PhoneAuthActivity]
 * [AnonymousAuthActivity]
 * [CustomAuthActivity]
 */
class ChooserActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooser)

        // Set up ListView and Adapter
        list_view.also {
            // set Adapter
            it.adapter = MyArrayAdapter(this, android.R.layout.simple_list_item_2, CLASSES).apply {
                setDescriptionIds(DESCRIPTION_IDS)
            }
            // set listener
            it.onItemClickListener = this
        }
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        startActivity(Intent(this, CLASSES[position]))
    }

    class MyArrayAdapter(private val mContext: Context, resource: Int, private val mClasses: Array<Class<*>>) : ArrayAdapter<Class<*>>(mContext, resource, mClasses) {
        private var mDescriptionIds: IntArray? = null

        private val mInflateView : View by lazy {
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(android.R.layout.simple_list_item_2, null)
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
            (convertView ?: mInflateView).also {
                (it.findViewById(android.R.id.text1) as TextView).text = mClasses[position].simpleName
                (it.findViewById(android.R.id.text2) as TextView).setText(mDescriptionIds!![position])
            }

        fun setDescriptionIds(descriptionIds: IntArray) {
            mDescriptionIds = descriptionIds
        }
    }

    companion object {

        private val CLASSES = arrayOf<Class<*>> (
            GoogleSignInActivity::class.java,
            FacebookLoginActivity::class.java,
            TwitterLoginActivity::class.java,
            EmailPasswordActivity::class.java,
            PhoneAuthActivity::class.java,
            AnonymousAuthActivity::class.java,
            FirebaseUIActivity::class.java,
            CustomAuthActivity::class.java
        )

        private val DESCRIPTION_IDS = intArrayOf(
            R.string.desc_google_sign_in,
            R.string.desc_facebook_login,
            R.string.desc_twitter_login,
            R.string.desc_emailpassword,
            R.string.desc_phone_auth,
            R.string.desc_anonymous_auth,
            R.string.desc_firebase_ui,
            R.string.desc_custom_auth
        )
    }
}
