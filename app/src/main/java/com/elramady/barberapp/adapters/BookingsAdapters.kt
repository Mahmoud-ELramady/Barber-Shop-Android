package com.elramady.barberapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.barberapp.R
import com.elramady.barberapp.databinding.CustomerReservationItemBinding
import com.elramady.barberapp.models.CustomersBooking

class BookingsAdapters(val context: Context,var listner:ClickListner):RecyclerView.Adapter<BookingsAdapters.BookingsViewHolder>() {
    var bookingList= ArrayList<CustomersBooking>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : CustomerReservationItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.customer_reservation_item,parent,false)
        return BookingsViewHolder(binding)

    }

    override fun onBindViewHolder(holder: BookingsViewHolder, position: Int) {
        val booking:CustomersBooking =bookingList[position]
        holder.bind(booking,context)
    }

    override fun getItemCount(): Int {
       return bookingList.size
    }
    fun setList(listBooking: ArrayList<CustomersBooking>) {
        this.bookingList= listBooking
        notifyDataSetChanged()

    }


    inner class BookingsViewHolder(val binding:CustomerReservationItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(booking: CustomersBooking, context: Context) {
            binding.cutomerNameItem.text=booking.name.toString()
            binding.cutomerServiceItem.text=booking.service.toString()
            binding.cutomerPriceItem.text=booking.price.toString()
            binding.cutomerDateItem.text=booking.date.toString()
            binding.cutomerTimeItem.text=booking.time.toString()


            binding.customerConfirmItem.setOnClickListener {
                listner.onItemClickConfirm(booking.status.toString(),position)

            }
        }

    }


    interface ClickListner{
        fun onItemClickConfirm(s:String,position: Int)
    }
}