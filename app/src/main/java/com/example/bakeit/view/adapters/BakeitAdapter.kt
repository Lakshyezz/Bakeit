package com.example.bakeit.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bakeit.databinding.ItemDishLayoutBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.view.fragments.AllDishesFragment
import com.example.bakeit.view.fragments.FavoriteDishesFragment

class BakeitAdapter(private val fragment: Fragment): RecyclerView.Adapter<BakeitAdapter.ViewHolder>() {

    private var dishes: List<Bakeit> = listOf()

    class  ViewHolder(view: ItemDishLayoutBinding): RecyclerView.ViewHolder(view.root){
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
                LayoutInflater.from(fragment.context),parent,false)
            return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title
        holder.itemView.setOnClickListener{
            if(fragment is AllDishesFragment){
                fragment.dishDetails(dish)
            }
            if(fragment is FavoriteDishesFragment){
                fragment.dishDetails(dish)
            }
        }
    }

    override fun getItemCount(): Int {
        return  dishes.size
    }

    fun  dishesList(list: List<Bakeit>){        // this time 'll not passing list normally like before but accessing it as observing to update data if dataset change
        dishes = list
        notifyDataSetChanged()          // like setstate it will updates UI as per change of data
    }
}