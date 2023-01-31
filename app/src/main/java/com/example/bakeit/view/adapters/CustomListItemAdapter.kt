package com.example.bakeit.view.adapters

import android.app.Activity
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bakeit.databinding.ItemCustomListBinding
import com.example.bakeit.view.activities.AddUpdateDishActivity

class CustomListItemAdapter(
    private  val activity: Activity,
    private val listItems: List<String>,
    private val selection: String)
    : RecyclerView.Adapter<CustomListItemAdapter.ViewHolder>(){
    class ViewHolder(view: ItemCustomListBinding):RecyclerView.ViewHolder(view.root){
        val tvText = view.tvText

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}