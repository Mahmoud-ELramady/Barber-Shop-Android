package com.elramady.barberapp.fragments.Booking

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent.ACTION_DOWN
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.ViewPager
import com.elramady.barberapp.R
import com.elramady.barberapp.adapters.SectionsPagerAdapter
import com.elramady.barberapp.databinding.FragmentReservationBinding
import com.google.android.material.tabs.TabLayout


class ReservationFragment(var fm:FragmentManager) : Fragment() {

    lateinit var binding: FragmentReservationBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_reservation, container, false)

        val sectionsPagerAdapter = SectionsPagerAdapter(requireActivity(), childFragmentManager)
        val viewPager: ViewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        tabs.setupWithViewPager(viewPager)


        // Inflate the layout for this fragment
        return binding.root
    }




}