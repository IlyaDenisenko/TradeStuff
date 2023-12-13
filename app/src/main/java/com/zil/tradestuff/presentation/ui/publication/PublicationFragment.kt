package com.zil.tradestuff.presentation.ui.publication

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.StorageReference
import com.zil.tradestuff.*
import com.zil.tradestuff.domain.adapter.PhotoPublicationRecyclerAdapter
import com.zil.tradestuff.domain.MyApp
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.databinding.FragmentPublicationBinding
import com.zil.tradestuff.domain.model.ThingModel
import com.zil.tradestuff.presentation.MainActivity
import com.zil.tradestuff.presentation.ui.thing.ThingViewModel
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.random.Random

class PublicationFragment : Fragment() {

    private  var _binding : FragmentPublicationBinding? = null
    private lateinit var viewModel: PublicationViewModel
    private lateinit var thingViewModel: ThingViewModel
    private val binding get() = _binding!!

    private val listPhotoUri : MutableList<Uri> = mutableListOf()
    lateinit var uri : Uri
    lateinit var thingModel : ThingModel

    lateinit var fragmentContainer: FragmentContainerView
    lateinit var nameEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var addPhotoButton: Button
    lateinit var publishButton: Button

    // Обработка выходных данных после возврата из галереи
    val galleryActivityLaunch = registerForActivityResult(TakePicsFromGalleryContract()){ result ->

        val count = result?.clipData?.itemCount
        if(count != null) {
            for (n in 0 until count ) {
                uri = result.clipData!!.getItemAt(n).uri
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
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("marco", "pub onCreate")
    //    checkWorkingDecodeFile()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PublicationViewModel::class.java)
        thingViewModel = ViewModelProvider(this).get(ThingViewModel::class.java)

        fragmentContainer = binding.fragmentContainer
        nameEditText = binding.nameEditText
        descriptionEditText = binding.descriptionEditText
        addPhotoButton = binding.addPhotoImage
        publishButton = binding.publicationButton

        clickAddPhoto()
        clickPublishThing()
    }

    override fun onResume() {
        super.onResume()
        Log.i("marco", "pub onResume")
        if (MyApp.firebaseAuth.getFirebaseAuth().currentUser == null)
            fragmentContainer.visibility = View.VISIBLE
        else
            fragmentContainer.visibility = View.GONE
    }

    fun checkWorkingDecodeFile(){
        val permissionStatus = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionStatus == PackageManager.PERMISSION_GRANTED){
            val fileUri = fileSizeReduction("/raw/storage/emulated/0/DCIM/Camera/IMG_20230206_124448.jpg")
            Toast.makeText(requireContext(), fileUri.toString(), Toast.LENGTH_SHORT).show()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(), Array(1) { android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1)
        }
    }

// TODO(Написать логику присваивания ID)
    fun createIdForDatabase(): Int{
        val id = 0
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
        val firebaseAuth = MyApp.firebaseAuth.getFirebaseAuth()
        publishButton.setOnClickListener() {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val date = Date()
            val imagesName: MutableList<String> = mutableListOf()
            for(n in 0 until listPhotoUri.size)
                imagesName.add(n,File(listPhotoUri[n].path.toString()).name )

            thingModel = ThingModel(
                id = Random.nextInt(1, 100),
                userId = firebaseAuth.uid,
                userName = firebaseAuth.currentUser?.displayName,
                images = ImagesConverter.fromUriToString(listPhotoUri),
                name =  name,
                description = description,
                date = date)

            if (nameEditText.text.isNotEmpty()){
                thingViewModel.insertThing(thingModel)
                MainActivity.navController.navigate(R.id.navigation_dashboard)
              //  MainActivity.navController.backQueue.remove(MainActivity.navController.getBackStackEntry(R.id.navigation_publication))
            }
            else
                Toast.makeText(requireContext(), R.string.edit_text_name_of_thing_empty, Toast.LENGTH_SHORT).show()
        }
       }

    fun fileSizeReduction(path: String): Uri{
        val file2 = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString())

        val bitmap: Bitmap =
            ReaderBitmap.decodeSampledBitmapFromFile(path, 500, 500)
            val outputStream = BufferedOutputStream(FileOutputStream(file2))
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        return file2.toUri()
    }

    override fun onStart() {
        super.onStart()
        Log.i("marco", "pub onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("marco", "pub onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("marco", "pub onDestroy")
    }
}