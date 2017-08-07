package com.google.firebase.quickstart.analytics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created by soul on 2017. 7. 23..
 */
class ImageFragmentKt : Fragment() {

    var resId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            resId = arguments.getInt(ARG_PATTERN)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view :View = inflater!!.inflate(R.layout.fragment_main, null)
        var imageView = view.findViewById(R.id.imageView) as ImageView
        imageView.setImageResource(resId)

        return view;
    }

    companion object {
        private val ARG_PATTERN : String = "pattern"

        fun newInstance(resId:Int) : ImageFragmentKt{
            var fragment : ImageFragmentKt = ImageFragmentKt()
            var args:Bundle = Bundle()
            args.putInt(ARG_PATTERN, resId)
            fragment.arguments = args
            return fragment
        }

    }

}