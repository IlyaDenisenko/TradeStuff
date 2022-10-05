package com.zil.tradestuff.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.zil.tradestuff.R
import com.zil.tradestuff.adapter.BoardOfThingsRecyclerAdapter
import com.zil.tradestuff.adapter.BoardOfThingsRecyclerAdapter.OnThingClickListener
import com.zil.tradestuff.databinding.FragmentDashboardBinding
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.ThingFragment
import com.zil.tradestuff.ui.publication.PublicationFragment
import kotlin.concurrent.thread

class DashboardFragment : Fragment(), OnThingClickListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var thingViewModel: ThingViewModel
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var floatingBut : FloatingActionButton
    private lateinit var deleteDataBut : Button
    private var idSelectedThing = 0
    var listThings: MutableList<ThingModel> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        thingViewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
       /* thingViewModel.allThingLiveData.observe(viewLifecycleOwner) {
                things -> initRecycler(things)
            listThings = things
        }*/

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        floatingBut = binding.floatingBut
        deleteDataBut = binding.deleteAllData

        getDataFromFirestore()
        initRecycler(listThings)
        clickFloatingBut()
        return root
    }


    private fun initRecycler(listThings : List<ThingModel>){
        val recyclerView: RecyclerView = binding.thingsRecycler
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = BoardOfThingsRecyclerAdapter(listThings, this)
    }

    fun getDataFromFirestore(){
        FirebaseFirestore.getInstance()
            .collection("things").document("Ilya")
            .get().addOnSuccessListener { documentSnapshot ->
              //  val thing = documentSnapshot.toObject(ThingModel)
              //  listThings.add(thing)
            }
    }


    private fun clickFloatingBut(){
        floatingBut.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction
                .replace(R.id.nav_host_fragment_activity_main, PublicationFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    fun backButtonPressed(){
        parentFragmentManager.popBackStack()
    }

    private fun clickDeleteDataBut(){
        deleteDataBut.setOnClickListener{

        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(thingModel: ThingModel, position: Int) {
        val fragmentTransaction = parentFragmentManager.beginTransaction()
        idSelectedThing = listThings[position].id
        parentFragmentManager.setFragmentResult("selectedItem", bundleOf("idSelectedThing" to idSelectedThing))
        fragmentTransaction
            .replace(R.id.nav_host_fragment_activity_main, ThingFragment())
            .addToBackStack("dashboardStack")
            .commit()
    }

    override fun onClickDeleteItem(position: Int){
        idSelectedThing = listThings[position].id
        thread {
            thingViewModel.deleteThing(idSelectedThing)
        }

    }
}

