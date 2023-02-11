package com.zil.tradestuff

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.zil.tradestuff.common.MyApp
import com.zil.tradestuff.databinding.ActivityMainBinding
import com.zil.tradestuff.model.ThingViewModel

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    lateinit var fragment : Fragment

    private lateinit var botNavView: BottomNavigationView
    private lateinit var thingViewModel: ThingViewModel
    private var itemId: Int = 0
    private lateinit var backStackEntry: NavBackStackEntry
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
                Log.i("marco", "BQ navController count: " + backQueue.size)

                    if (!backQueue.contains(backStackEntry)){
                        return@setOnItemSelectedListener true
                    }
                    else{
                        backQueue.remove(backStackEntry)
                        Log.i("marco", "BS fragment manager count: " + supportFragmentManager.backStackEntryCount.toString())
                    }
            } catch (e: IllegalArgumentException) {
                navController.navigate(itemId, null, navigationOption())
                Log.i("marco", "BQ navController count: " + backQueue.toList())
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
        if (MyApp.getFirebaseAuth().currentUser == null){
           // authActivityLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
            Toast.makeText(this, "Sign up or Sign in", Toast.LENGTH_LONG).show()
        } else{
            Toast.makeText(this, "Welcome " + MyApp.getFirebaseAuth().currentUser?.displayName, Toast.LENGTH_LONG).show()
        }
    }
}