package com.anderson.restaapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anderson.restaapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_location, container, false)

        val supportMapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.idMap) as SupportMapFragment
        supportMapFragment.getMapAsync {
            val coordinates = LatLng(1.289417, 103.856872)
            val marker = MarkerOptions().position(coordinates).title("Makansutra Gluttons Bay")
            it.addMarker(marker)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates,17f),2000,null)
        }

        return view
    }
}