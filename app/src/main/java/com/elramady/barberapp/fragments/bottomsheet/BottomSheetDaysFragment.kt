package com.elramady.barberapp.fragments.bottomsheet

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.elramady.barberapp.R
import com.elramady.barberapp.databinding.FragmentBottomSheetDaysBinding
import com.elramady.barberapp.fragments.settings.SettingsFragment
import com.elramady.barberapp.fragments.settings.SettingsFragment.Companion.amPM
import com.elramady.barberapp.models.Days
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.*


@Suppress("UNREACHABLE_CODE")
class BottomSheetDaysFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentBottomSheetDaysBinding


    var day_1:Boolean=true
    var day_2:Boolean=true
    var day_3:Boolean=true
    var day_4:Boolean=true
    var day_5:Boolean=true
    var day_6:Boolean=true
    var day_7:Boolean=true

    lateinit var dbRef: DatabaseReference
    lateinit var listStore:ArrayList<String>
    lateinit var progressDialog: ProgressDialog
    lateinit var pref: SharedPreferences
     var getTimeFromShared:Int=0
     var getTimeToShared:Int=0
 lateinit var listShowDays:ArrayList<String>
    companion object {
        const val day1Name:String="Saturday"
        const val day2Name:String="Sunday"
        const val day3Name:String="Monday"
        const val day4Name:String="Tuesday"
        const val day5Name:String="Wednesday"
        const val day6Name:String="Thursday"
        const val day7Name:String="Friday"

         var listDays=ArrayList<String>()


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_sheet_days,container,false)

        dbRef= FirebaseDatabase.getInstance().getReference().child("Barbers")

        listShowDays=ArrayList<String>()
        listStore=ArrayList<String>()
        pref =requireActivity().getSharedPreferences("stored_time", AppCompatActivity.MODE_PRIVATE)

        progressDialog= ProgressDialog(requireContext())

