package com.example.otocashz;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class Utility {

    public static void displayInfo(final Activity context, final String message){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean mandatoryCheck(String text){
        /*check edittext length*/
        return text.trim().length() == 0;
    }

    public static void viewDialog(final Activity context, String msgTtl, String msgBody, String isBtn){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title dialog
        alertDialogBuilder.setTitle(msgTtl);

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage(msgBody)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setNeutralButton(isBtn,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        dialog.dismiss();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }
}
