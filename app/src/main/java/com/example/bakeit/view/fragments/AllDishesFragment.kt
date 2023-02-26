package com.example.bakeit.view.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bakeit.R
import com.example.bakeit.application.BakeitApplication
import com.example.bakeit.databinding.DialogCustomListBinding
import com.example.bakeit.databinding.FragmentAllDishesBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.utils.Constants
import com.example.bakeit.view.activities.AddUpdateDishActivity
import com.example.bakeit.view.activities.MainActivity
import com.example.bakeit.view.adapters.BakeitAdapter
import com.example.bakeit.view.adapters.CustomListItemAdapter
import com.example.bakeit.viewmodel.BakeitViewModel
import com.example.bakeit.viewmodel.BakeitViewModelFactory
//import com.example.bakeit.databinding.FragmentHomeBinding
import com.example.bakeit.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

     private lateinit var mBinding:FragmentAllDishesBinding
     private  lateinit var mBakeitAdapter: BakeitAdapter
    private  lateinit var mCustomListDialog: Dialog

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
         mBakeitAdapter = BakeitAdapter(this@AllDishesFragment)   // object of adapter we just created

        mBinding.rvDishesList.adapter = mBakeitAdapter

        mBakeitViewModel.allDishesList.observe(viewLifecycleOwner){
            dishes ->
            dishes.let {
                if (it.isNotEmpty()){
                    mBinding.rvDishesList.visibility = View.VISIBLE     // if not empty atleast one dish is in dataset show list
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE  // and disable the visibilty of text that needs to be shown if not data is present in dataset
                    mBakeitAdapter.dishesList(it)
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
    fun deleteDish(dish: Bakeit){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Delete Dish")
        builder.setMessage("Are you sure you want to delete ${dish.title}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){ dialogInterface,_ ->
            mBakeitViewModel.delete(dish)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){ dialogInterface,_ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private  fun filterDishesListDialog(){
         mCustomListDialog = Dialog(requireActivity())
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = resources.getString(R.string.title_select_item_to_filter)

        val dishTypes = Constants.dishTypes()
        dishTypes.add(0,Constants.ALL_ITEMS)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = CustomListItemAdapter(requireActivity(),this@AllDishesFragment,dishTypes,Constants.FILTER_SELECTION)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()

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
            R.id.action_filter_dish -> {
                filterDishesListDialog()
                return  true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun  filterSelection(filterItemSelection: String){
        mCustomListDialog.dismiss()
        Log.i("Filter Selection", filterItemSelection)

        if(filterItemSelection == Constants.ALL_ITEMS){
            mBakeitViewModel.allDishesList.observe(viewLifecycleOwner){
                    dishes ->
                dishes.let {
                    if (it.isNotEmpty()){
                        mBinding.rvDishesList.visibility = View.VISIBLE     // if not empty atleast one dish is in dataset show list
                        mBinding.tvNoDishesAddedYet.visibility = View.GONE  // and disable the visibilty of text that needs to be shown if not data is present in dataset
                        mBakeitAdapter.dishesList(it)
                    }else{
                        mBinding.rvDishesList.visibility = View.GONE
                        mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                    }
                }
            }
        }else{
            mBakeitViewModel.getFilteredList(filterItemSelection).observe(viewLifecycleOwner){
                dishes ->
                    dishes.let {
                        if(it.isNotEmpty()){
                            mBinding.rvDishesList.visibility = View.VISIBLE
                            mBinding.tvNoDishesAddedYet.visibility = View.GONE
                            mBakeitAdapter.dishesList(it)
                        }else{
                            mBinding.rvDishesList.visibility = View.GONE
                            mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}