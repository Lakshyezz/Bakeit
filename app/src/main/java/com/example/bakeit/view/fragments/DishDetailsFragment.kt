package com.example.bakeit.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bakeit.R
import com.example.bakeit.databinding.FragmentDishDetailsBinding
import java.io.IOException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DishDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DishDetailsFragment : Fragment() {
    private var binding:FragmentDishDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args:DishDetailsFragmentArgs by navArgs()

        args.let {
            try {
                Glide.with(requireActivity()).load(it.dishDetails.image)
                    .centerCrop().into(binding!!.ivDishImage)
            }catch(e:IOException) {
                e.printStackTrace()
            }

        binding!!.tvTitle.text = it.dishDetails.title
        binding!!.tvType.text = it.dishDetails.type.capitalize(Locale.ROOT)     // used to make first letter capital
            binding!!.tvCategory.text = it.dishDetails.category
            binding!!.tvIngredients.text = it.dishDetails.ingredients
            binding!!.tvCookingDirection.text = it.dishDetails.directionsToCook
            binding!!.tvCookingTime.text  =
                resources.getString(R.string
                .lbl_estimate_cooking_time,it.dishDetails.cookingTime)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}