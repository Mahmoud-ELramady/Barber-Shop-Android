package com.elramady.barberapp.fragments.Booking

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.barberapp.R
import com.elramady.barberapp.adapters.BookingsAdapters
import com.elramady.barberapp.databinding.FragmentDoneBookingBinding
import com.elramady.barberapp.databinding.FragmentReservationBinding
import com.elramady.barberapp.models.CustomersBooking
import com.google.firebase.database.*


class DoneBookingFragment : Fragment() ,BookingsAdapters.ClickListner{
    lateinit var binding: FragmentDoneBookingBinding


    lateinit var adapter: BookingsAdapters


    lateinit var dbRef: DatabaseReference
    lateinit var list: ArrayList<CustomersBooking>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_done_booking, container, false)

        dbRef= FirebaseDatabase.getInstance().getReference("Barbers")



        list=ArrayList<CustomersBooking>()
        adapter= BookingsAdapters(requireContext(),this)
        binding.rvDoneBooking.adapter=adapter
        binding.rvDoneBooking.setHasFixedSize(true)
        binding.rvDoneBooking.layoutManager= LinearLayoutManager(requireContext())


        getBookingsEqualNew()


        return binding.root
    }


    fun getBookingsEqualNew(){
        list.clear()

        dbRef.child("elrmady").child("calender").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){

                        Log.e("data",data.key.toString())

                        dbRef.child("elrmady").child("calender").child(data.key.toString()).orderByChild("status").equalTo("done").addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {

                                if (snapshot.exists()){
                                    for (data in snapshot.children){

                                        val book= data.getValue(CustomersBooking::class.java)
                                        list.add(book!!)
                                    }
                                    adapter.setList(list)
                                    adapter.notifyDataSetChanged()

                                }


                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })





                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })



    }


    override fun onItemClickConfirm(s: String, position: Int) {

    }


}