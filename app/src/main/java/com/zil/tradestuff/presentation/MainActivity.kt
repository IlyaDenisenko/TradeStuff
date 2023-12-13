package com.zil.tradestuff.presentation

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.zil.tradestuff.R
import com.zil.tradestuff.domain.MyApp
import com.zil.tradestuff.databinding.ActivityMainBinding
import com.zil.tradestuff.presentation.ui.thing.ThingViewModel
import java.util.*

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    lateinit var fragment : Fragment

    private lateinit var botNavView: BottomNavigationView
    private lateinit var thingViewModel: ThingViewModel
    private var itemId: Int = 0
    private var addToBackStack = true
    private lateinit var fragmentBackStack: Stack<Int>
    companion object{
        var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        lateinit var navController: NavController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thingViewModel = ViewModelProvider(this).get(ThingViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        botNavView = binding.bottomNavView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()
        botNavView.setupWithNavController(navController)

        checkAuth()
        defaultNavigation()
     //   onBack()
    }

    private fun defaultNavigation(){
        fragmentBackStack = Stack()
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (addToBackStack) {
                if (!fragmentBackStack.contains(destination.id)) {
                    fragmentBackStack.add(destination.id)
                } else if (fragmentBackStack.contains(destination.id)) {
                    if (destination.id == R.id.navigation_dashboard) {
                        val homeCount = Collections.frequency(
                            fragmentBackStack,
                            R.id.navigation_dashboard
                        )
                        if (homeCount < 1) {
                            fragmentBackStack.push(destination.id)
                        } else {
                            fragmentBackStack.asReversed().remove(destination.id)
                            fragmentBackStack.push(destination.id)
                        }
                    } else {
                        fragmentBackStack.remove(destination.id)
                        fragmentBackStack.push(destination.id)
                    }
                }
            }
            addToBackStack = true
            showFragmentBackStackSize()
        }
    }

    /*override fun onBackPressed() {
        if (::fragmentBackStack.isInitialized && fragmentBackStack.size > 1){
            fragmentBackStack.pop()
            val fragmentId = fragmentBackStack.lastElement()
            addToBackStack = false
            navController.navigate(fragmentId)
        }else{
            if (::fragmentBackStack.isInitialized && fragmentBackStack.size == 1){
                finish()
            } else{
                super.onBackPressed()
            }
        }
    }*/

    private fun onBack(){
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (::fragmentBackStack.isInitialized && fragmentBackStack.size > 1){
                    fragmentBackStack.pop()
                    val fragmentId = fragmentBackStack.lastElement()
                    addToBackStack = false
                    navController.navigate(fragmentId)
                }else if (::fragmentBackStack.isInitialized && fragmentBackStack.size == 1){
                    finish()
                    }else{
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
        })
    }

    private fun showFragmentBackStackSize(){
        Log.i("hunt", fragmentBackStack.size.toString())
    }

    private fun checkAuth(){
        /*if (MyApp.getFirebaseAuth().currentUser == null){
            Toast.makeText(this, "Sign up or Sign in", Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(this, "Welcome " + MyApp.getFirebaseAuth().currentUser?.displayName, Toast.LENGTH_LONG).show()
        }*/
    }
}