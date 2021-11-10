package com.elramady.barberapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elramady.barberapp.R
import com.elramady.barberapp.databinding.ImageItemBinding
import com.elramady.barberapp.databinding.ServicesItemBinding
import com.elramady.barberapp.models.Services

class ServiceAdapter(val context: Context):RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    var listService=ArrayList<Services>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceAdapter.ServiceViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        val binding : ServicesItemBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.services_item,parent,false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceAdapter.ServiceViewHolder, position: Int) {
        val service: Services =listService[position]
        holder.bind(service,context)
    }

    override fun getItemCount(): Int {
        return listService.size

    }

    fun setList(list: ArrayList<Services>) {
        this.listService= list
        notifyDataSetChanged()

    }


    inner class ServiceViewHolder(val binding: ServicesItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(service: Services, context: Context) {
            binding.serviceNameItem.text=service.service_name
            binding.servicePriceItem.text=service.service_price
        }

    }
}