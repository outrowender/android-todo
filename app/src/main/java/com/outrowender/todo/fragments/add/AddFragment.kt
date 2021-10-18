package com.outrowender.todo.fragments.add

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.outrowender.todo.R
import com.outrowender.todo.data.models.Priority
import com.outrowender.todo.data.models.TodoData
import com.outrowender.todo.data.viewModel.TodoViewModel
import com.outrowender.todo.databinding.FragmentAddBinding
import com.outrowender.todo.fragments.SharedViewModel

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        binding.updateNoteSpinner.onItemSelectedListener = mSharedViewModel.listener

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add) insertDataToDB()
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val mTitle = binding.noteTitleUpdateEditText.text.toString()
        val mPriority = binding.updateNoteSpinner.selectedItem.toString()
        val mDescription = binding.noteDescriptionUpdateEditText.text.toString()

        val valid = mSharedViewModel.validate(mTitle, mPriority, mDescription)

        if(!valid) return //TODO: something if error on validation

        val newData = TodoData(
            0,
            mTitle,
            mSharedViewModel.parsePriority(mPriority),
            mDescription
        )

        mTodoViewModel.insertData(newData)

        Toast.makeText(requireContext(), "TODO: saved message!", Toast.LENGTH_SHORT).show()
        //Go back, go crazy
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
    }



}