package top.iwill.simplepermission

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import java.util.*
import kotlin.collections.ArrayList

/**
 * @description:
 * @author: btcw
 * @date: 2020/4/2
 */
fun AppCompatActivity.request(permission: String, requestCode: Int, callback: ISinglePermissionCallback){
    request(permission, requestCode){
        callback.onPermissionResult(it)
    }
}

fun AppCompatActivity.request(permissions: Array<out String>, requestCode: Int, callback:IPermissionCallback){
    request(permissions,requestCode){
        onGranted { callback.onGranted(it) }
        onDenied { callback.onDenied(it) }
    }
}

fun AppCompatActivity.request(permission: String, requestCode: Int, callback: (res: Boolean) -> Unit) {
    request(arrayOf(permission), requestCode) {
        onGranted{ callback.invoke(true)}
        onDenied { callback.invoke(false) }
    }
}

fun AppCompatActivity.request(permissions: Array<out String>, requestCode: Int, listener: PermissionBuilder.() -> Unit) {
    val builder = PermissionBuilder().also(listener)
    if (hasPermission(permissions)) {
        builder.onGranted?.invoke(permissions.toList() as ArrayList<out String>)
        return
    }
    val fm = supportFragmentManager
    var fragment = fm.findFragmentByTag(PermissionFragment.TAG)
    if (null == fragment || (fragment is PermissionFragment).not()) {
        val t = fm.beginTransaction()
        val pf = PermissionFragment().apply {
            observe(requestCode) {
                onGranted { builder.onGranted?.invoke(it) }
                onDenied { builder.onDenied?.invoke(it) }
            }
        }
        t.add(pf, PermissionFragment.TAG)
        t.commitAllowingStateLoss()
        fm.executePendingTransactions()
        fragment = pf
    } else {
        (fragment as PermissionFragment).observe(requestCode) {
            onGranted { builder.onGranted?.invoke(it) }
            onDenied { builder.onDenied?.invoke(it) }
        }
    }
    fragment.requestPermissions(permissions, requestCode)
}

fun Context.hasPermission(permissions: Array<out String>): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
    val pkName = packageName
    permissions.forEach {
        val res = packageManager.checkPermission(it, pkName)
        if (PackageManager.PERMISSION_DENIED == res) return false
    }
    return true
}

internal fun PermissionFragment.observe(requestCode: Int, listener: PermissionBuilder.() -> Unit) {
    val builder = PermissionBuilder().also(listener)
    addRequestCallback(requestCode, object : IPermissionCallback {
        override fun onGranted(grantedPermissions: ArrayList<out String>) {
            builder.onGranted?.invoke(grantedPermissions)
        }


        override fun onDenied(deniedPermissions: ArrayList<out String>) {
            builder.onDenied?.invoke(deniedPermissions)
        }
    })
}




