package com.zil.tradestuff.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.database.FirebaseListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zil.tradestuff.MainActivity
import com.zil.tradestuff.R
import com.zil.tradestuff.model.DataMessage
import com.zil.tradestuff.databinding.FragmentMessageBinding
import java.text.SimpleDateFormat
import java.util.*

class MessageFragment :  Fragment() {

    private lateinit var messageViewModel: MessageViewModel
    private var _binding: FragmentMessageBinding? = null
    private var time : Long = 0
    var userName = FirebaseAuth.getInstance().currentUser


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        messageViewModel =
            ViewModelProvider(this).get(MessageViewModel::class.java)

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        time = Date().time

        val sendMessageBut = binding.sendMessageButton
        /*sendMessageBut.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View?) {
                val editText = binding.messageEditText
                MainActivity.firebaseDatabase.getReference("message")
                    .push()
                    .setValue(DataMessage(
                        editText.text.toString(), userName?.displayName, time)
                    )

                editText.setText("")
            }
        })*/
        initAdapterMyMessages()
        return root
    }

    fun initAdapterMyMessages(){
        val adapter: FirebaseListAdapter<DataMessage>
                = object : FirebaseListAdapter<DataMessage>(
                    activity,
                    DataMessage::class.java,
                    R.layout.item_chat_their,
                    MainActivity.firebaseDatabase.getReference("message")){
            override fun populateView(v: View, model: DataMessage, position: Int){
                val imageBack = v.findViewById(R.id.item_message_back) as RelativeLayout
                val textBody = v.findViewById(R.id.text_body) as TextView
                val timeMessage = v.findViewById(R.id.text_time) as TextView
                val textName = v.findViewById(R.id.text_name) as TextView

                if (model.name == userName?.displayName){
                    imageBack.setBackgroundResource(R.drawable.my_message)
                   // imageBack.layout(0,0,21,0)
                }

                val timeData = Date(model.time!!)
                val timeFormat = SimpleDateFormat("HH:mm:ss")
                val date = timeFormat.format(timeData)

                textBody.text = model.text
                timeMessage.text = date
                textName.text = model.name
            }
        }
        val recyclerView = binding.messageRecyclerView
        recyclerView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}