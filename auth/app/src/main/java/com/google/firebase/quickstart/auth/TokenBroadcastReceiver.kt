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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

/**
 * Receiver to capture tokens broadcast via ADB and insert them into the
 * running application to facilitate easy testing of custom authentication.
 */
abstract class TokenBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive:" + intent)

        intent.run {
            if (action == ACTION_TOKEN) {
                extras.getString(EXTRA_KEY_TOKEN)?.let {
                    onNewToken(it)
                }
            }
        }
    }

    abstract fun onNewToken(token: String)

    companion object {
        private val TAG = "TokenBroadcastReceiver"

        val ACTION_TOKEN = "com.google.example.ACTION_TOKEN"
        val EXTRA_KEY_TOKEN = "key_token"

        val filter: IntentFilter
            get() {
                val filter = IntentFilter(ACTION_TOKEN)
                return filter
            }
    }

}
