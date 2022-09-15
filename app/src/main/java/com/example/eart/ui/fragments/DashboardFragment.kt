package com.example.eart.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.ContentView
import androidx.fragment.app.Fragment
import com.example.eart.R
import com.example.eart.databinding.FragmentDashboardBinding
import com.example.eart.ui.activities.Settings

class DashboardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // To use the options menu in a fragment we add this below
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val textView: TextView = root.findViewById(R.id.text_dashboard)
            textView.text = "I Aam the Dashboard"
        return root
    }

    // This will make the menu to be activated
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //We using a custon menu that we created, so we have to inflate it first
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    //This will activate the items in the options menu on what to display next
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        when(id){
            R.id.action_settings ->{
                startActivity(Intent(activity, Settings::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}