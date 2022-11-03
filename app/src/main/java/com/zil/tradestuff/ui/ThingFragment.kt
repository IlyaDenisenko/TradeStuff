package com.zil.tradestuff.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
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

    var idThing = 1
    var nameThing = ""
    lateinit var listThing: List<ThingModel>
    lateinit var thing: ThingModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentThingBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
        nameTextView = binding.nameTextView

        setFragmentResultListener("selectedItem"){
                key, bundle -> idThing = bundle.getInt("idSelectedThing")
        }
        getDataFromLiveData()
    }

    fun getDataFromLiveData(){
        viewModel.allThingLiveData(this).observe(viewLifecycleOwner) {
                things ->
            Log.i("gruff", "ThingFragment: " + things.toString())
            listThing = things
        }
    }

    fun initRecycler(thing: ThingModel){
        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = DescriptionThingRecyclerAdapter(thing, idThing)
    }

    override fun actionAfterComingData() {
        thing = listThing.find { it.id == idThing }!!
        nameThing = thing.name
        Log.i("gruff", "ThingFragmentName: " + thing.name + idThing)
        initRecycler(thing)
    }

}