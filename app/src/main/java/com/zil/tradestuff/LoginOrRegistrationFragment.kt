package com.zil.tradestuff

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.zil.tradestuff.databinding.FragmentLoginOrRegistrationBinding


class LoginOrRegistrationFragment : Fragment() {

    private var _binding: FragmentLoginOrRegistrationBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginButton: Button

    private val auth = FirebaseAuth.getInstance()

    val authActivityLauncher : ActivityResultLauncher<Intent?> = registerForActivityResult(AuthWindowContract()) { result ->
        if(result == 1) {
            Toast.makeText(activity,
                "Successfully signed in. Welcome!",
                Toast.LENGTH_LONG)
                .show()
        } else {
            Toast.makeText(activity,
                "We couldn't sign you in. Please try again later.",
                Toast.LENGTH_LONG)
                .show()

            // Close the app
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginOrRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        loginButton = binding.buttonLogin

        clickLogin()
        return root
    }

    fun clickLogin(){
        loginButton.setOnClickListener {
            authActivityLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build())
        }
    }
}