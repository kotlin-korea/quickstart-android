package com.google.firebase.quickstart.analytics

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

class MainActivityKt : AppCompatActivity() {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each image.
     * This uses a {@link FragmentPagerAdapter}, which keeps every loaded fragment in memory.
     */
    private var mImagePagerAdapter : ImagePagerAdapter;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_kt)
    }



    companion object {
        private val TAG:String = "MainActivity"
        private val KEY_FAVORITE_FOOD = "favorite_food"

        val IMAGE_INFOS = arrayOf(
                ImageInfo(R.drawable.favorite, R.string.pattern1_title, R.string.pattern1_id),
                ImageInfo(R.drawable.flash, R.string.pattern2_title, R.string.pattern2_id),
                ImageInfo(R.drawable.face, R.string.pattern3_title, R.string.pattern3_id),
                ImageInfo(R.drawable.whitebalance, R.string.pattern4_title, R.string.pattern4_id))
    }

    inner class ImagePagerAdapter(fm: FragmentManager?, val infos: Array<ImageInfoKt>) : FragmentPagerAdapter(fm) {


        override fun getItem(position: Int): Fragment {
            var info : ImageInfoKt = infos[position]
            return ImageFragmentKt.newInstance(info.)
        }

        override fun getCount(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
