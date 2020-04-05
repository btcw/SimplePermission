package top.iwill.simplepermissiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import top.iwill.simplepermission.IPermissionCallback;
import top.iwill.simplepermission.ISinglePermissionCallback;
import top.iwill.simplepermission.PermissionUtilKt;

public class MainJavaActivity extends AppCompatActivity implements IPermissionCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_java);
        PermissionUtilKt.request(this
                , new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1, this);
//        PermissionUtilKt.request(this
//                ,Manifest.permission.ACCESS_COARSE_LOCATION,1, listener);
    }

    @Override
    public void onDenied(@NotNull ArrayList<? extends String> arrayList) {

    }

    @Override
    public void onGranted(@NotNull ArrayList<? extends String> arrayList) {

    }
}
