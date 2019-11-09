package com.example.front

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_enroll_marker.*
import android.util.Log
import android.view.View
import android.content.Intent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import android.R
import androidx.appcompat.app.AlertDialog
import javax.swing.text.StyleConstants.setIcon





class EnrollMarkerActivity : AppCompatActivity() {
    private val GET_GALLERY_IMAGE = 200
    private lateinit var  addImageButton: ImageView
    private lateinit var addresNname : TextView
    private lateinit var imageview:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroll_marker)

        addresNname = addressname
        addImageButton = addImagebutton
        imageview=imageView
        val intents = intent

        val d1 = intents.getStringExtra("marker.lat")
        val d2 = intents.getStringExtra("marker.long")
        val addr =intents.getStringExtra("marker.addr")
        Log.d("인텐트 맵좌표","좌표: 위도(" + d1!!.toString()+ "),경도(" + (d2!!.toString()) + ")"+addr!!.toString())
        addressname.text=addr

        //TedPermission 라이브러리 -> 카메라 권한 획득
        val permissionlistener = object : PermissionListener {
            override fun onPermissionGranted() {
                Toast.makeText(this@EnrollMarkerActivity, "Permission Granted", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: List<String>) {
                Toast.makeText(this@EnrollMarkerActivity, "Permission Denied\n$deniedPermissions", Toast.LENGTH_SHORT).show()
            }
        }

            TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setDeniedTitle("Permission denied")
            .setDeniedMessage(
            "If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
            .setGotoSettingButtonText("bla bla")
            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

                .check()





        addImageButton.setOnClickListener(object:View.OnClickListener{
            override  fun onClick(var1: View){
                makeDialog()
//                val intent = Intent()
//                intent.type = "image/*"
//                intent.action = Intent.ACTION_PICK
//                startActivityForResult(intent, GET_GALLERY_IMAGE)
            }
        })

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_GALLERY_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val selectedImageUri = data!!.data!!
                    imageview.setImageURI(selectedImageUri)
                } catch (e: Exception) {

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun makeDialog() {

        val alt_bld = AlertDialog.Builder(this@EnrollMarkerActivity, R.style.MyAlertDialogStyle)

        alt_bld.setTitle("사진 업로드").setIcon(R.drawable.check_dialog_64).setCancelable(

                false).setPositiveButton("사진촬영",

                DialogInterface.OnClickListener { dialog, id ->
                    Log.v("알림", "다이얼로그 > 사진촬영 선택")

                    // 사진 촬영 클릭

                    takePhoto()
                }).setNeutralButton("앨범선택",

                DialogInterface.OnClickListener { dialogInterface, id ->
                    Log.v("알림", "다이얼로그 > 앨범선택 선택")

                    //앨범에서 선택

                    selectAlbum()
                }).setNegativeButton("취소   ",

                DialogInterface.OnClickListener { dialog, id ->
                    Log.v("알림", "다이얼로그 > 취소 선택")

                    // 취소 클릭. dialog 닫기.

                    dialog.cancel()
                })

        val alert = alt_bld.create()

        alert.show()

    }


}
