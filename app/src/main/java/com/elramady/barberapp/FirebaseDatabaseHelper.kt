package com.elramady.barberapp

import android.content.Context
import android.util.Log
import com.elramady.barberapp.fragments.Booking.BookingFragment
import com.elramady.barberapp.models.CustomersBooking
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class FirebaseDatabaseHelper(var context: Context,var list: ArrayList<CustomersBooking>){

    lateinit var fbDatabase:FirebaseDatabase
  lateinit  var dbRef:DatabaseReference

      lateinit  var listBookings:ArrayList<CustomersBooking>




    fun intializeFirebase(key: String){
        FirebaseApp.initializeApp(context)
        fbDatabase= FirebaseDatabase.getInstance()
        dbRef=fbDatabase.getReference(key)
    }




    fun readBookings(){
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
//                listBookings.clear()
//                var bookingsKey=ArrayList<String>()
listBookings=ArrayList<CustomersBooking>()
                if (snapshot.exists()){
                    for (data in snapshot.children){
//                    bookingsKey.add(keyNode.key.toString())
                        var customerBooking=data.getValue(CustomersBooking::class.java)

                        listBookings.add(customerBooking!!)

                    }

                }
                list=listBookings

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    }



}