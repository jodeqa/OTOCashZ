package com.example.otocashz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cashlez.android.sdk.CLErrorResponse;


import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.companion.reader.CLReaderCompanion;
import com.cashlez.android.sdk.model.CLPrintObject;
import com.cashlez.android.sdk.payment.CLPaymentResponse;
import com.cashlez.android.sdk.payment.noncash.CLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentHandler;
import com.cashlez.android.sdk.printing.CLPrinterHandler;
import com.cashlez.android.sdk.printing.ICLPrinterService;
import com.cashlez.android.sdk.printing.ICLPrintingHandler;
import com.cashlez.android.sdk.service.CLPrintAlignEnum;
import com.cashlez.android.sdk.service.CLPrintEnum;

import java.util.ArrayList;


public class ConfigActivity extends AppCompatActivity implements ICLPrinterService {

    private static final String TAG = "ConfigActivity";

    TextView readerStatus;
    TextView printerStatus;

    protected ICLPaymentHandler paymentHandler;
    private ICLPrintingHandler printingHandler;

    private CLPaymentResponse data;
    private CLReaderCompanion readerCompanion;
    private CLPrinterCompanion printerCompanion;
    private CLErrorResponse errorResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        readerStatus = findViewById(R.id.txtReaderStatus);
        printerStatus = findViewById(R.id.txtPrinterStatus);


        paymentHandler = new CLPaymentHandler(this, savedInstanceState);
        printingHandler = new CLPrinterHandler(this);
    }

    public void doCheckReader(View view) {
        Log.d(TAG, "Check Device");
        paymentHandler.doCheckReaderCompanion();
    }


    public void doCheckPrinter(View view) {
        Log.d(TAG, "Check Printer");
        paymentHandler.doCheckPrinterCompanion();
    }

    public void doTestPrinter(View view) {
        ArrayList<CLPrintObject> freeText = new ArrayList<>();

        CLPrintObject clPrintObject = new CLPrintObject();
        clPrintObject.setFreeText("Rp. 999.999.999");
        clPrintObject.setFormat(CLPrintEnum.TITLE);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        freeText.add(clPrintObject);

        clPrintObject = new CLPrintObject();
        clPrintObject.setFreeText("Pembayaran Cicilan ke xx");
        clPrintObject.setFormat(CLPrintEnum.BOLD);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        freeText.add(clPrintObject);

        clPrintObject = new CLPrintObject();
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_oma_full_small);
        clPrintObject.setBitmap(largeIcon);
        clPrintObject.setFormat(CLPrintEnum.SMALL_LOGO);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        freeText.add(clPrintObject);

        printingHandler.doPrintFreeText(freeText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        doInitPrinter();
    }

    void doInitPrinter() {
        printingHandler.doInitPrinterConnection(this);
    }

    @Override
    public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {
        setPrinterSuccess(printerCompanion);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                printerStatus.setText(String.format("%s: %s\n%s: %s",
                        getString(R.string.printer_connection_status), String.valueOf(printerCompanion.isConnected()),
                        getString(R.string.message), printerCompanion.getMessage()));
            }
        });
    }

    private void setPrinterSuccess(CLPrinterCompanion printerCompanion) {
        this.printerCompanion = printerCompanion;
    }

    @Override
    public void onPrintingError(CLErrorResponse clErrorResponse) {
        onPrinterError(errorResponse);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                printerStatus.setText(errorResponse.getErrorMessage());
            }
        });
    }

    private void onPrinterError(CLErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
