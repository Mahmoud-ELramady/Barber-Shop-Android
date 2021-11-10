package com.elramady.barberapp.fragments.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.elramady.barberapp.R
import com.elramady.barberapp.adapters.BookingsAdapters
import com.elramady.barberapp.adapters.FeedbackAdapter
import com.elramady.barberapp.adapters.ImageAdapter
import com.elramady.barberapp.databinding.FragmentBookingBinding
import com.elramady.barberapp.databinding.FragmentProfileBinding
import com.elramady.barberapp.models.Barber
import com.elramady.barberapp.models.CustomersBooking
import com.elramady.barberapp.models.Feedback
import com.elramady.barberapp.models.Images
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import java.util.*
import javax.xml.validation.Validator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class ProfileFragment : Fragment() {

    private  val  GALLERY_CODE: Int=1
    var imageUri: Uri ?=null
    lateinit var binding: FragmentProfileBinding

    lateinit var listFeedback:ArrayList<Feedback>

    lateinit var nameProfile:TextInputEditText
    lateinit var descriptionProfile:TextInputEditText
    lateinit var addressProfile:TextInputEditText
    lateinit var phoneProfile:TextInputEditText

    lateinit var adapterFeedback: FeedbackAdapter

    lateinit var addPhoto:ImageView

    val REQUESTED_CODE:Int=1
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient

    var validate:Int=0


    lateinit var dbRef: DatabaseReference
    lateinit var mStorage: FirebaseStorage
    lateinit var progressDialog:ProgressDialog

    lateinit var listImages: ArrayList<String>
    lateinit var adapter:ImageAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)

        listFeedback=ArrayList<Feedback>()
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(requireActivity())
        dbRef= FirebaseDatabase.getInstance().getReference().child("Barbers")
        listImages=ArrayList<String>()


        displayImages()

        displayInformation()

        displayFeedback()


        mStorage= FirebaseStorage.getInstance()

        progressDialog= ProgressDialog(requireContext())

        nameProfile=binding.infoNameProfileEd
        descriptionProfile=binding.infoDescProfileEd
        addressProfile=binding.infoLocationProfileEd
        phoneProfile=binding.infoPhoneProfileEd
        addPhoto=binding.addPhoto

        addressProfile.setOnClickListener {
            getCurrentLocation()
        }




        binding.btnSaveSettingsProfile.setOnClickListener {
            if (validation()==4){
                saveInformation()
            }else{
                Toast.makeText(requireContext(),"please confirm that all fields not empty".toString(),Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(),validate.toString(),Toast.LENGTH_SHORT).show()


            }

        }


        addPhoto.setOnClickListener{
            selectPhoto()
        }



        return binding.root
    }

    private fun displayFeedback() {
        adapterFeedback= FeedbackAdapter(requireContext())
        binding.rvFeedback.adapter=adapter
        binding.rvFeedback.setHasFixedSize(true)
        var linear=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        linear.reverseLayout=true
        linear.stackFromEnd=true

        binding.rvFeedback.layoutManager=linear



        listFeedback.clear()
        dbRef.child("elrmady").child("feedback").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    for (data in snapshot.children){
                        var key=data.key
                        listFeedback.add(data.getValue(Feedback::class.java)!!)

                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun saveInformation() {

        progressDialog.setTitle("Updating.....")
        progressDialog.show()

        val user = mapOf<String,String>(
            "name" to nameProfile.text.toString(),
            "description" to descriptionProfile.text.toString(),
            "address" to addressProfile.text.toString(),
            "phone" to phoneProfile.text.toString(),
        )



        dbRef.child("elrmady").updateChildren(user).addOnSuccessListener(object :OnSuccessListener<Void>{
            override fun onSuccess(p0: Void?) {
                progressDialog.dismiss()
                Snackbar.make(requireView(),"Information Updated",Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun displayInformation() {
        dbRef.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for(data in snapshot.children){
                        val barber= data.getValue(Barber::class.java)
                        nameProfile.setText(barber!!.name)
                        descriptionProfile.setText(barber!!.description)
                        phoneProfile.setText(barber!!.phone)
                        addressProfile.setText(barber!!.address)

                    }
                }

                }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun displayImages() {

        adapter= ImageAdapter(requireContext())
        binding.rvProfile.adapter=adapter
        binding.rvProfile.setHasFixedSize(true)
        var linear=LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        linear.reverseLayout=true
        linear.stackFromEnd=true

        binding.rvProfile.layoutManager=linear


        dbRef.child("elrmady").child("images").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listImages.clear()
                if (snapshot.exists()){
                    for (data in snapshot.children){
                        val image= data.getValue(String::class.java)
                        listImages.add(image.toString())



                    }
                    adapter.setList(listImages)
                    adapter.notifyDataSetChanged()
                }

                }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun insertPhoto() {
        if (imageUri!=null){
            progressDialog.setTitle("Uploading.....")
            progressDialog.show()

            var filePath=mStorage.getReference().child("barbersImages").child(imageUri!!.lastPathSegment.toString())
            filePath.putFile(imageUri!!).addOnSuccessListener(object :OnSuccessListener<UploadTask.TaskSnapshot>{
                override fun onSuccess(taskSnapShot: UploadTask.TaskSnapshot?) {
                    var downloadUrl=taskSnapShot!!.storage.downloadUrl.addOnCompleteListener(object :OnCompleteListener<Uri>{
                        override fun onComplete(p0: Task<Uri>) {
                            var map=HashMap<String,String>()
                            map.put(dbRef.push().key!!,p0.getResult().toString())

                            dbRef.child("elrmady").child("images").updateChildren(map as Map<String, Any>).
                            addOnCompleteListener(object :OnCompleteListener<Void>{
                                override fun onComplete(p0: Task<Void>) {
                                    Snackbar.make(requireView(),"Image Uploaded",Snackbar.LENGTH_LONG).show()
                                    progressDialog.dismiss()
                                    map.clear()
                                }
                            })
                        }
                    })
                }
            }).addOnProgressListener(object :OnProgressListener<UploadTask.TaskSnapshot>{
                override fun onProgress(snapshot: UploadTask.TaskSnapshot) {
                    var progressPercent=(100.00*snapshot.bytesTransferred/snapshot.totalByteCount)
                    progressDialog.setMessage("Progress:${progressPercent.toInt()}%")
                }
            })


        }
    }

    private fun selectPhoto() {
        var intent=Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        startActivityForResult(intent,GALLERY_CODE)
    }

    // validation for fields and return 4  if all not empty
    private fun validation():Int {
        validate=0

        if (TextUtils.isEmpty(nameProfile.text)){
            nameProfile.error="name must not empty"
            validate=0
        }else{
            nameProfile.error=null
            validate+=1
        }

        if (TextUtils.isEmpty(descriptionProfile.text)){
            descriptionProfile.error="description must not empty"
            validate=0

        }else{
            descriptionProfile.error=null
            validate+=1

        }

         if (TextUtils.isEmpty(addressProfile.text)){
            addressProfile.error="location must not empty"
             validate=0
         }else{
             addressProfile.error=null
             validate+=1

         }


        if (TextUtils.isEmpty(phoneProfile.text)){
            phoneProfile.error="phone must not empty"
            validate=0

        }else{
            phoneProfile.error=null
            validate+=1

        }

        return validate

    }


    fun getCurrentLocation(){
        if (checkPermission()){
            getLocation()
        }else{
            checkPermission()
        }

    }

    private fun checkPermission():Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return true
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUESTED_CODE)

                return false
            }

        }
        return true
    }

    private fun getLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(object : OnCompleteListener<Location>{
            override fun onComplete(task: Task<Location>) {
                var location=task.result
                if (location!=null){
                    try {
                        var gecoder=Geocoder(requireActivity(), Locale.getDefault())
                        val address:List<Address> = gecoder.getFromLocation(
                         location.latitude,location.longitude,1
                     )
                    addressProfile.setText(address.get(0).getAddressLine(0))
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }
        })

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==GALLERY_CODE && resultCode==RESULT_OK){
            imageUri=data?.data
            insertPhoto()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUESTED_CODE ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(requireContext(), "Permission is allowed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(requireContext(), "Permission is denied", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }



}