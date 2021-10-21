package com.outrowender.todo.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.outrowender.todo.R
import com.outrowender.todo.data.viewModel.TodoViewModel
import com.outrowender.todo.databinding.FragmentListBinding
import com.outrowender.todo.fragments.SharedViewModel
import com.outrowender.todo.fragments.list.adapter.ListAdapter
import com.outrowender.todo.fragments.update.UpdateFragmentArgs

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val mTodoViewModel: TodoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setupRecyclerView()

        mTodoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            mSharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.listRecyclerView.adapter = adapter
        binding.listRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete_all) {
            mTodoViewModel.deleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    //important AF
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}