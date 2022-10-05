package com.zil.tradestuff.ui.publication

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.dao.AppDatabase
import com.zil.tradestuff.dao.ThingDAO
import com.zil.tradestuff.TakePicsFromGalleryContract
import com.zil.tradestuff.adapter.PhotoPublicationRecyclerAdapter
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.databinding.FragmentPublicationBinding
import com.zil.tradestuff.model.DataMessage
import com.zil.tradestuff.model.StaffModel
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.dashboard.DashboardFragment
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.thread

class PublicationFragment : Fragment() {

    private  var _binding : FragmentPublicationBinding? = null
    private lateinit var viewModel: PublicationViewModel
    private lateinit var thingViewModel: ThingViewModel
    private val binding get() = _binding!!

    private val listPhotoUri : MutableList<Uri> = mutableListOf()
    lateinit var uri : Uri
    lateinit var thingModel : ThingModel
    lateinit var storageReference: StorageReference

    lateinit var descriptionEditText: EditText

    // Обработка выходных данных после возврата из галереи
    val galleryActivityLaunch = registerForActivityResult(TakePicsFromGalleryContract()){ result ->

        val count = result.clipData!!.itemCount
        for (n in 0 until count){
            uri = result.clipData!!.getItemAt(n).uri
            listPhotoUri.add(uri)
        }

        initRecyclerPhoto(listPhotoUri)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        descriptionEditText = binding.nameEditText

        val storage = FirebaseStorage.getInstance()
        storageReference = storage.getReferenceFromUrl("gs://tradestuff-b1fb3.appspot.com")

        pressedAddPhoto()
        clickPublishThing()

        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PublicationViewModel::class.java)
        thingViewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun createIdForDatabase(): Int{
        var id = 0
        return id
    }

   private fun pressedAddPhoto(){
       val but = binding.addPhotoImage
       but.setOnClickListener {
           val intent = Intent(Intent.ACTION_PICK)
           intent.type = "image/*"
           intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

           galleryActivityLaunch.launch(intent)
       }
   }

    private fun clickPublishThing() {
        val butPublish = binding.publicationButton
        butPublish.setOnClickListener() {
            val name = descriptionEditText.text.toString()
            val date = Date()


            thingModel = ThingModel(images = listPhotoUri, name =  name, date = date)
            thread {
              // thingViewModel.insertThing(thingModel)
           }
            FirebaseFirestore.getInstance()
                .collection("Publication").document(FirebaseAuth.getInstance().uid!!).collection("Things").document()
                .set(thingModel)
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, DashboardFragment())
                .commit()
            var user = FirebaseAuth.getInstance().currentUser
            Log.i("User: ", user?.uid.toString() + " " + user?.email + " " + user?.phoneNumber)
        }
     //   TODO("Написать свою логику присваивания ID, конвектировать данные при вставки в firebase")
       }

    private fun initRecyclerPhoto(photos : List<Uri>){
        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = PhotoPublicationRecyclerAdapter(photos)
    }

    fun backButtonPressed(){
        childFragmentManager.beginTransaction().remove(this).commit()
    }
}