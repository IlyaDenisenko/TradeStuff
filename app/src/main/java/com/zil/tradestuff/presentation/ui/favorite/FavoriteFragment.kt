package com.zil.tradestuff.presentation.ui.favorite

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zil.tradestuff.R


class FavoriteFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("marco", "fav onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.i("marco", "fav onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.i("marco", "fav onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("marco", "fav onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("marco", "fav onDestroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

}