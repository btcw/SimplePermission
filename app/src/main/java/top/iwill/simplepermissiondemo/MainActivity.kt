package top.iwill.simplepermissiondemo

import android.Manifest
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import top.iwill.simplepermission.request

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            request(arrayOf(Manifest.permission.CALL_PHONE,Manifest.permission.ACCESS_COARSE_LOCATION),1){
                onGranted { toast("onGranted...$it") }
                onDenied { toast("onDenied:$it") }
            }
        }
        btn2.setOnClickListener {
            request(Manifest.permission.WRITE_EXTERNAL_STORAGE,2){res ->
                if (res) toast("WRITE_EXTERNAL_STORAGE granted...")
                else toast("WRITE_EXTERNAL_STORAGE denied...")
            }
        }

//        request(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1){
//            onGranted {grantedList->
//                //do with grantedList...
//            }
//            onDenied {deniedList->
//                //do with deniedList...
//            }
//        }
    }
}
