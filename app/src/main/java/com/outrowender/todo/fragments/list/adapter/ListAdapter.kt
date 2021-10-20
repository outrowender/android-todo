package com.outrowender.todo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.outrowender.todo.R
import com.outrowender.todo.data.models.Priority
import com.outrowender.todo.data.models.TodoData
import com.outrowender.todo.databinding.RowLayoutBinding
import com.outrowender.todo.fragments.list.ListFragmentDirections

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var dataList = emptyList<TodoData>()

    class MyViewHolder (val binding: RowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //set click event listener for each row
        holder.binding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(action)
        }

        //set data of the card
        holder.binding.titleCardTxt.text = dataList[position].title
        holder.binding.descriptionTxt.text = dataList[position].description
        fun setCardWithColor(color: Int){
            return holder.binding.priorityIndicator.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, color))
        }

        when(dataList[position].priority){
            Priority.HIGH -> setCardWithColor(R.color.red)
            Priority.MEDIUM -> setCardWithColor(R.color.yellow)
            Priority.LOW -> setCardWithColor(R.color.green)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(todoData: List<TodoData>){
        this.dataList = todoData
        notifyDataSetChanged()
    }

}