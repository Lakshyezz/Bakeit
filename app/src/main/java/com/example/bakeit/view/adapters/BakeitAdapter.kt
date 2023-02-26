package com.example.bakeit.view.adapters

import android.content.Intent
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bakeit.R
import com.example.bakeit.databinding.ItemDishLayoutBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.utils.Constants
import com.example.bakeit.view.activities.AddUpdateDishActivity
import com.example.bakeit.view.fragments.AllDishesFragment
import com.example.bakeit.view.fragments.FavoriteDishesFragment

class BakeitAdapter(private val fragment: Fragment): RecyclerView.Adapter<BakeitAdapter.ViewHolder>() {

    private var dishes: List<Bakeit> = listOf()

    class  ViewHolder(view: ItemDishLayoutBinding): RecyclerView.ViewHolder(view.root){
        val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        val ibMore = view.ibMore
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
        holder.ibMore.setOnClickListener{
            val popup = PopupMenu(fragment.context, holder.ibMore)
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish){
                    val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS,dish)
                    fragment.requireActivity().startActivity(intent)
                }
                else if(it.itemId == R.id.action_delete_dish){
//                    Log.i("you have clicked delete", "${dish.title}")
                    if(fragment is AllDishesFragment){
                        fragment.deleteDish(dish)
                    }
                }
                true
            }
            popup.show()
        }
        if(fragment is AllDishesFragment){
            holder.ibMore.visibility = View.VISIBLE
        }
        else if(fragment is FavoriteDishesFragment){
            holder.ibMore.visibility = View.GONE
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