package com.outrowender.todo.fragments.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import com.outrowender.todo.R
import com.outrowender.todo.data.models.TodoData
import com.outrowender.todo.data.viewModel.TodoViewModel
import com.outrowender.todo.databinding.FragmentListBinding
import com.outrowender.todo.fragments.SharedViewModel
import com.outrowender.todo.fragments.list.adapter.ListAdapter
import com.outrowender.todo.fragments.utils.hideKeyboard
import com.outrowender.todo.fragments.utils.observeOnce
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(), SearchView.OnQueryTextListener {

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

        mTodoViewModel.getAllData.observe(viewLifecycleOwner) {
            mSharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        }

        setHasOptionsMenu(true)

        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }

        swipeToDelete(binding.recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                // Delete Item
                mTodoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                // Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: TodoData) {
        val snackBar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            mTodoViewModel.insertData(deletedItem)
        }
        snackBar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        fun andSetData(it: List<TodoData>) {
            adapter.setData(
                it
            )
        }

        when (item.itemId) {
            R.id.menu_delete_all -> mTodoViewModel.deleteAll()
            R.id.menu_priority_high -> mTodoViewModel.sortByHighPriority.observe(
                viewLifecycleOwner,
                ::andSetData
            )
            R.id.menu_priority_low -> mTodoViewModel.sortByLowPriority.observe(
                viewLifecycleOwner,
                ::andSetData
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mTodoViewModel.searchDatabase(searchQuery).observeOnce(viewLifecycleOwner) { list ->
            list?.let {
                Log.d("ListFragment", "searchThroughDatabase")
                adapter.setData(it)
            }
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchThroughDatabase(query)
        }
        return true
    }

    //important AF
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}