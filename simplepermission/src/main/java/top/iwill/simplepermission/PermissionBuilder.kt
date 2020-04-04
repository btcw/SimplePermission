package top.iwill.simplepermission

class PermissionBuilder {
    internal var onGranted: ((grantedPermissions: ArrayList<out String>) -> Unit)? = null
    internal var onDenied: ((deniedPermissions: ArrayList<out String>) -> Unit)? = null
    fun onGranted(function: ((grantedPermissions: ArrayList<out String>) -> Unit)?) {
        this.onGranted = function
    }

    fun onDenied(function: ((deniedPermissions: ArrayList<out String>) -> Unit)?) {
        this.onDenied = function
    }
}