package com.example.bakeit.view.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.example.bakeit.R
import com.example.bakeit.databinding.ActivityAddUpdateDishBinding
import com.example.bakeit.databinding.DialogCustomImageSelectionBinding

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener{

    private lateinit var  mBinding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(mBinding.root)
        setActionBar()
        mBinding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mBinding.toolbarAddDishActivity.setNavigationOnClickListener{
            onBackPressed()
        }

    }

    override fun onClick(v: View?) {
            if(v != null){
                when(v.id){
                    R.id.iv_add_dish_image -> {
                        customImageSelectionDialog()
//                        Toast.makeText(this, "Tappped on add image icon", Toast.LENGTH_SHORT).show()
                    return
                    }
                }
            }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding:DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        binding.tvCamera.setOnClickListener{
            Toast.makeText(this, "Tapped On Camera", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener{
            Toast.makeText(this, "Tapped On Gallery", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)

        dialog.show()
    }
}