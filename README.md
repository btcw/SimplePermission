# 一行代码搞定Android权限申请

### 一、使用

#### 1.添加依赖

```gradle
implementation 'top.iwill.futuretools:simplepermission:1.0.0'
```

#### 2.代码中使用

##### a.单个权限请求

```kotlin
   request(Manifest.permission.WRITE_EXTERNAL_STORAGE,2){res ->
                if (res) toast("WRITE_EXTERNAL_STORAGE granted...")
                else toast("WRITE_EXTERNAL_STORAGE denied...")
            }
```

##### b.多个权限请求

**回调定义**

```kotlin
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
```

**使用**

```kotlin
  request(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION),1){
            onGranted {grantedList->
                //do with grantedList...
            }
            onDenied {deniedList->
                //do with deniedList...
            }
        }
```

### 二、源码分享

#### 1.请求实现原理

通过对当前activity添加一个不可见的fragment,然后使用fragment进行请求的发起和回调。

内部发起权限请求调用

```kotlin
 fragment.requestPermissions(permissions, requestCode)
```

内部权限请求fragment回调

```kotlin
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
```

#### 2.DSL配置

通过添加DSL配置让回调更加简洁，[DSL相关](https://juejin.im/post/5c4f106a6fb9a049de6dc410)

```kotlin
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
```

添加拓展函数

```kotlin
AppCompatActivity.request(permissions: Array<out String>, requestCode: Int, listener: PermissionBuilder.() -> Unit)
```

使用则十分简便

MainActivity.kt

```kotlin
  request(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),1){
            onGranted {grantedList->
                //do with grantedList...
            }
            onDenied {deniedList->
                //do with deniedList...
            }
        }
```

