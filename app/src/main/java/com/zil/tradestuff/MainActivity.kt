package com.zil.tradestuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zil.tradestuff.databinding.ActivityMainBinding
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.publication.PublicationFragment

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    var fragment : PublicationFragment = PublicationFragment()

    lateinit var botNavView: BottomNavigationView
    lateinit var thingViewModel: ThingViewModel
    lateinit var auth: FirebaseAuth
    companion object{
        var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        lateinit var navController: NavController
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
        auth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.findNavController()

        botNavView.setupWithNavController(navController)
        checkAuth()
        onNavigationBottomItemSelect()
    }

    fun navigationOption(): NavOptions{
        val option: NavOptions = NavOptions.Builder()
            .build()
        return option
    }

    fun onNavigationBottomItemSelect(){
        botNavView.setOnItemSelectedListener {
            navController.navigate(it.itemId, null, navigationOption())
            return@setOnItemSelectedListener true
        }
    }

    fun checkAuth(){
        if (auth.currentUser == null){
          //  authActivityLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
        } else{
            Toast.makeText(this, "Welcome " + auth.currentUser?.displayName, Toast.LENGTH_LONG).show()
        }
    }
}