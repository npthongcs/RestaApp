package com.anderson.restaapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anderson.restaapp.R


class SelectTimeFragment : Fragment(R.layout.fragment_select_time) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d("SelectTimeFragment","123")
        return inflater.inflate(R.layout.fragment_select_time, container, false)
    }

}