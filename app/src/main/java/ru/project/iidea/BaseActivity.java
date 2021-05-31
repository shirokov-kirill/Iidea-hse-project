package ru.project.iidea;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    public void showProgress(){
        if(mProgressDialog == null){
            mProgressDialog = new ProgressDialog(this, R.style.customDialog);
            mProgressDialog.setCancelable(false);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_dialog);
    }

    public void hideProgress(){
        if(mProgressDialog != null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    public void showError(String message, Exception error){
        showToast(message);
        Log.e("BaseActivity", error.toString());
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
