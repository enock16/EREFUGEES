package com.example.erefugees

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.erefugees.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_messages.*

class NewMessages : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_messages)
        supportActionBar?.title = "Select User"

        //  ViewHolder that handles Views
//          val adapter = GroupAdapter<ViewHolder>()
//       //  add Users to adapter
//        adapter.add(UserItem())
//    adapter.add(UserItem())
//    adapter.add(UserItem())
//    adapter.add(UserItem())
//
//    recyclerView_New_Message.adapter = adapter
        fetchUsers()

    }
    companion object{

        val USER_KEY= "USER_KEY"
    }

    private fun fetchUsers(){


        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            // method fetches DataSnapshots for Users class
            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d("NewMessageActivity",it.toString())

                    val currentUser = FirebaseAuth.getInstance().uid
                    val user =  it.getValue(User::class.java)
                    if (user != null && user.uid != currentUser!!) {

                        adapter.add(UserItem(user))

                    }
                }
                // on click adapter intent to  the next Activity
                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context,ChatLogActivity::class.java)
                    // intent.putExtra(USER_KEY,userItem.user.username)
                    intent.putExtra(USER_KEY,userItem.user)
                    startActivity(intent)
                    finish()

                }

                recyclerNew.adapter = adapter
            }


            override fun onCancelled(p0: DatabaseError) {
            }


        })


    }
}
// add UserItem class since object is layout
class UserItem( val user : User): Item<ViewHolder>(){
    //bind all data  in ViewHolder
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_new_message_textView.text = user.username
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_new_message)

    }
    // layout for New_MessageActivity
    override fun getLayout(): Int {
        return R.layout.row_newmessage_layout
    }

}

