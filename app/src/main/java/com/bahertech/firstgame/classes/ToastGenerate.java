package com.bahertech.firstgame.classes;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bahertech.firstgame.R;

public class ToastGenerate {
    private static ToastGenerate ourInstance;
    private final Context context;

    public ToastGenerate (Context context) {
        this.context = context;
    }
    public static ToastGenerate getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new ToastGenerate(context);
        return ourInstance;
    }

    //pass message and message type to this method
    public void createToastMessage(int errorCode){

//inflate the custom layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout toastLayout = (LinearLayout) layoutInflater.inflate(android.R.layout.simple_list_item_1,null);
        TextView toastShowMessage = (TextView) toastLayout.findViewById(android.R.id.message);

        switch (errorCode){
            case 0 :
                Toast.makeText(context, context.getString(R.string.error_try_again), Toast.LENGTH_SHORT).show();
                break;
            case 1 :
                Toast.makeText(context, context.getString(R.string.error_try_again_later), Toast.LENGTH_SHORT).show();
                break;
            case 2 :
                Toast.makeText(context, context.getString(R.string.error_internet), Toast.LENGTH_SHORT).show();
                break;
            case 3 :
                Toast.makeText(context, context.getString(R.string.no_ad_inventory), Toast.LENGTH_SHORT).show();
                break;
        }
        /*switch (type){
            case 0:
                //if the message type is 0 fail toaster method will call
                createFailToast(toastLayout,toastShowMessage,message);
                break;
            case 1:
                //if the message type is 1 success toaster method will call
                createSuccessToast(toastLayout,toastShowMessage,message);
                break;

            case 2:
                createWarningToast( toastLayout, toastShowMessage, message);
                //if the message type is 2 warning toaster method will call
                break;
            default:
                createFailToast(toastLayout,toastShowMessage,message);

        }*/
    }

    //Failure toast message method
    private final void createFailToast(LinearLayout toastLayout,TextView toastMessage,String message){
        toastLayout.setBackgroundResource(R.drawable.shaptextviewtoasterror);
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        showToast(context,toastLayout);
    }

    //warning toast message method
    private final void createWarningToast( LinearLayout toastLayout, TextView toastMessage, String message) {
        toastLayout.setBackgroundResource(R.drawable.shaptextviewtoasterror1);
        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        showToast(context, toastLayout);
    }
    //success toast message method
    private final void createSuccessToast(LinearLayout toastLayout,TextView toastMessage,String message){
        toastLayout.setBackgroundResource(R.drawable.shaptextviewtoasterror2);

        toastMessage.setText(message);
        toastMessage.setTextColor(context.getResources().getColor(R.color.white));
        showToast(context,toastLayout);
    }

    private void showToast(Context context, View view){
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.TOP,0,0); // show message in the top of the device
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}