package com.example.bakeit.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bakeit.R
import com.example.bakeit.view.activities.AddUpdateDishActivity
//import com.example.bakeit.databinding.FragmentHomeBinding
import com.example.bakeit.viewmodel.HomeViewModel

class AllDishesFragment : Fragment() {

//    private var _binding: inflater? = null

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
        homeViewModel  =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val root  = inflater.inflate(R.layout.fragment_all_dishes, container, false)


        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
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