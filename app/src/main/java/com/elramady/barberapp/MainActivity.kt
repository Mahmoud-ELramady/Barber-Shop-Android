package com.elramady.barberapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.elramady.barberapp.adapters.MenuBottomAdapter
import com.elramady.barberapp.fragments.Booking.ReservationFragment

import com.elramady.barberapp.fragments.profile.ProfileFragment
import com.elramady.barberapp.fragments.settings.SettingsFragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {
    lateinit var mbn: MeowBottomNavigation
    lateinit var viewPager: ViewPager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(baseContext)
        mbn=findViewById(R.id.mbn_main)
        viewPager=findViewById(R.id.view_pager)


        val menuAdapter= MenuBottomAdapter(initFragments(),supportFragmentManager)
        viewPager.adapter=menuAdapter

        mbn.show(1,true)

        mbn.add(MeowBottomNavigation.Model(1,R.drawable.reserve_icon))
        mbn.add(MeowBottomNavigation.Model(2,R.drawable.user_icon))
        mbn.add(MeowBottomNavigation.Model(3,R.drawable.settings_icon))

        mbn.setOnClickMenuListener{
                model ->
            when(model.id){


                1-> viewPager.currentItem=0
                2-> viewPager.currentItem=1
                3-> viewPager.currentItem=2
//

            }
        }

        viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                mbn.show(position+1,true)
            }


            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }










    private fun initFragments():ArrayList<Fragment>{
        return arrayListOf(
            ReservationFragment(supportFragmentManager),
            ProfileFragment(),
            SettingsFragment()
        )
    }

}