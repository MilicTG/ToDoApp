package com.delminiusdevs.todoapp.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.delminiusdevs.todoapp.R
import com.delminiusdevs.todoapp.data.models.Priority
import com.delminiusdevs.todoapp.data.models.ToDoData
import com.delminiusdevs.todoapp.data.viewmodel.SharedViewModel
import com.delminiusdevs.todoapp.data.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.*
import kotlinx.android.synthetic.main.fragment_add.view.*

class AddFragment : Fragment() {

    private val toDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        setHasOptionsMenu(true)

        view.spCurrentPriorities.onItemSelectedListener = sharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val title = etCurrentTitle.text.toString()
        val priority = spCurrentPriorities.selectedItem.toString()
        val description = etCurrentDescription.text.toString()
        val validation = sharedViewModel.verifyDataFromUser(title, description)

        //insert data to database
        if (validation) {
            val newData = ToDoData(0, title, sharedViewModel.parsePriority(priority), description)
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Added to database", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields", Toast.LENGTH_SHORT)
                .show()
        }

    }
}