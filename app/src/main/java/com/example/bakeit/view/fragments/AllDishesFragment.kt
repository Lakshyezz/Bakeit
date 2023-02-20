package com.example.bakeit.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bakeit.R
import com.example.bakeit.application.BakeitApplication
import com.example.bakeit.databinding.FragmentAllDishesBinding
import com.example.bakeit.databinding.ItemDishLayoutBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.view.activities.AddUpdateDishActivity
import com.example.bakeit.view.activities.MainActivity
import com.example.bakeit.view.adapters.BakeitAdapter
import com.example.bakeit.viewmodel.BakeitViewModel
import com.example.bakeit.viewmodel.BakeitViewModelFactory
//import com.example.bakeit.databinding.FragmentHomeBinding
import com.example.bakeit.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

       lateinit var mBinding:FragmentAllDishesBinding

    private val mBakeitViewModel: BakeitViewModel by viewModels{
        BakeitViewModelFactory((requireActivity().application as BakeitApplication).repository)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
//    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAllDishesBinding.inflate(inflater,container,false)
        return  mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(),2)
        val bakeitAdapter = BakeitAdapter(this@AllDishesFragment)   // object of adapter we just created

        mBinding.rvDishesList.adapter = bakeitAdapter

        mBakeitViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {
                if (it.isNotEmpty()){
                    mBinding.rvDishesList.visibility = View.VISIBLE     // if not empty atleast one dish is in dataset show list
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE  // and disable the visibilty of text that needs to be shown if not data is present in dataset
                    bakeitAdapter.dishesList(it)
                }else{
                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }

    fun dishDetails(bakeit: Bakeit){
        findNavController().navigate(AllDishesFragmentDirections
            .actionAllDishesToDishDetails(bakeit ))

        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.hideBottomNavigationView()
        }
    }

    override fun onResume() {
        super.onResume()
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish -> {
                startActivity(Intent(requireActivity(),AddUpdateDishActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}