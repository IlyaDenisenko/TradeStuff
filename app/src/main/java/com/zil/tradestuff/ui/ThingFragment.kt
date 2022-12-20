package com.zil.tradestuff.ui

import android.app.Activity
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.zil.tradestuff.LoadingDialog
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.adapter.DescriptionThingRecyclerAdapter
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
    private lateinit var progressBar: ProgressBar

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
        progressBar = binding.progressBar

        progressBar.visibility = View.VISIBLE
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
        nameTextView.text = thing.name
        descTextView.text = thing.description
        initRecycler(thing)
    }

}