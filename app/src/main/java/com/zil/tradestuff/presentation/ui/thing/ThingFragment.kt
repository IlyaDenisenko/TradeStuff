package com.zil.tradestuff.presentation.ui.thing

import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import androidx.recyclerview.widget.SnapHelper
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.StorageReference
import com.zil.tradestuff.presentation.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.State
import com.zil.tradestuff.dao.ImagesConverter
import com.zil.tradestuff.domain.adapter.DescriptionThingRecyclerAdapter
import com.zil.tradestuff.domain.MyApp
import com.zil.tradestuff.databinding.FragmentThingBinding
import com.zil.tradestuff.domain.model.ThingModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class ThingFragment : Fragment(){

    private lateinit var viewModel: ThingViewModel
    private var _binding: FragmentThingBinding? = null
    private val binding get() = _binding!!
    private lateinit var nameTextView: TextView
    private lateinit var descTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var deleteButton: Button
    private lateinit var photoCount: TextView
    private lateinit var recyclerView: RecyclerView

    private var adapter = DescriptionThingRecyclerAdapter(listOf())
    var currentPhotoNumber = 0
    var idThing = 1
    lateinit var userIdSelectedThing: String
    var thing: ThingModel = ThingModel()
    private val uiScope = CoroutineScope(Dispatchers.Main)
    var listPhotos: List<Task<Uri>> = mutableListOf()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("marco", "thingFragment onSaveInstanceState")
        outState.putString("userThing", userIdSelectedThing)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
       // Log.i("marco", "thingFragment onViewStateRestored: " + userSelectedThing)
        if (savedInstanceState != null) {
            userIdSelectedThing = savedInstanceState.getString("userThing").toString()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentThingBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("marco", "thingFragment onViewCreated")
        if (savedInstanceState != null) {
            userIdSelectedThing = savedInstanceState.getString("userThing").toString()
        }
        viewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
        nameTextView = binding.nameTextView
        descTextView = binding.descriptionTextView
        userNameTextView = binding.userName
        progressBar = binding.progressBar
        deleteButton = binding.butDeleteThing
        photoCount = binding.countPhotoText
        recyclerView = binding.photoRecyclerView

        progressBar.visibility = View.VISIBLE
        clickDeleteButton()
        initRecycler()

        setFragmentResultListener("selectedItem"){
                key, bundle ->
            userIdSelectedThing = bundle.getString("userSelectedThing")!!
            idThing = bundle.getInt("idSelectedThing")
            Log.i("checkFragmentResult", "Result thing" + userIdSelectedThing)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("marco", "thingFragment onResume")
        uiScope.launch {
            getDataFromViewModel(userIdSelectedThing, idThing)
        }
    }

    suspend fun getDataFromViewModel(userIdSelectedThingParam: String, idThingParam: Int){
        Log.i("checkFragmentResult", "Result thing " + userIdSelectedThing)
        var listThing: List<ThingModel>
        viewModel.getThingsByUser(userIdSelectedThingParam).collect{ state ->

            when(state){
                is State.Loading -> progressBar.visibility = View.VISIBLE
                is State.Success -> {
                    listThing = state.data
                    Log.i("checkFragmentResult", "List things " + listThing.size)

                    progressBar.visibility = View.GONE
                    thing = listThing.find { it.id == idThingParam }!!

                    if (MyApp.firebaseAuth.getFirebaseAuth().uid == thing.userId)
                        deleteButton.visibility = View.VISIBLE
                    nameTextView.text = thing.name
                    descTextView.text = thing.description
                    userNameTextView.text = thing.userName
                    /*adapter.thing = thing
                    Log.i("marco", File(ImagesConverter.fromStringToUri(thing.images)[1].path.toString()).name)*/
                }
                is State.Failed -> Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.getListPhotosByItem(thing).collect{state ->
            when (state){
                is State.Loading -> Log.i("marco", "Photos is loading")
                is State.Success -> {
                    listPhotos = state.data
                    adapter.thing = listPhotos
                    adapter.notifyDataSetChanged()
                    photoCount.text = getString(R.string.text_photo_count, currentPhotoNumber + 1, listPhotos.size)
                    Log.i("marco", listPhotos.get(0).await().toString())
                }
                is State.Failed -> Log.i("marco", "Loading is failed")
            }
        }
    }

    private fun initRecycler() {
        Log.i("marco", "thingFragment initRecycler")
        val mLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = adapter

        val snapHelper: SnapHelper = LinearSnapHelper()
        recyclerView.onFlingListener = null
        snapHelper.attachToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var visiblePosition: Int
                when(newState){
                    SCROLL_STATE_IDLE -> {
                        visiblePosition = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                        currentPhotoNumber = visiblePosition
                        photoCount.text = getString(R.string.text_photo_count, currentPhotoNumber + 1, (recyclerView.layoutManager as LinearLayoutManager).itemCount)
                    }

                    SCROLL_STATE_SETTLING -> {
                        visiblePosition = mLayoutManager.findFirstVisibleItemPosition()
                        currentPhotoNumber = visiblePosition
                        photoCount.text = getString(R.string.text_photo_count, currentPhotoNumber + 1, (recyclerView.layoutManager as LinearLayoutManager).itemCount)
                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.i("marco", "thingFragment onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("marco", "thingFragment onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("marco", "thingFragment onDestroy")
        recyclerView.clearOnScrollListeners()
        _binding = null
    }

    private fun clickDeleteButton(){
        deleteButton.setOnClickListener {
            viewModel.deleteThing(thing)
            MainActivity.navController.navigate(R.id.navigation_dashboard)
            /*MainActivity.navController.backQueue.remove(
                MainActivity.navController.getBackStackEntry(
                    R.id.thingFragment))*/
        }
    }
}