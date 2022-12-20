package com.zil.tradestuff.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private lateinit var notificationsViewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null
    lateinit var auth : FirebaseAuth
    private lateinit var butSignOut: Button

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
        auth = FirebaseAuth.getInstance()

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val root: View = binding.root
        butSignOut = binding.butExitAccount
        pressedSignOut(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentContainer = binding.fragmentAccountContainer

        if (FirebaseAuth.getInstance().currentUser == null){
            fragmentContainer.visibility = View.VISIBLE
            butSignOut.visibility = View.GONE}
        else {
            fragmentContainer.visibility = View.GONE
            butSignOut.visibility = View.VISIBLE}
    }

    private fun pressedSignOut(fragment: Fragment){
        butSignOut.setOnClickListener(object : View.OnClickListener{

            override fun onClick(p0: View?) {
                auth.signOut()
                parentFragmentManager.beginTransaction().remove(fragment).commit()
                activity?.onBackPressed()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}