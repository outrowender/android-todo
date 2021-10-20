package com.outrowender.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.outrowender.todo.R
import com.outrowender.todo.data.models.Priority
import com.outrowender.todo.data.models.TodoData
import com.outrowender.todo.data.viewModel.TodoViewModel
import com.outrowender.todo.databinding.FragmentUpdateBinding
import com.outrowender.todo.fragments.SharedViewModel

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        binding.noteTitleEditText.setText(args.currentItemParcelable.title)
        binding.noteDescriptionEditText.setText(args.currentItemParcelable.description)
        binding.addNoteSpinner.setSelection(mSharedViewModel.parsePriority(args.currentItemParcelable.priority))

        binding.addNoteSpinner.onItemSelectedListener = mSharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> deleteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setNegativeButton("No"){ _, _ ->}
        builder.setPositiveButton("Yes"){ _, _ ->
            mTodoViewModel.deleteItem(args.currentItemParcelable)
            backToListing()
        }
        builder.setTitle("Delete '${args.currentItemParcelable.title}'?")
        builder.setMessage("Are you sure you want to remove this?")
        builder.create().show()
    }

    private fun updateItem() {
        val mTitle = binding.noteTitleEditText.text.toString()
        val mPriority = binding.addNoteSpinner.selectedItem.toString()
        val mDescription = binding.noteDescriptionEditText.text.toString()

        val valid = mSharedViewModel.validate(mTitle, mPriority, mDescription)

        if (!valid) return

        val dataUpdate = TodoData(
            args.currentItemParcelable.id,
            mTitle,
            mSharedViewModel.parsePriority(mPriority),
            mDescription
        )

        mTodoViewModel.updateData(dataUpdate)

        Toast.makeText(requireContext(), "TODO: updated message!", Toast.LENGTH_SHORT).show()
        //Go back, go crazy
        backToListing()
    }

    private fun backToListing(){
        findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    }

}