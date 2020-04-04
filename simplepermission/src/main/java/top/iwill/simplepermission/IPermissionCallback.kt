package top.iwill.simplepermission

interface IPermissionCallback {
    /**
     * 多个请求成功部分的回调(没有则不会调用)
     * @param grantedPermissions 请求成功的列表
     */
    fun onGranted(grantedPermissions: ArrayList<out String>)
    /**
     * 多个请求失败部分的回调(没有则不会调用)
     * @param deniedPermissions 请求成功的列表
     */
    fun onDenied(deniedPermissions: ArrayList<out String>)
}