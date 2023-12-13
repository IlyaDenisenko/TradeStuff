package com.zil.tradestuff.presentation.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zil.tradestuff.presentation.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.State
import com.zil.tradestuff.domain.adapter.BoardOfThingsRecyclerAdapter
import com.zil.tradestuff.domain.adapter.BoardOfThingsRecyclerAdapter.OnThingClickListener
import com.zil.tradestuff.databinding.FragmentDashboardBinding
import com.zil.tradestuff.domain.model.ThingModel
import com.zil.tradestuff.presentation.ui.publication.PublicationFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardFragment : Fragment(), OnThingClickListener {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingBut : FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private var adapter = BoardOfThingsRecyclerAdapter(mutableListOf(), this)

    private var idSelectedThing = 0
    var listThings: MutableList<ThingModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        Log.i("marco", "dash onCreate")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        swipeLayout = binding.swipeRefresh
        recyclerView = binding.thingsRecycler
        floatingBut = binding.floatingBut
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE

        initRecycler()
        uiScope.launch {
            getDataFromViewModel()
        }
        refreshingDashboardList()
        clickFloatingBut()

        return root
    }

    private fun refreshingDashboardList(){
        swipeLayout.setOnRefreshListener {
            uiScope.launch {
                getDataFromViewModel()
            }
            swipeLayout.isRefreshing = false
        }
    }

    private fun initRecycler(){
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        recyclerView.adapter = adapter
    }

    suspend fun getDataFromViewModel(){
        dashboardViewModel.getAllData().collect{ state ->
            when(state){
                is State.Loading ->  progressBar.visibility = View.VISIBLE

                is State.Success -> {
                    listThings = state.data
                    adapter.listThings = listThings
                    adapter.notifyDataSetChanged()
                    progressBar.visibility = View.GONE
                }
                is State.Failed -> Toast.makeText(requireContext(), "failed", Toast.LENGTH_SHORT).show()
            }
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

    override fun onResume() {
        super.onResume()
        Log.i("marco", "dash onResume")
    }

    override fun onStart() {
        super.onStart()
        Log.i("marco", "dash onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i("marco", "dash onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("marco", "dash onDestroy")
        _binding = null
    }

    override fun onClickItem(thingModel: ThingModel, position: Int) {
        val nav = MainActivity.navController

        Log.i("checkFragmentResult", "Result dash " + listThings[position].userId)
        idSelectedThing = listThings[position].id
        setFragmentResult("selectedItem", bundleOf(
            "userSelectedThing" to listThings[position].userId,
            "idSelectedThing" to idSelectedThing))

        nav.navigate(R.id.action_navigation_dashboard_to_thingFragment)
    }
}

