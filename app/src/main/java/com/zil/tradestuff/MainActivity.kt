package com.zil.tradestuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.MainThread
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.zil.tradestuff.common.MyApp
import com.zil.tradestuff.databinding.ActivityMainBinding
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.account.AccountFragment
import com.zil.tradestuff.ui.dashboard.DashboardFragment
import com.zil.tradestuff.ui.favorite.FavoriteFragment
import com.zil.tradestuff.ui.message.MessageFragment
import com.zil.tradestuff.ui.publication.PublicationFragment
import java.util.*
import kotlin.collections.ArrayDeque

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    lateinit var fragment : Fragment

    private lateinit var botNavView: BottomNavigationView
    private lateinit var thingViewModel: ThingViewModel
    private var itemId: Int = 0
    private lateinit var backStackEntry: NavBackStackEntry
    private lateinit var backQueue: ArrayDeque<NavBackStackEntry>
    private var addToBackStack = true
    private lateinit var fragmentBackStack: Stack<Int>
    companion object{
        var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        lateinit var navController: NavController
    }

    object SingletonFirebaseStorage {
        val getFirebaseStorage = FirebaseStorage.getInstance()
    }

    val authActivityLauncher : ActivityResultLauncher<Intent?> = registerForActivityResult(AuthWindowContract()) { result ->
            if(result == 1) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!",
                        Toast.LENGTH_LONG)
                        .show()
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.",
                        Toast.LENGTH_LONG)
                        .show()

                // Close the app
                finish()
            }
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
    }

    private fun defaultNavigation(){
        fragmentBackStack = Stack()
        navController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                if(addToBackStack){
                    if(!fragmentBackStack.contains(destination.id)){
                        fragmentBackStack.add(destination.id)
                    } else if(fragmentBackStack.contains(destination.id)){
                        if (destination.id == R.id.navigation_dashboard){
                            val homeCount = Collections.frequency(fragmentBackStack, R.id.navigation_dashboard)
                            if (homeCount < 2){
                                fragmentBackStack.push(destination.id)
                            } else{
                                fragmentBackStack.asReversed().remove(destination.id)
                                fragmentBackStack.push(destination.id)
                            }
                        }else {
                            fragmentBackStack.remove(destination.id)
                            fragmentBackStack.push(destination.id)
                        }
                    }
                }
                addToBackStack = true
            }
        })
    }

    override fun onBackPressed() {
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
    }

    private fun checkAuth(){
        if (MyApp.getFirebaseAuth().currentUser == null){
           // authActivityLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
            Toast.makeText(this, "Sign up or Sign in", Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(this, "Welcome " + MyApp.getFirebaseAuth().currentUser?.displayName, Toast.LENGTH_LONG).show()
        }
    }
}