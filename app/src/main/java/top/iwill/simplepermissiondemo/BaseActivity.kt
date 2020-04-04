package top.iwill.simplepermissiondemo

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity(){

    fun toast(msg:String)= Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
}