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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bakeit.R
import com.example.bakeit.databinding.ActivityAddUpdateDishBinding
import com.example.bakeit.databinding.DialogCustomImageSelectionBinding
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

            dialog.dismiss()
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
        dialog.setContentView(binding.root)

        dialog.show()
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

}