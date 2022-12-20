package com.zil.tradestuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zil.tradestuff.databinding.ActivityMainBinding
import com.zil.tradestuff.model.ThingViewModel
import com.zil.tradestuff.ui.account.AccountFragment
import com.zil.tradestuff.ui.dashboard.DashboardFragment
import com.zil.tradestuff.ui.favorite.FavoriteFragment
import com.zil.tradestuff.ui.message.MessageFragment
import com.zil.tradestuff.ui.publication.PublicationFragment
import io.grpc.internal.MessageFramer

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    lateinit var fragment : Fragment

    private lateinit var botNavView: BottomNavigationView
    private lateinit var thingViewModel: ThingViewModel
    private lateinit var auth: FirebaseAuth
    private var itemId: Int = 0
    private lateinit var backStackEntry: NavBackStackEntry
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
      //  checkAuth()
        onNavigationBottomItemSelect()
    }

    private fun navigationOption(): NavOptions {
        return NavOptions.Builder()
            .setRestoreState(false)
            .build()
    }
    private lateinit var previousFragment : Fragment

    private fun replaceFragment(fragment: Fragment){
        val manager = supportFragmentManager
        manager.beginTransaction()
            .replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(null)
            .commit()
        previousFragment = fragment

    }

    private fun onNavigationBottomItemSelect(){
        val backQueue = navController.backQueue

        botNavView.setOnItemSelectedListener {
            itemId = it.itemId
            try {
                backStackEntry = navController.getBackStackEntry(itemId)
                navController.navigate(itemId, null, navigationOption())

                    if (!backQueue.contains(backStackEntry)){
                        return@setOnItemSelectedListener true
                    }
                    else{
                        backQueue.remove(backStackEntry)
                        supportFragmentManager.findFragmentById(itemId)
                            ?.let { it1 -> supportFragmentManager.beginTransaction().remove(it1) }
                        Log.i("marco", supportFragmentManager.backStackEntryCount.toString())
                    }

            } catch (e: IllegalArgumentException) {
                navController.navigate(itemId, null, navigationOption())
            }
            for (i in 1..backQueue.size )
                Log.i("legend", backQueue[i-1].destination.displayName)

            /*when(it.itemId){
                R.id.navigation_dashboard -> replaceFragment(DashboardFragment())
                R.id.navigation_favorite -> replaceFragment(FavoriteFragment())
                R.id.navigation_publication -> replaceFragment(PublicationFragment())
                R.id.navigation_message -> replaceFragment(MessageFragment())
                R.id.navigation_account -> replaceFragment(AccountFragment())
            }*/
            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        try{
//            backStackEntry = navController.getBackStackEntry(itemId)
//            navController.backQueue.remove(backStackEntry)
//            Toast.makeText(this, "work", Toast.LENGTH_SHORT).show()
//        }
//        catch (e: IllegalArgumentException){
//            Toast.makeText(this, "don't work", Toast.LENGTH_SHORT).show() }
        if (navController.backQueue.size == 1){
            finish()
        }
    }

    private fun checkAuth(){
        if (auth.currentUser == null){
            authActivityLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
        } else{
            Toast.makeText(this, "Welcome " + auth.currentUser?.displayName, Toast.LENGTH_LONG).show()
        }
    }
}