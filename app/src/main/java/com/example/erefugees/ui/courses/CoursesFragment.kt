package com.example.erefugees.ui.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.erefugees.R

class CoursesFragment : Fragment() {

    private lateinit var coursesViewModel: CoursesViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        coursesViewModel =
                ViewModelProviders.of(this).get(CoursesViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_coures, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        coursesViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
        return root
    }
}
