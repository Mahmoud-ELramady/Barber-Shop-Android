package com.elramady.barberapp.fragments.settings

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.barberapp.R
import com.elramady.barberapp.adapters.ServiceAdapter
import com.elramady.barberapp.databinding.FragmentSettingsBinding
import com.elramady.barberapp.fragments.bottomsheet.BottomSheetDaysFragment
import com.elramady.barberapp.fragments.bottomsheet.BottomSheetDaysFragment.Companion.listDays
import com.elramady.barberapp.models.Services
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class SettingsFragment : Fragment() {

    private var validate: Int=0
    lateinit var binding:FragmentSettingsBinding


lateinit var bottomSheet:BottomSheetDialog

lateinit var nameServiceEd:TextInputEditText
lateinit var priceServiceEd:TextInputEditText
lateinit var addServiceBtn:MaterialButton

lateinit var dbRef: DatabaseReference

var workHoursFrom:String="00:00"
var workHoursTo:String="00:00"
var barbersNumbers:String="work"

var serviceName:String=""
var servicePrice:String=""

lateinit var progressDialog: ProgressDialog
lateinit var listService:ArrayList<Services>

lateinit var adapter:ServiceAdapter

lateinit var pref:SharedPreferences

var minutes:String=""


    companion object{
        var hoursFromDb:Int=0
        var hoursToDb:Int=0
        var amPM:String=""

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         binding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)


        pref =requireActivity().getSharedPreferences("stored_time", AppCompatActivity.MODE_PRIVATE)


        bottomSheet= BottomSheetDialog(requireContext())
        bottomSheet.setContentView(R.layout.bottom_sheet_dialog)
        bottomSheet.setCanceledOnTouchOutside(false)

        nameServiceEd= bottomSheet.findViewById<TextInputEditText>(R.id.service_bottom_sheet_ed)!!
        priceServiceEd= bottomSheet.findViewById<TextInputEditText>(R.id.service_price_sheet_ed)!!
        addServiceBtn=bottomSheet.findViewById<MaterialButton>(R.id.btn_add_service)!!

        progressDialog= ProgressDialog(requireContext())

        listService=ArrayList<Services>()
        dbRef= FirebaseDatabase.getInstance().getReference().child("Barbers")


        displayInfo()
        displayServices()


        addServiceBtn.setOnClickListener {
            if (validation()==2){
                serviceName= nameServiceEd.text.toString()
                servicePrice= priceServiceEd.text.toString()

                storeServiceData(serviceName,servicePrice)
                bottomSheet.dismiss()

            }else{
                Toast.makeText(requireContext(),"please confirm that all fields not empty".toString(),Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnClockFrom.setOnClickListener {
            openTimePicker(1)
//            setTime(1)

        }

        binding.btnClockTo.setOnClickListener {
//            setTime(2)
            openTimePicker(2)

        }

        binding.tvSetDays.setOnClickListener {
         val fragment=BottomSheetDaysFragment()
        fragment.show(childFragmentManager,"Tag")
        }


        binding.floatingSetting.setOnClickListener{
            bottomSheet.show()

        }


//        storeDataTime()

        binding.numberPickerSetting.maxValue=50
        binding.numberPickerSetting.minValue=0
        binding.numberPickerSetting.value=3

        binding.numberPickerSetting.setOnValueChangedListener {
                picker, oldVal, newVal ->
            barbersNumbers= newVal.toString()

        }

        binding.btnSaveSettings.setOnClickListener {
            saveInformation()
        }



        return binding.root
    }



    private fun storeServiceData(serviceName: String, servicePrice: String) {
        progressDialog.setTitle("Adding service.....")
        progressDialog.show()

        var key=dbRef.push().key

        var map= mapOf<String,String>(
            "service_name" to "$serviceName",
            "service_price" to "$servicePrice"
        )


        dbRef.child("elrmady").child("services").child(key.toString()).updateChildren(map)
            .addOnSuccessListener(object :OnSuccessListener<Void>{
                override fun onSuccess(p0: Void?) {
                    progressDialog.dismiss()
                    Snackbar.make(requireView(),"service added", Snackbar.LENGTH_LONG).show()
                }
            })
    }


    fun displayServices(){
        adapter= ServiceAdapter(requireContext())
        binding.rvServicesSettings.adapter=adapter
        binding.rvServicesSettings.setHasFixedSize(true)
        var linear= LinearLayoutManager(requireContext())
        linear.stackFromEnd=true
        binding.rvServicesSettings.layoutManager=linear


        dbRef.child("elrmady").child("services").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listService.clear()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        val serviceid=data.key.toString()
                        val name=data.child("service_name").getValue().toString()
                        val price=data.child("service_price").getValue().toString()
                       var service=Services(name,price,serviceid)
                        listService.add(service)
                        adapter.setList(listService)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun validation():Int {
        validate = 0

        if (TextUtils.isEmpty(nameServiceEd.text)) {
            nameServiceEd.error = "Service name must not empty"
            validate = 0
        } else {
            nameServiceEd.error = null
            validate += 1
        }

        if (TextUtils.isEmpty(priceServiceEd.text)) {
            priceServiceEd.error = "service price must not empty"
            validate = 0

        } else {
            priceServiceEd.error = null
            validate += 1

        }

        return  validate
    }


    private fun saveInformation() {

        progressDialog.setTitle("Updating.....")
        progressDialog.show()

        val user = mapOf<String,String>(
            "work_hours" to "from $workHoursFrom to $workHoursTo",
            "barbers_workers" to "$barbersNumbers",

        )


        dbRef.child("elrmady").updateChildren(user).addOnSuccessListener(object :
            OnSuccessListener<Void> {
            override fun onSuccess(p0: Void?) {
                progressDialog.dismiss()
                Snackbar.make(requireView(),"Information Updated", Snackbar.LENGTH_LONG).show()
            }
        })

//        storeDataTime()

    }



    fun displayInfo(){
        dbRef.child("elrmady").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                workHoursFrom=""
                workHoursTo=""

                var workHours=""
                if (snapshot.exists()){

                    if (snapshot.child("work_hours").getValue()!=null){
                       workHours =snapshot.child("work_hours").getValue().toString()
                        workHoursFrom=workHours.replace("from ","")
                        workHoursFrom=workHoursFrom.replaceAfter("to","")
                        workHoursFrom=workHoursFrom.replace("to","")
                        workHoursTo=workHours.replaceBefore("to","")
                        workHoursTo=workHoursTo.replace("to ","")

                        binding.btnClockFrom.text=workHoursFrom
                        binding.btnClockTo.text=workHoursTo
                        binding.btnClockFrom.isEnabled=false
                        binding.btnClockTo.isEnabled=false


                    }else{
                        workHoursFrom="00:00"
                        workHoursTo="00:00"
                        binding.btnClockFrom.text=workHoursFrom
                        binding.btnClockTo.text=workHoursTo
                        binding.btnClockFrom.isEnabled=true
                        binding.btnClockTo.isEnabled=true

                    }



                    if (snapshot.child("barbers_workers").getValue()!=null){
                        barbersNumbers =snapshot.child("barbers_workers").getValue().toString()
                        binding.numberPickerSetting.value=barbersNumbers.toInt()
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }





    fun openTimePicker(button:Int){


        val isSystem24Hour= is24HourFormat(requireContext())
        val picker=MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Work Hours")
            .build()

        picker.show(childFragmentManager,"tag2")

        picker.addOnPositiveButtonClickListener {
            var hour=picker.hour

            if (hour>=12){
                if (hour>12){
                    hour=hour-12
                }
                amPM="PM"
            }else if (hour==0){
                hour=(hour+12)
                amPM="AM"
            }else{
                amPM="AM"

            }

            val min="00"

            var time="$hour:$min $amPM"

            if (button==1){

                binding.btnClockFrom.isEnabled=false
                    workHoursFrom=time
                    hoursFromDb=hour
                    pref.edit().putInt("hoursFromDb", hour).commit()
                Log.e("hour",hour.toString())
                    binding.btnClockFrom.text=workHoursFrom

                }else if (button==2){
                    binding.btnClockTo.isEnabled=false
                    workHoursTo=time
                    hoursToDb=hour
                pref.edit().putInt("hoursToDb", hour).commit()
                Log.e("hour2",hour.toString())
                binding.btnClockTo.text=workHoursTo
                }

            Toast.makeText(requireContext(),"$hour:$min $amPM",Toast.LENGTH_SHORT).show()
        }



    }



}