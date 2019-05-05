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
import com.cashlez.android.sdk.payment.CLTCashQRResponse;
import com.cashlez.android.sdk.payment.CLTMoneyResponse;
import com.cashlez.android.sdk.payment.noncash.CLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentService;
import com.cashlez.android.sdk.printing.CLPrinterHandler;
import com.cashlez.android.sdk.printing.ICLPrinterService;
import com.cashlez.android.sdk.printing.ICLPrintingHandler;
import com.cashlez.android.sdk.service.CLPrintAlignEnum;
import com.cashlez.android.sdk.service.CLPrintEnum;

import java.util.ArrayList;


public class ConfigActivity extends AppCompatActivity implements ICLPaymentService, ICLPrinterService {

    private static final String TAG = "ConfigActivity";

    TextView readerStatus;
    TextView printerStatus;

    protected ICLPaymentHandler paymentHandler;
    private ICLPrintingHandler printingHandler;

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
    public void onReaderSuccess(final CLReaderCompanion clReaderCompanion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                readerStatus.setText(String.format("%s: %s\n%s: %s",
                        getString(R.string.reader_connection_status), String.valueOf(clReaderCompanion.isConnected()),
                        getString(R.string.message), clReaderCompanion.getMessage()));
            }
        });
    }

    @Override
    public void onReaderError(final CLErrorResponse clErrorResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                readerStatus.setText(clErrorResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onPrinterSuccess(final CLPrinterCompanion clPrinterCompanion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                printerStatus.setText(String.format("%s: %s\n%s: %s",
                        getString(R.string.printer_connection_status), String.valueOf(clPrinterCompanion.isConnected()),
                        getString(R.string.message), clPrinterCompanion.getMessage()));
            }
        });
    }

    @Override
    public void onPrinterError(final CLErrorResponse clErrorResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                printerStatus.setText(clErrorResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onInsertCreditCard(CLPaymentResponse clPaymentResponse) {
        Log.d(TAG, "Card Activity InsertCreditCard");
        //not implementing
    }

    @Override
    public void onInsertOrSwipeDebitCard(CLPaymentResponse clPaymentResponse) {
        Log.d(TAG, "Card Activity InsertOrSwipeDebitCard");
        //not implementing
    }

    @Override
    public void onCashPaymentSuccess(CLPaymentResponse clPaymentResponse) {
        //not implementing
    }

    @Override
    public void onCashPaymentError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onPaymentSuccess(CLPaymentResponse clPaymentResponse) {
        //not implementing
    }

    @Override
    public void onRemoveCard(String s) {
        Log.d(TAG, "Card Activity RemoveCard");
        //not implementing
    }

    @Override
    public void onProvideSignatureRequest(CLPaymentResponse clPaymentResponse) {
        //not implementing
    }

    @Override
    public void onPaymentError(CLErrorResponse clErrorResponse, String s) {
        //not implementing
    }

    @Override
    public void onProvideSignatureError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onTCashQRSuccess(CLTCashQRResponse cltCashQRResponse) {
        //not implementing
    }

    @Override
    public void onTCashQRError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onCheckTCashQRStatusSuccess(CLTCashQRResponse cltCashQRResponse) {
        //not implementing
    }

    @Override
    public void onCheckTCashQRStatusError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onTMoneySuccess(CLTMoneyResponse cltMoneyResponse) {
        //not implementing
    }

    @Override
    public void onTMoneyError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onCheckTMoneyStatusSuccess(CLTMoneyResponse cltMoneyResponse) {
        //not implementing
    }

    @Override
    public void onCheckTMoneyStatusError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onCancelTMoneySuccess(CLTMoneyResponse cltMoneyResponse) {
        //not implementing
    }

    @Override
    public void onCancelTMoneyError(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {
        Log.d(TAG, "Print Activity PrintingSuccess");
    }

    @Override
    public void onPrintingError(CLErrorResponse clErrorResponse) {
        Log.d(TAG, "Print Activity PrintingError");
    }
}