//        listDays=ArrayList<String>()
        listDays.add(day1Name)
        listDays.add(day2Name)
        listDays.add(day3Name)
        listDays.add(day4Name)
        listDays.add(day5Name)
        listDays.add(day6Name)
        listDays.add(day7Name)


        showDays()
        onClickButtons()

    binding.btnSheetSaveDays.setOnClickListener {

        try {
            storeDataTime()


        }catch (e:Exception){
            throw e
        }

    }


        return binding.root
    }


  fun  showDays(){
      dbRef.child("elrmady").child("calender").addValueEventListener(object :ValueEventListener{
          override fun onDataChange(snapshot: DataSnapshot) {
              listShowDays.clear()
              if (snapshot.exists()){
                  setBooleanforAll(1)
                  setColorsForAll(1)

                  for (data in snapshot.children) {
                      var day = data.key.toString()
                      listShowDays.add(day)
                  }


                  for (data in listShowDays) {

                      if (data == day1Name) {
                          binding.btnSheetDay1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay1.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_1 = true
                      }

                      if (data == day2Name) {
                          binding.btnSheetDay2.setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                  requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay2.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_2 = true
                      }

                      if (data == day3Name) {
                          binding.btnSheetDay3.setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                  requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay3.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_3 = true
                      }

                      if (data == day4Name) {
                          binding.btnSheetDay4.setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                  requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay4.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_4 = true
                      }

                      if (data == day5Name) {
                          binding.btnSheetDay5.setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                  requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay5.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_5 = true
                      }

                      if (data == day6Name) {
                          binding.btnSheetDay6.setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                  requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay6.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_6 = true
                      }

                      if (data == day7Name) {
                          binding.btnSheetDay7.setBackgroundTintList(
                              ContextCompat.getColorStateList(
                                  requireContext(),
                                  R.color.white
                              )
                          )
                          binding.btnSheetDay7.setTextColor(
                              AppCompatResources.getColorStateList(
                                  requireContext(),
                                  R.color.bottom_sheet_text_color
                              )
                          )
                          continue
                          day_7 = true
                      }


                  }

              }else{
                  setBooleanforAll(2)
                  setColorsForAll(2)

              }
          }

          override fun onCancelled(error: DatabaseError) {
          }
      })
  }


    fun setBooleanforAll(i:Int){

        if (i==1){
            var day_1:Boolean=false
            var day_2:Boolean=false
            var day_3:Boolean=false
            var day_4:Boolean=false
            var day_5:Boolean=false
            var day_6:Boolean=false
            var day_7:Boolean=false

        }else if (i==2){
            var day_1:Boolean=true
            var day_2:Boolean=true
            var day_3:Boolean=true
            var day_4:Boolean=true
            var day_5:Boolean=true
            var day_6:Boolean=true
            var day_7:Boolean=true

        }


    }


    fun setColorsForAll(i:Int){
        if (i==1){
            binding.btnSheetDay1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay1.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            binding.btnSheetDay2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay2.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            binding.btnSheetDay3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay3.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            binding.btnSheetDay4.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay4.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            binding.btnSheetDay5.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay5.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            binding.btnSheetDay6.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay6.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            binding.btnSheetDay7.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
            binding.btnSheetDay7.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

        } else if (i==2){
            binding.btnSheetDay1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay1.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            binding.btnSheetDay2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay2.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            binding.btnSheetDay3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay3.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            binding.btnSheetDay4.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay4.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            binding.btnSheetDay5.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay5.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            binding.btnSheetDay6.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay6.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            binding.btnSheetDay7.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
            binding.btnSheetDay7.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

        }





    }


    private fun onClickButtons() {
        binding.btnSheetDay1.setOnClickListener {OnClickButtonDays(1) }
        binding.btnSheetDay2.setOnClickListener {OnClickButtonDays(2) }
        binding.btnSheetDay3.setOnClickListener {OnClickButtonDays(3) }
        binding.btnSheetDay4.setOnClickListener {OnClickButtonDays(4) }
        binding.btnSheetDay5.setOnClickListener {OnClickButtonDays(5) }
        binding.btnSheetDay6.setOnClickListener {OnClickButtonDays(6) }
        binding.btnSheetDay7.setOnClickListener {OnClickButtonDays(7) }

    }

        private fun OnClickButtonDays(dayNumber:Int) {
        when(dayNumber){
            1-> {
                doChangesButton(1)
            }
            2-> { doChangesButton(2) }
            3-> { doChangesButton(3) }
            4-> { doChangesButton(4)  }
            5-> { doChangesButton(5)  }
            6-> { doChangesButton(6) }
            7-> { doChangesButton(7) }
        }
    }


    private fun storeDataTime() {
//        progressDialog.setTitle("Save Settings.....")
//        progressDialog.show()
        dbRef.child("elrmady").child("calender").setValue(null)



        var map=mapOf<String,String>(
            "name" to "Mahmoud",
            "service" to "شعر وذقن",
            "price" to "25 EGP",
            "status" to "new"
        )
        Log.e("listStore", listDays.toString())
        getTimeFromShared=pref.getInt("hoursFromDb",0)
        getTimeToShared=pref.getInt("hoursToDb",0)
        Log.e("getTimeFromShared", getTimeFromShared.toString())
        Log.e("getTimeToShared", getTimeToShared.toString())

        for (day in listDays){
            for (hour in getTimeFromShared..getTimeToShared){

                dbRef.child("elrmady").child("calender").child(day).child("$hour:00 $amPM")
                    .updateChildren(map).addOnSuccessListener(object : OnSuccessListener<Void> {
                        override fun onSuccess(p0: Void?) {
                            Toast.makeText(requireContext(),"done",Toast.LENGTH_SHORT).show()
                        }
                    })
            }

        }
//        progressDialog.dismiss()

//        SettingsFragment.hoursFromDb =0
//        SettingsFragment.hoursToDb =0

    }




    @SuppressLint("ResourceAsColor")
    private fun doChangesButton(n: Int) {
        if (n==1){
            if (day_1){
                day_1=false
                if (listDays.contains(day1Name)){
                    listDays.remove(day1Name)
                }

                binding.btnSheetDay1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500));
                binding.btnSheetDay1.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            }else{
                day_1=true
                if (!listDays.contains(day1Name)){
                    listDays.add(day1Name)
                }

                binding.btnSheetDay1.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay1.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))
            }
        }


        if (n==2){
            if (day_2){
                day_2=false

                if (listDays.contains(day2Name)){
                    listDays.remove(day2Name)
                }


                binding.btnSheetDay2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500))
                binding.btnSheetDay2.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            }else{
                day_2=true
                if (!listDays.contains(day2Name)){
                    listDays.add(day2Name)
                }

                binding.btnSheetDay2.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay2.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))
            }
        }


        if (n==3){
            if (day_3){
                day_3=false
                if (listDays.contains(day3Name)){
                    listDays.remove(day3Name)
                }


                binding.btnSheetDay3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500))
                binding.btnSheetDay3.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))
            }else{
                day_3=true

                if (!listDays.contains(day3Name)){
                    listDays.add(day3Name)
                }

                binding.btnSheetDay3.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay3.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            }
        }


        if (n==4){
            if (day_4){
                day_4=false
                if (listDays.contains(day4Name)){
                    listDays.remove(day4Name)
                }

                binding.btnSheetDay4.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500))
                binding.btnSheetDay4.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))
            }else{
                day_4=true

                if (!listDays.contains(day4Name)){
                    listDays.add(day4Name)
                }

                binding.btnSheetDay4.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay4.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))
            }
        }


        if (n==5){
            if (day_5){

                day_5=false

                if (listDays.contains(day5Name)){
                    listDays.remove(day5Name)
                }
                binding.btnSheetDay5.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500))
                binding.btnSheetDay5.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            }else{
                day_5=true
                if (!listDays.contains(day5Name)){
                    listDays.add(day5Name)
                }


                listDays.add(4,day5Name)
                binding.btnSheetDay5.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay5.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            }
        }

        if (n==6){
            if (day_6){
                day_6=false
                if (listDays.contains(day6Name)){
                    listDays.remove(day6Name)
                }


                binding.btnSheetDay6.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500))
                binding.btnSheetDay6.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            }else{
                day_6=true
                if (!listDays.contains(day6Name)){
                    listDays.add(day6Name)
                }

                binding.btnSheetDay6.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay6.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            }
        }

        if (n==7){
            if (day_7){
                day_7=false
                if (listDays.contains(day7Name)){
                    listDays.remove(day7Name)
                }

                binding.btnSheetDay7.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.purple_500))
                binding.btnSheetDay7.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.white))

            }else{
                day_7=true

                if (!listDays.contains(day7Name)){
                    listDays.add(day7Name)
                }
                binding.btnSheetDay7.setBackgroundTintList(ContextCompat.getColorStateList(requireContext(), R.color.white));
                binding.btnSheetDay7.setTextColor(AppCompatResources.getColorStateList(requireContext(),R.color.bottom_sheet_text_color))

            }
        }

        Log.e("days",listDays.toString())

    }




}