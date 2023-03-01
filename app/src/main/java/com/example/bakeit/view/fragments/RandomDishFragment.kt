package com.example.bakeit.view.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.example.bakeit.R
import com.example.bakeit.application.BakeitApplication
import com.example.bakeit.databinding.ActivityAddUpdateDishBinding.inflate
import com.example.bakeit.databinding.ActivityMainBinding.inflate
import com.example.bakeit.databinding.FragmentRandomDishBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.model.entities.RandomDish
import com.example.bakeit.utils.Constants
import com.example.bakeit.viewmodel.BakeitViewModel
import com.example.bakeit.viewmodel.BakeitViewModelFactory
import com.example.bakeit.viewmodel.NotificationsViewModel
import com.example.bakeit.viewmodel.RandomDishViewModel

class RandomDishFragment : Fragment() {

    private var mBinding: FragmentRandomDishBinding? = null
    private lateinit var mRandomDishViewModel: RandomDishViewModel
    private  var mProgressDialog:Dialog?  = null

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
      mBinding = FragmentRandomDishBinding.inflate(inflater, container, false)

        return mBinding!!.root
    }

    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(requireActivity())
        mProgressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }
    private fun hideCustomProgressDialog(){
        mProgressDialog?.let {
            it.dismiss()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRandomDishViewModel = ViewModelProvider(this).get(RandomDishViewModel::class.java)

        mRandomDishViewModel.getRandomRecipeFromApi()
        randomDishViewModelObserver()

        mBinding!!.srlRandomDish.setOnRefreshListener {
            mRandomDishViewModel.getRandomRecipeFromApi()
        }
    }

    private fun randomDishViewModelObserver(){
        mRandomDishViewModel.randomDishResponse.observe(viewLifecycleOwner
        ) { randomDishResponse ->
            randomDishResponse?.let {
                Log.i("Random Dish Response", "${randomDishResponse.recipes[0]}")
                if(mBinding!!.srlRandomDish.isRefreshing){
                    mBinding!!.srlRandomDish.isRefreshing = false
                }
                setRandomDishResponseInUI(randomDishResponse.recipes[0])
            }
        }
        mRandomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner) { dataError ->
            dataError?.let {
                Log.e("Random Dish API ERROR", "${dataError}")
                if(mBinding!!.srlRandomDish.isRefreshing){
                    mBinding!!.srlRandomDish.isRefreshing = false
                }
            }
        }

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner
        ) { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Load Random Dish", "${loadRandomDish}")

                if(loadRandomDish && !mBinding!!.srlRandomDish.isRefreshing){
                    showCustomProgressDialog()
                }
                else hideCustomProgressDialog()
            }
        }
    }

    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe){
            Glide.with(requireActivity())
                .load(recipe.image)
                .centerCrop()
                .into(mBinding!!.ivDishImage)

        var dishType: String = "other"

        mBinding!!.tvTitle.text = recipe.title
        if(recipe.dishTypes.isNotEmpty()){
            dishType = recipe.dishTypes[0]
            mBinding!!.tvType.text = dishType
        }

        mBinding!!.tvCategory.text = "other"
        var ingredients = ""
        for(value in recipe.extendedIngredients){
            if(ingredients.isEmpty()){
                ingredients = value.original
            }else{
                ingredients = ingredients + ", \n" + value.original
            }
        }
        mBinding!!.tvIngredients.text = ingredients

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mBinding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        }else{
            @Suppress("DEPRECATION")
            mBinding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        mBinding!!.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(requireActivity(),R.drawable.ic_favorite_unselected)
        )

        var addedToFavorites = false

        mBinding!!.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipe.readyInMinutes.toString()
            )

        mBinding!!.ivFavoriteDish.setOnClickListener{
            if(addedToFavorites){
                Toast.makeText(requireActivity(),resources.getString(R.string.msg_already_added_to_favorites),Toast.LENGTH_SHORT).show()
            }
            else{
                val randomDishDetails = Bakeit(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )
                val mBakeitViewModel: BakeitViewModel by viewModels{
                    BakeitViewModelFactory((requireActivity().application as BakeitApplication).repository)
                }
                mBakeitViewModel.insert(randomDishDetails)

                addedToFavorites = true
                mBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
                Toast.makeText(requireActivity(),"Added to Favorites!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}