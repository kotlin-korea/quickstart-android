package com.google.firebase.quickstart.analytics

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivityKt : AppCompatActivity() {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each image.
     * This uses a {@link FragmentPagerAdapter}, which keeps every loaded fragment in memory.
     */
    private var mImagePagerAdapter: ImagePagerAdapter? = null
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mFavoriteFood: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        var userFavoriteFood: String = getUserFavoriteFood()
        if (userFavoriteFood == null) {
            askFavoriteFood()
        } else {
            setUserFavoriteFood(userFavoriteFood)
        }

        mImagePagerAdapter = ImagePagerAdapter(supportFragmentManager, IMAGE_INFOS)
        pager.adapter = mImagePagerAdapter

        var params = pager_tab_strip.layoutParams as ViewPager.LayoutParams
        params.isDecor = true

        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                recordImageView()
                recordScreenView()
            }
        })

        recordImageView()
    }

    override fun onResume() {
        super.onResume()
        recordScreenView()
    }

    private fun askFavoriteFood() {
        val choices: Array<String> = resources.getStringArray(R.array.food_items)
        var ad: AlertDialog = AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.food_dialog_title)
                .setItems(choices, DialogInterface.OnClickListener {
                    dialogInterface, i ->
                    var food: String = choices[i]
                    setUserFavoriteFood(food)
                }).create()

        ad.show()
    }

    private fun getUserFavoriteFood(): String {
        return PreferenceManager.getDefaultSharedPreferences(this)
                .getString(KEY_FAVORITE_FOOD, null)
    }

    private fun setUserFavoriteFood(food: String) {
        Log.d(TAG, "setFavoriteFood: " + food)
        mFavoriteFood = food

        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString(KEY_FAVORITE_FOOD, food)
                .apply()

        mFirebaseAnalytics?.setUserProperty("favorite_food", mFavoriteFood)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var i: Int = item.itemId
        if (i == R.id.menu_share) {
            var name: String = getCurrentItemTitle()
            var text: String = "I'd love you to hear about " + name

            var sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, text)
            sendIntent.type = "text/plain"
            startActivity(sendIntent)

            // [START custom_event]
            var params = Bundle()
            params.putString("image_name", name)
            params.putString("full_text", text)
            mFirebaseAnalytics!!.logEvent("share_image", params)
        }
        return false
    }

    private fun getCurrentItemTitle(): String {
        var position = pager.currentItem
        var info: ImageInfoKt = IMAGE_INFOS[position]
        return getString(info.title)
    }

    private fun getCurrentImageId(): String {
        var position = pager.currentItem
        var info = IMAGE_INFOS[position]
        return getString(info.id)
    }

    private fun recordImageView() {
        var id = getCurrentImageId();
        var name = getCurrentItemTitle()

        // [START image_view_event]
        var bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id)
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image")
        mFirebaseAnalytics!!.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
        // [END image_view_event]
    }

    private fun recordScreenView() {
        var screenName = getCurrentImageId() + "-" + getCurrentItemTitle()

        // [START set_current_screen]
        mFirebaseAnalytics!!.setCurrentScreen(this, screenName, null /* class override */)
        // [END set_current_screen]
    }

    inner class ImagePagerAdapter(fm: FragmentManager?, val infos: Array<ImageInfoKt>) : FragmentPagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            var info: ImageInfoKt = infos[position]
            return ImageFragmentKt.newInstance(info.image)
        }

        override fun getCount(): Int {
            return infos.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            if (position < 0 || position >= infos.size) {
                return null
            }
            var l = Locale.getDefault()
            var info = infos[position]
            return getString(info.title).toUpperCase()
        }
    }


    companion object {
        private val TAG: String = "MainActivity"
        private val KEY_FAVORITE_FOOD = "favorite_food"

        val IMAGE_INFOS = arrayOf(
                ImageInfoKt(R.drawable.favorite, R.string.pattern1_title, R.string.pattern1_id),
                ImageInfoKt(R.drawable.flash, R.string.pattern2_title, R.string.pattern2_id),
                ImageInfoKt(R.drawable.face, R.string.pattern3_title, R.string.pattern3_id),
                ImageInfoKt(R.drawable.whitebalance, R.string.pattern4_title, R.string.pattern4_id))
    }
}
