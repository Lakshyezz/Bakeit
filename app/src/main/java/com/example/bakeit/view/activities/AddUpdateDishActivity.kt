package com.example.bakeit.view.activities

import android.app.Dialog
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings;
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bakeit.R
import com.example.bakeit.application.BakeitApplication
import com.example.bakeit.databinding.ActivityAddUpdateDishBinding
import com.example.bakeit.databinding.DialogCustomImageSelectionBinding
import com.example.bakeit.databinding.DialogCustomListBinding
import com.example.bakeit.model.entities.Bakeit
import com.example.bakeit.utils.Constants
import com.example.bakeit.view.adapters.CustomListItemAdapter
import com.example.bakeit.viewmodel.BakeitViewModel
import com.example.bakeit.viewmodel.BakeitViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener{
    companion object{
        // TO store constant values // also to check which request we considering with the help of these constants
        private const val  CAMERA = 1
        private const val  GALLERY = 2
        private  const val  IMAGE_DIRECTORY = "FavDishImages"
    }

    private lateinit var  mBinding: ActivityAddUpdateDishBinding
    private var mImagePath: String = ""

    private var bakeitDetails: Bakeit? = null

    private lateinit var mCustomListDialog: Dialog

    private val mBakeitViewModel: BakeitViewModel by viewModels{
            BakeitViewModelFactory((application as BakeitApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        if(intent.hasExtra(Constants.EXTRA_DISH_DETAILS)){
            bakeitDetails = intent.getParcelableExtra(Constants.EXTRA_DISH_DETAILS)
        }

        setUpActionBar()
        bakeitDetails?.let {
            if(it.id != 0){
                mImagePath = it.image
                Glide.with(this@AddUpdateDishActivity)
                    .load(mImagePath)
                    .centerCrop()
                    .into(mBinding.ivDishImage)
                mBinding.etTitle.setText(it.title)
                mBinding.etType.setText(it.type)
                mBinding.etCategory.setText(it.category)
                mBinding.etIngredients.setText(it.ingredients)
                mBinding.etCookingTime.setText(it.cookingTime)
                mBinding.etDirectionsToCook.setText(it.directionsToCook)
                mBinding.btnAddDish.text = resources.getString(R.string.lbl_update_dish)
            }
        }

        mBinding.ivAddDishImage.setOnClickListener(this)

        mBinding.etType.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)
        mBinding.btnAddDish.setOnClickListener(this)
    }

    private fun setUpActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)
        if(bakeitDetails != null && bakeitDetails!!.id  != 0){
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_edit_dish)
            }
        }else{
            supportActionBar?.let {
                it.title = resources.getString(R.string.title_add_dish)
            }
        }

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
                    R.id.et_type -> {
                        customItemsListDialog(resources.getString(R.string.title_select_dish_type)
                            ,Constants.dishTypes(),Constants.DISH_TYPE)
                        return
                    }
                    R.id.et_category -> {
                        customItemsListDialog(resources.getString(R.string.title_select_dish_category)
                            ,Constants.dishCategories(),Constants.DISH_CATEGORY)
                        return
                    }
                    R.id.et_cooking_time -> {
                        customItemsListDialog(resources.getString(R.string.title_select_dish_cooking_time)
                            ,Constants.dishCookTime(),Constants.DISH_COOKING_TIME)
                        return
                    }
                    R.id.btn_add_dish -> {
                        val title = mBinding.etTitle.text.toString().trim{ it <= ' ' }
                        val type = mBinding.etType.text.toString().trim{ it <= ' ' }
                        val category = mBinding.etCategory.text.toString().trim{ it <= ' ' }
                        val ingredients = mBinding.etIngredients.text.toString().trim{ it <= ' ' }
                        val cookingTimeInMinutes = mBinding.etCookingTime.text.toString().trim{ it <= ' ' }
                        val cookingDirection = mBinding.etDirectionsToCook.text.toString().trim{ it <= ' ' }

                        when{
                            TextUtils.isEmpty(mImagePath) ->{
                                Toast.makeText(this@AddUpdateDishActivity,"Select Image",Toast.LENGTH_SHORT
                                ).show()
                            }
                            TextUtils.isEmpty(title) ->{
                                Toast.makeText(this@AddUpdateDishActivity,"Fill Title",Toast.LENGTH_SHORT
                                ).show()
                            }
                            TextUtils.isEmpty(type) ->{
                                Toast.makeText(this@AddUpdateDishActivity,"Select Type of dish",Toast.LENGTH_SHORT
                                ).show()
                            }
                            TextUtils.isEmpty(category) ->{
                            Toast.makeText(this@AddUpdateDishActivity,"Select Category",Toast.LENGTH_SHORT
                            ).show()
                            }
                            TextUtils.isEmpty(ingredients) ->{
                            Toast.makeText(this@AddUpdateDishActivity,"Fill Ingredients Used",Toast.LENGTH_SHORT
                            ).show()
                        }
                            TextUtils.isEmpty(cookingTimeInMinutes) ->{
                                Toast.makeText(this@AddUpdateDishActivity,"Select Cooking Time",Toast.LENGTH_SHORT
                                ).show()
                            }
                            TextUtils.isEmpty(cookingDirection) ->{
                                Toast.makeText(this@AddUpdateDishActivity,"Fill Directions to Cook",Toast.LENGTH_SHORT
                                ).show()
                            }
                        else -> {
                                var dishId = 0
                            var imageSource = Constants.DISH_IMAGE_SOURCE_LOCAL
                            var favoriteDish = false

                            bakeitDetails?.let {
                                if(it.id != 0){
                                    dishId = it.id
                                    imageSource = it.imageSource
                                    favoriteDish = it.favoriteDish
                                }
                            }

                            val dishDetails: Bakeit = Bakeit(
                                mImagePath,
                                imageSource,
                                title,
                                type,
                                category,
                                ingredients,
                                cookingTimeInMinutes,
                                cookingDirection,
                                favoriteDish,
                                dishId
                            )
                            if(dishId == 0){
                                mBakeitViewModel.insert(dishDetails)
                                Toast.makeText(this@AddUpdateDishActivity
                                    ,"You Successfully added your dish details."
                                    ,Toast.LENGTH_SHORT
                                ).show()
                                Log.i("Insertion", "Success")
                            }else{
                                mBakeitViewModel.update(bakeitDetails!!)
                            }
                            finish()
                        }

                        }
                    }
                }
            }
    }

    private fun customImageSelectionDialog(){
        val dialog = Dialog(this)
        val binding:DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        binding.tvCamera.setOnClickListener{
//            Toast.makeText(this, "Tapped On Camera", Toast.LENGTH_SHORT).show()
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object: MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    // it make sure that report is not empty
                  report?.let {
                      if(report.areAllPermissionsGranted()){
                      val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                          startActivityForResult(intent,CAMERA)
                      }
                  }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                   showRationalDialogForPermissions()
                }

             }
            ).onSameThread().check()

            mCustomListDialog.dismiss()
        }
        binding.tvGallery.setOnClickListener{
           Dexter.withContext(this@AddUpdateDishActivity).withPermission(
               Manifest.permission.READ_EXTERNAL_STORAGE,
           )
               .withListener(object : PermissionListener{
                   override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
//                       Toast.makeText(this@AddUpdateDishActivity, "You have the Gallery permissions now", Toast.LENGTH_SHORT).show()
                    val galleryIntent = Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                       startActivityForResult(galleryIntent, GALLERY)
                   }

                   override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                       Toast.makeText(this@AddUpdateDishActivity, "You have denied the storage permissions to select an image", Toast.LENGTH_SHORT).show()
                   }

                   override fun onPermissionRationaleShouldBeShown(
                       p0: PermissionRequest?,
                       p1: PermissionToken?
                   ) {
                       showRationalDialogForPermissions()
                   }


               }
               ).onSameThread().check()
            //END
            dialog.dismiss()
        }

        dialog.show()
    }

    fun selectedListItem(item: String, selection: String){
        when(selection){
            Constants.DISH_TYPE -> {
                mCustomListDialog.dismiss()
                mBinding.etType.setText(item)
            }
            Constants.DISH_CATEGORY -> {
                mCustomListDialog.dismiss()
                mBinding.etCategory.setText(item)
            }
            else -> {
                mCustomListDialog.dismiss()
                mBinding.etCookingTime.setText(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA){
               data?.extras?.let {
                   val thumbnail: Bitmap = data.extras!!.get("data") as Bitmap
                   mBinding.ivDishImage.setImageBitmap(thumbnail)
                   Glide.with(this).load(thumbnail)
                       .centerCrop()
                       .into(mBinding.ivDishImage)

                   mImagePath = saveImageToInternalStorage(thumbnail)
                    Log.i("ImagePath",mImagePath)
                   mBinding.ivAddDishImage.setImageDrawable(
                       ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
               }
            }
            if(requestCode == GALLERY){
                data?.let {
                    val selectedPhotoUri = data.data
//                    mBinding.ivDishImage.setImageURI(selectedPhotoUri)

                    Glide.with(this).load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                               Log.e("TAG","Error loading image",e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    mImagePath = saveImageToInternalStorage(bitmap)
                               Log.i("ImagePath",mImagePath)
                                }
                                return false
                            }

                        })
                        .into(mBinding.ivDishImage)

                    mBinding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                }
            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Toast.makeText(this@AddUpdateDishActivity,
                "Didn't pick any image from media",
                Toast.LENGTH_SHORT).show()
        }
    }
    // This complete method is to get a dialog box with some +ve/-ve buttons with certain message as mentions in the message
        // and this will help us to get to the user app settings and provide their permissions
    // AND THIS' LL RUN ONCE USER DENY TO GIVE ACCESS TO PERMISSION BEFORE
    private fun showRationalDialogForPermissions(){
           AlertDialog.Builder(this).setMessage("It loos like you have turned off permissions " +
                   "required for this feature. It can be enabled under Application Settings")
               .setPositiveButton("GO TO SETTINGS")
               {_,_ ->
                   try{
                       val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                       val uri = Uri.fromParts("package",packageName,null)
                       intent.data = uri
                       startActivity(intent)
                   }catch (e: ActivityNotFoundException){
                       e.printStackTrace()
                   }
               }
               .setNegativeButton("Cancel"){dialog,_ ->
                   dialog.dismiss()
               }.show()
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap):String{
        val wrapper = ContextWrapper(applicationContext) // basically provides context of the app to whom photo is concerned

        var file = wrapper.getDir(IMAGE_DIRECTORY,Context.MODE_PRIVATE) // gets the location of image   // file is like an object here to store info
        file = File(file,"${UUID.randomUUID()}.jpg") // overrides the existing object with the image and assign it to unique name/value

        try {
            val stream: OutputStream = FileOutputStream(file)   //sequence of data //OutputStream: writing data to a destination .. InputStream: Use to read data from a source
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return  file.absolutePath
    }

    private fun customItemsListDialog(title: String, itemsList: List<String>, selection: String){
         mCustomListDialog = Dialog(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding
            .inflate(LayoutInflater.from(this))
        mCustomListDialog.setContentView(binding.root)

        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListItemAdapter(this,itemsList,selection)
        binding.rvList.adapter = adapter
        mCustomListDialog.show()
    }
}