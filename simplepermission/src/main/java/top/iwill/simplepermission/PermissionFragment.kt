package top.iwill.simplepermission

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

class PermissionFragment : Fragment() {

    companion object {
        const val TAG = "top.iwill.simplepermission.PermissionFragment"
    }

    private val callbackMap = hashMapOf<Int, IPermissionCallback>()

    fun addRequestCallback(requestCode: Int, callback: IPermissionCallback) {
        callbackMap.put(requestCode, callback)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
        permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        callbackMap.get(requestCode)?.let { callback ->
            val deniedPermissions = arrayListOf<String>()
            val grantedPermissions = arrayListOf<String>()
            grantResults.forEachIndexed { index, res ->
                if (PackageManager.PERMISSION_GRANTED != res)
                    deniedPermissions.add(permissions[index])
                else
                    grantedPermissions.add(permissions[index])
            }
            if (Lifecycle.State.DESTROYED != lifecycle.currentState)
                if (deniedPermissions.isEmpty())
                    callback.onGranted(grantedPermissions)
                else {
                    if (grantedPermissions.isNotEmpty())
                        callback.onGranted(grantedPermissions)
                    callback.onDenied(deniedPermissions)
                }
        }
    }
}