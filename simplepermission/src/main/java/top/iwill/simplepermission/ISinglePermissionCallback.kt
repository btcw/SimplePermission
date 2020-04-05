package top.iwill.simplepermission

interface ISinglePermissionCallback{
    /**
     * 单个授权的回调
     * @param res 结果 true:授权成功 ，false:授权失败
     */
    fun onPermissionResult(res:Boolean)
}