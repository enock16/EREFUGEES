package com.example.erefugees

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.erefugees.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }


    val adapter = GroupAdapter<ViewHolder>()
    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        toUser  = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title =  toUser?.username
        recyclerView_chatLog_typeMessage.adapter = adapter
//        adapter.add(ChatItemFromItem("This must be kinda short for my own good that is why am her to stay "))
//        adapter.add(ChatItemToItem("i hope everything goes well and we go back to work"))
//        adapter.add(ChatItemFromItem("This must be kinda short for my own good that is why am her to stay"))
//        adapter.add(ChatItemToItem("i hope everything goes well and we go back to work"))



        // setupDummyData()
        listenForMessages()

        btnSendMessage_toClient_chatLog.setOnClickListener {
            Log.d(TAG, "ATTEMPT TO SEND MESSAGE ")
            performSendMessage()

        }
    }

    private fun listenForMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        //val ref = FirebaseDatabase.getInstance().getReference("/user-messages/")
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId/")


        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessages::class.java)
                if (chatMessage != null) {
                    Log.d(TAG, "chatMessage.text")
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentuser = ChatFragment.currentUser ?: return
                        adapter.add(ChatItemFromItem(chatMessage.text, currentuser))

                    } else {
                        adapter.add(ChatItemToItem(chatMessage.text, toUser!!))

                    }

                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })



    }

    private  fun performSendMessage(){
        // how do we finally send a message to database
        val text = et_typeMessage_toClient_chatLog.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user  = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid
        if (fromId ==null)return
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessages(
            reference.key!!,
            text,
            fromId,
            toId,
            System.currentTimeMillis() / 1000
        )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")

                et_typeMessage_toClient_chatLog.text.clear()

                recyclerView_chatLog_typeMessage.scrollToPosition(adapter.itemCount - 1)

            }
        toReference.setValue(chatMessage)
            .addOnSuccessListener {
                recyclerView_chatLog_typeMessage.scrollToPosition(adapter.itemCount - 1)

            }

        //Blocked correct code
        //val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId$toId")
        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)
        //Blocked correct code
        //val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId$fromId")
        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)

    }


}