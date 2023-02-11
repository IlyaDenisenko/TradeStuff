package com.zil.tradestuff.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.adapter.DescriptionThingRecyclerAdapter
import com.zil.tradestuff.common.MyApp
import com.zil.tradestuff.databinding.FragmentThingBinding
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.server.ContractDBInterface

class ThingFragment : Fragment(), ContractDBInterface.CallbackServerData {

    private lateinit var viewModel: ThingViewModel
    private var _binding: FragmentThingBinding? = null
    private val binding get() = _binding!!
    private lateinit var nameTextView: TextView
    private lateinit var descTextView: TextView
    private lateinit var userNameTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var deleteButton: Button

    var idThing = 1
    var userSelectedThing = ""
    lateinit var listThing: List<ThingModel>
    var thing: ThingModel = ThingModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener("selectedItem", this){
                key, bundle ->
            userSelectedThing = bundle.getString("userSelectedThing")!!
            idThing = bundle.getInt("idSelectedThing")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentThingBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
        nameTextView = binding.nameTextView
        descTextView = binding.descriptionTextView
        userNameTextView = binding.userName
        progressBar = binding.progressBar
        deleteButton = binding.butDeleteThing

        progressBar.visibility = View.VISIBLE
        clickDeleteButton()
    }

    override fun onResume() {
        super.onResume()
        getDataFromLiveData()
    }

    fun getDataFromLiveData(){
        listThing = viewModel.getThingsByUser(userSelectedThing, this)
    }

    fun initRecycler(thing: ThingModel){
        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = DescriptionThingRecyclerAdapter(thing, idThing)
    }

    override fun actionAfterComingData() {
        progressBar.visibility = View.GONE
        thing = listThing.find { it.id == idThing }!!

        if (MyApp.getFirebaseAuth().uid == thing.userId)
            deleteButton.visibility = View.VISIBLE
        nameTextView.text = thing.name
        descTextView.text = thing.description
        userNameTextView.text = thing.userName
        initRecycler(thing)
    }

    fun clickDeleteButton(){
        deleteButton.setOnClickListener {
            viewModel.deleteThing(thing)
            MainActivity.navController.navigate(R.id.navigation_dashboard)
            MainActivity.navController.backQueue.remove(
                MainActivity.navController.getBackStackEntry(
                    R.id.thingFragment))
        }
    }

}