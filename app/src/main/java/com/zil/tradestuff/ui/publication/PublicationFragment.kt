package com.zil.tradestuff.ui.publication

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zil.tradestuff.R
import com.zil.tradestuff.TakePicsFromGalleryContract
import com.zil.tradestuff.adapter.PhotoPublicationRecyclerAdapter
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.databinding.FragmentPublicationBinding
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.dashboard.DashboardFragment
import java.util.*

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
    lateinit var addPhotoButton: Button
    lateinit var publishButton: Button

    // Обработка выходных данных после возврата из галереи
    val galleryActivityLaunch = registerForActivityResult(TakePicsFromGalleryContract()){ result ->

        val count = result?.clipData?.itemCount
        if(count != null) {
            for (n in 0 until count!! ) {
                uri = result?.clipData!!.getItemAt(n).uri
                listPhotoUri.add(uri)
            }
            initRecyclerPhoto(listPhotoUri)
        } else {
            result?.describeContents()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPublicationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val storage = FirebaseStorage.getInstance()
        storageReference = storage.getReferenceFromUrl("gs://tradestuff-b1fb3.appspot.com")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PublicationViewModel::class.java)
        thingViewModel = ViewModelProvider(this).get(ThingViewModel::class.java)

        descriptionEditText = binding.nameEditText
        addPhotoButton = binding.addPhotoImage
        publishButton = binding.publicationButton

        clickAddPhoto()
        clickPublishThing()
    }

// TODO(Написать логику присваивания ID)
    fun createIdForDatabase(): Int{
        var id = 0
        return id
    }

    private fun initRecyclerPhoto(photos : List<Uri>){
        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = PhotoPublicationRecyclerAdapter(photos)
    }

   private fun clickAddPhoto(){
       addPhotoButton.setOnClickListener {
           val intent = Intent(Intent.ACTION_PICK)
           intent.type = "image/*"
           intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

           galleryActivityLaunch.launch(intent)
       }
   }

    private fun clickPublishThing() {
        publishButton.setOnClickListener() {
            val name = descriptionEditText.text.toString()
            val date = Date()

            thingModel = ThingModel(images = ImagesConverter.fromUriToString(listPhotoUri), name =  name, date = date)

            FirebaseFirestore.getInstance()
                .collection("Publication").document(FirebaseAuth.getInstance().uid!!).collection("Things").document(thingModel.name)
                .set(thingModel)
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, DashboardFragment())
                .commit()
        }
       }

    fun backButtonPressed(){
        childFragmentManager.beginTransaction().remove(this).commit()
    }
}