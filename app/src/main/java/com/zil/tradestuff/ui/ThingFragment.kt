package com.zil.tradestuff.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.zil.tradestuff.adapter.BoardOfThingsRecyclerAdapter
import com.zil.tradestuff.adapter.DescriptionThingRecyclerAdapter
import com.zil.tradestuff.databinding.FragmentThingBinding
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.model.ThingViewModel
import kotlin.concurrent.thread

class ThingFragment : Fragment() {

    private lateinit var viewModel: ThingViewModel
    private var _binding: FragmentThingBinding? = null
    private val binding get() = _binding!!
    var idThing = 0
    lateinit var listThing: List<ThingModel>
    lateinit var thing: ThingModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setFragmentResultListener("selectedItem"){
                key, bundle -> idThing = bundle.getInt("idSelectedThing")
        }
        viewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
        viewModel.allThingLiveData.observe(viewLifecycleOwner) {
                things -> listThing = things
            thing = things.find { it.id == idThing }!!
            initRecycler(thing)
            binding.name.text = thing.name
        }
        _binding = FragmentThingBinding.inflate(inflater, container, false)
        val root = binding.root
        return root
    }


    fun initRecycler(thing: ThingModel){
        val recyclerView = binding.photoRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = DescriptionThingRecyclerAdapter(thing, idThing)
      //  Log.i("ThingFragment: ", listThing.toString())
    }

}