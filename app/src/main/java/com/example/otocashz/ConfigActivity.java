package com.example.otocashz;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cashlez.android.sdk.CLErrorResponse;


import com.cashlez.android.sdk.CLTransferDetail;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.companion.reader.CLReaderCompanion;
import com.cashlez.android.sdk.model.CLPrintObject;
import com.cashlez.android.sdk.payment.CLDimoResponse;
import com.cashlez.android.sdk.payment.CLMandiriPayResponse;
import com.cashlez.android.sdk.payment.CLPaymentResponse;
import com.cashlez.android.sdk.payment.CLTCashQRResponse;
import com.cashlez.android.sdk.payment.noncash.CLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentService;
import com.cashlez.android.sdk.printing.CLPrinterHandler;
import com.cashlez.android.sdk.printing.ICLPrinterService;
import com.cashlez.android.sdk.printing.ICLPrintingHandler;
import com.cashlez.android.sdk.service.CLPrintAlignEnum;
import com.cashlez.android.sdk.service.CLPrintEnum;

import java.util.ArrayList;


public class ConfigActivity extends CZPaymentService implements ICLPrinterService {

    private static final String TAG = "ConfigActivity";

    TextView readerStatus;
    TextView printerStatus;

    protected ICLPaymentHandler paymentHandler;
    private ICLPrintingHandler printingHandler;

    private CLReaderCompanion readerCompanion = new CLReaderCompanion();
    private CLPrinterCompanion printerCompanion = new CLPrinterCompanion();
    private CLPaymentResponse paymentResponse = new CLPaymentResponse();
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
        ICLPaymentService paymentService = new CZPaymentService() {
            @Override
            public void onReaderSuccess(CLReaderCompanion clReaderCompanion) {
                readerCompanion = clReaderCompanion;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        readerStatus.setText("Reader Ready");
                    }
                });
            }

            @Override
            public void onReaderError(CLErrorResponse clErrorResponse) {
                errorResponse = clErrorResponse;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        readerStatus.setText(errorResponse.getErrorMessage());
                    }
                });
          }
        };
        paymentHandler.doStartPayment(paymentService);
        paymentHandler.doCheckReaderCompanion();
    }


    public void doCheckPrinter(View view) {
        Log.d(TAG, "Check Printer");
        ICLPaymentService paymentService = new CZPaymentService() {
            @Override
            public void onPrinterSuccess(CLPrinterCompanion clPrinterCompanion) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        printerStatus.setText("Printer Ready");
                    }
                });
            }

            @Override
            public void onPrinterError(CLErrorResponse clErrorResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        printerStatus.setText(errorResponse.getErrorMessage());
                    }
                });
            }

        };
        paymentHandler.doStartPayment(paymentService);
        paymentHandler.doCheckPrinterCompanion();
    }


    public void doTestPrinter(View view) {
        ArrayList<CLPrintObject> freeText = new ArrayList<>();
        CLPrintObject clPrintObject = new CLPrintObject();

        freeText.add(doPrintLogo(clPrintObject));


        clPrintObject = new CLPrintObject();
        freeText.add(doPrintTitleCenter(clPrintObject, "OTO TULANG BAWANG"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject, "TID:12345678"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignRight(clPrintObject, "MID:123456789012345"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject, "XXXX XXXX XXXX 8223"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject, "Narses Mariko"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintTitleLeft(clPrintObject,"SALE"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"REF NO", "123456789012", 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"BATCH", "123456", 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"DATE", "12345678901", 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"TIME", "12345678", 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"INVOICE NO", "123456", 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByright(clPrintObject,"TOTAL", "12345678901234", 10));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintTitleLeft(clPrintObject,"DESC"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject,"Kalimat ini 24 karakter."));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject,"Kalimat ini sepanjang 34 karakter."));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject,"Kalimat ini sepanjang 75 karakter yang akan terlipat menjadi beberapa baris"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintSmallLeft(clPrintObject,"Atau kita coba kalimat yang lebih panjang dengan panjang 97 karakter menggunakan kerapatan 38 cpi"));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

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
        setPrinterSuccess(printerCompanion);//ini mesti cl pak
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
        onPrinterError(errorResponse);//ini jg mesti cl pak
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                printerStatus.setText(errorResponse.getErrorMessage());
            }
        });
    }

    public void onPrinterError(CLErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public void doPaymentHist(View view) {
    }
}
