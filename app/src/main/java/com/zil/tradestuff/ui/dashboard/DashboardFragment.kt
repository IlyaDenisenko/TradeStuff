package com.zil.tradestuff.ui.dashboard

import android.os.Bundle
import android.os.Environment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.adapter.BoardOfThingsRecyclerAdapter
import com.zil.tradestuff.adapter.BoardOfThingsRecyclerAdapter.OnThingClickListener
import com.zil.tradestuff.databinding.FragmentDashboardBinding
import com.zil.tradestuff.model.ThingModel
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.server.ContractDBInterface
import com.zil.tradestuff.ui.ThingFragment
import com.zil.tradestuff.ui.publication.PublicationFragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class DashboardFragment : Fragment(), OnThingClickListener, ContractDBInterface.CallbackServerData {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var thingViewModel: ThingViewModel
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingBut : FloatingActionButton

    private var idSelectedThing = 0
    lateinit var listThings: List<ThingModel>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("marco", "dash onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        thingViewModel = ViewModelProvider(this).get(ThingViewModel::class.java)

        swipeLayout = binding.swipeRefresh
        recyclerView = binding.thingsRecycler
        floatingBut = binding.floatingBut

        getDataFromLiveData()
        refreshingDashboardList()
        clickFloatingBut()
    }

    private fun refreshingDashboardList(){
        swipeLayout.setOnRefreshListener {
            getDataFromLiveData()
            Toast.makeText(context, "refreshed", Toast.LENGTH_SHORT).show()
            swipeLayout.isRefreshing = false
        }
    }

    private fun initRecycler(listThings : List<ThingModel>){
        //recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = BoardOfThingsRecyclerAdapter(listThings, this)
    }

    private fun getDataFromLiveData(){
        thingViewModel.allThingLiveData(this).observe(viewLifecycleOwner) {
                things -> listThings = things
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

    override fun onStart() {
        super.onStart()
        Log.i("marco", "dash onStart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("marco", "dash onDestroy")
        _binding = null
    }

    override fun onClickItem(thingModel: ThingModel, position: Int) {
        val nav = MainActivity.navController

        idSelectedThing = listThings[position].id
        parentFragmentManager.setFragmentResult("selectedItem", bundleOf(
            "userSelectedThing" to listThings[position].userId,
            "idSelectedThing" to idSelectedThing))

        nav.navigate(R.id.thingFragment)
    }

    override fun actionAfterComingData() {
            initRecycler(listThings)
    }
}

