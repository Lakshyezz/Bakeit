package com.example.bakeit.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bakeit.R
import com.example.bakeit.databinding.ActivityAddUpdateDishBinding.inflate
import com.example.bakeit.databinding.ActivityMainBinding.inflate
import com.example.bakeit.databinding.FragmentRandomDishBinding
import com.example.bakeit.viewmodel.NotificationsViewModel

class RandomDishFragment : Fragment() {

    private var mBinding: FragmentRandomDishBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}