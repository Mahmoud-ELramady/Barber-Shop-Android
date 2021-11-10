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
import com.elramady.barberapp.FirebaseDatabaseHelper
import com.elramady.barberapp.R
import com.elramady.barberapp.adapters.BookingsAdapters
import com.elramady.barberapp.databinding.FragmentBookingBinding
import com.elramady.barberapp.databinding.FragmentDoneBookingBinding
import com.elramady.barberapp.models.CustomersBooking
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*

class BookingFragment : Fragment(),BookingsAdapters.ClickListner {
    lateinit var binding: FragmentBookingBinding

    lateinit var firebaseDatabaseHelper: FirebaseDatabaseHelper

    lateinit var listBooking: ArrayList<CustomersBooking>


    lateinit var adapter:BookingsAdapters


    lateinit var dbRef:DatabaseReference
    lateinit var list: ArrayList<CustomersBooking>



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_booking, container, false)

        FirebaseApp.initializeApp(requireContext())
        dbRef=FirebaseDatabase.getInstance().getReference().child("Barbers")



        list=ArrayList<CustomersBooking>()
        adapter= BookingsAdapters(requireContext(),this)
        binding.rvBooking.adapter=adapter
        binding.rvBooking.setHasFixedSize(true)
        binding.rvBooking.layoutManager=LinearLayoutManager(requireContext())


        getBookingsEqualNew()


//        listBooking=ArrayList<CustomersBooking>()
//
//        firebaseDatabaseHelper=FirebaseDatabaseHelper(requireContext(),listBooking)
//        firebaseDatabaseHelper.intializeFirebase("CustomersBookings")
//        firebaseDatabaseHelper.readBookings()
//
//        adapter= BookingsAdapters(requireContext())
//
////        adapter.setList()
//        binding.rvBooking.adapter=adapter
//        binding.rvBooking.layoutManager=LinearLayoutManager(requireContext())
//        binding.rvBooking.setHasFixedSize(true)
//
//
//
//
//        Log.e("list",listBooking.toString())
//
////
////
////

        return binding.root
    }


    fun getBookingsEqualNew(){
         list.clear()

        dbRef.child("elrmady").child("calender").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (data in snapshot.children){

                        Log.e("data",data.key.toString())

                        dbRef.child("elrmady").child("calender").child(data.key.toString()).orderByChild("status").equalTo("new").addValueEventListener(object :ValueEventListener{
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





    override fun onItemClickConfirm(s: String,position: Int) {
        if (s=="new"){
            updateItemStatus(s,position)
        }


    }

    private fun updateItemStatus(s: String,position: Int) {
        val user = mapOf<String,String>(
            "status" to "done",
        )


        dbRef.child("elrmady").child("calender").child(s).updateChildren(user).addOnSuccessListener {
            Toast.makeText(requireContext(),"updated",Toast.LENGTH_SHORT).show()
//            getBookingsEqualNew()

        }.addOnFailureListener {
//            Toast.makeText(requireContext(),"updated",Toast.LENGTH_SHORT).show()
        }

    }


}