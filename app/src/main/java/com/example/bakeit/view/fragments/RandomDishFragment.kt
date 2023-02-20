package com.example.bakeit.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bakeit.R
import com.example.bakeit.viewmodel.NotificationsViewModel

class RandomDishFragment : Fragment() {




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       return inflater.inflate(R.layout.fragment_random_dish, container, false)
    }

}