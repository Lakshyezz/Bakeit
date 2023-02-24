package com.example.bakeit.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bakeit.R
import com.example.bakeit.application.BakeitApplication
import com.example.bakeit.databinding.FragmentFavoriteDishesBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.view.activities.MainActivity
import com.example.bakeit.view.adapters.BakeitAdapter
import com.example.bakeit.viewmodel.BakeitViewModel
import com.example.bakeit.viewmodel.BakeitViewModelFactory
import com.example.bakeit.viewmodel.DashboardViewModel

class FavoriteDishesFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val bakeitViewModel: BakeitViewModel by viewModels{
        BakeitViewModelFactory((requireActivity().application
                as BakeitApplication).repository)
    }

    private var binding:FragmentFavoriteDishesBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteDishesBinding.inflate(inflater,container,false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bakeitViewModel.favoriteDishes.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {
            binding!!.rvFavoriteDishesList.layoutManager =
                GridLayoutManager(requireActivity(),2)
            val adapter = BakeitAdapter(this)
                binding!!.rvFavoriteDishesList.adapter = adapter

               if (it.isNotEmpty()){
                   binding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                   binding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE
                   adapter.dishesList(it)
               }else{
                   binding!!.rvFavoriteDishesList.visibility = View.GONE
                   binding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
               }
        }
        }
    }

    fun dishDetails(bakeit: Bakeit){
        if(requireActivity() is MainActivity){
            (activity as MainActivity?) !!.hideBottomNavigationView()   // to hide bottom navigation bar once favorite tab is open
        }
        findNavController().navigate(FavoriteDishesFragmentDirections
            .actionFavoriteDishesToDishDetails(bakeit))  // line to navigate it from one fragment to detailfragment
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }
}

