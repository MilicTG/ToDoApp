package com.delminiusdevs.todoapp.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.delminiusdevs.todoapp.R
import com.delminiusdevs.todoapp.data.models.ToDoData
import com.delminiusdevs.todoapp.data.viewmodel.SharedViewModel
import com.delminiusdevs.todoapp.data.viewmodel.ToDoViewModel
import kotlinx.android.synthetic.main.fragment_add.etCurrentDescription
import kotlinx.android.synthetic.main.fragment_add.etCurrentTitle
import kotlinx.android.synthetic.main.fragment_add.spCurrentPriorities
import kotlinx.android.synthetic.main.fragment_update.view.*

class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val toDoViewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_update, container, false)

        setHasOptionsMenu(true)

        view.etCurrentTitle.setText(args.currentItem.title)
        view.etCurrentDescription.setText(args.currentItem.description)
        view.spCurrentPriorities.setSelection(sharedViewModel.parsePriorityToInt(args.currentItem.priority))
        view.spCurrentPriorities.onItemSelectedListener = sharedViewModel.listener

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateItem() {
        val getTitle = etCurrentTitle.text.toString()
        val getDescription = etCurrentDescription.text.toString()
        val getSpinnerPosition = spCurrentPriorities.selectedItem.toString()

        val validation = sharedViewModel.verifyDataFromUser(getTitle, getDescription)
        if (validation) {
            //Update the item in DB
            val updatedItem = ToDoData(
                args.currentItem.id,
                getTitle,
                sharedViewModel.parsePriority(getSpinnerPosition),
                getDescription
            )
            toDoViewModel.updateData(updatedItem)
            Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
            //navigate back
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill out fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmItemRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            toDoViewModel.deleteItem(args.currentItem)
            Toast.makeText(
                requireContext(),
                "Item deleted: ${args.currentItem.title}",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete: ${args.currentItem.title}?")
        builder.setMessage("Are you sure you want to delete: ${args.currentItem.title}?")
        builder.create().show()
    }
}