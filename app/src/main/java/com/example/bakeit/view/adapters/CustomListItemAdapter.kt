package com.example.bakeit.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.bakeit.databinding.ItemCustomListBinding
import com.example.bakeit.view.activities.AddUpdateDishActivity
import com.example.bakeit.view.fragments.AllDishesFragment

class CustomListItemAdapter(
    private  val activity: Activity,
    private val fragment: Fragment?,
    private val listItems: List<String>,
    private val selection: String)
    : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>(){

    class ViewHolder(view: ItemCustomListBinding):RecyclerView.ViewHolder(view.root){
        val tvText = view.tvText

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCustomListBinding = ItemCustomListBinding
            .inflate(LayoutInflater.from(activity),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]

        holder.tvText.text = item

        holder.itemView.setOnClickListener{
            if(activity is AddUpdateDishActivity){  // it is on diff activity like adding or updating activity
                activity.selectedListItem(item, selection)
            }
            if(fragment is AllDishesFragment){      // it on other fragment for filtering item
                fragment.filterSelection(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return listItems.size
    }
}