package com.zil.tradestuff.presentation.ui.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zil.tradestuff.domain.MyApp
import com.zil.tradestuff.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var notificationsViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null
    private lateinit var userName: TextView
    private lateinit var butSignOut: Button
    private lateinit var fragmentContainer: FragmentContainerView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        userName = binding.userName
        butSignOut = binding.butExitAccount

        userName.text = MyApp.firebaseAuth.getFirebaseAuth().currentUser?.displayName
        pressedSignOut(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentContainer = binding.fragmentAccountContainer
    }

    override fun onResume() {
        super.onResume()
        if (MyApp.firebaseAuth.getFirebaseAuth().currentUser == null){
            fragmentContainer.visibility = View.VISIBLE
            butSignOut.visibility = View.GONE}
        else {
            fragmentContainer.visibility = View.GONE
            butSignOut.visibility = View.VISIBLE}
    }

    private fun pressedSignOut(fragment: Fragment){
        butSignOut.setOnClickListener(object : View.OnClickListener{

            override fun onClick(p0: View?) {
                MyApp.firebaseAuth.getFirebaseAuth().signOut()
                parentFragmentManager.beginTransaction().remove(fragment).commit()
                activity?.onBackPressed()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("marco", "acc onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("marco", "acc onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}