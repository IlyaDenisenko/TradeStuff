package com.zil.tradestuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zil.tradestuff.databinding.ActivityMainBinding
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.account.AccountFragment
import com.zil.tradestuff.ui.dashboard.DashboardFragment
import com.zil.tradestuff.ui.message.MessageFragment
import com.zil.tradestuff.ui.publication.PublicationFragment

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    var fragment : PublicationFragment = PublicationFragment()
    lateinit var thingViewModel: ThingViewModel
    lateinit var auth: FirebaseAuth
    companion object{
        var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
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

        val navView: BottomNavigationView = binding.navView
        auth = FirebaseAuth.getInstance()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.findNavController()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.navigation_dashboard, R.id.navigation_message, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)

        navView.background = null
        checkAuth()

       // val appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, "DB").build()
    }

    fun loadFragmentInBottomNavigationView(fragment: Fragment?) : Boolean{
        if (fragment != null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment)
                .commit()
        }
        return true
    }

    fun checkAuth(){
        if (auth.currentUser == null){
            authActivityLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
        } else{
            Toast.makeText(this, "Welcome " + auth.currentUser?.displayName, Toast.LENGTH_LONG).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        var fragment: Fragment? = null

        when(item.itemId){
            R.id.navigation_dashboard -> fragment = DashboardFragment()
            R.id.navigation_message -> fragment =  MessageFragment()
            R.id.navigation_account -> fragment = AccountFragment()
        }
        return loadFragmentInBottomNavigationView(fragment)
    }


}