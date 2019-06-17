package com.example.otocashz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.CLTransferDetail;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.companion.reader.CLReaderCompanion;
import com.cashlez.android.sdk.model.CLPrintObject;
import com.cashlez.android.sdk.payment.CLDimoResponse;
import com.cashlez.android.sdk.payment.CLMandiriPayResponse;
import com.cashlez.android.sdk.payment.CLPaymentResponse;
import com.cashlez.android.sdk.payment.CLTCashQRResponse;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentService;
import com.cashlez.android.sdk.printing.ICLPrinterService;
import com.cashlez.android.sdk.sendreceipt.CLSendReceiptResponse;
import com.cashlez.android.sdk.sendreceipt.ICLSendReceiptHandler;
import com.cashlez.android.sdk.sendreceipt.ICLSendReceiptService;
import com.cashlez.android.sdk.service.CLPrintAlignEnum;
import com.cashlez.android.sdk.service.CLPrintEnum;

import java.util.ArrayList;

public class CZPaymentService extends AppCompatActivity implements ICLPaymentService, ICLPaymentHandler, ICLSendReceiptService, ICLPrinterService {


    @Override
    public void onReaderSuccess(CLReaderCompanion clReaderCompanion) {

    }

    @Override
    public void onReaderError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onPrinterSuccess(CLPrinterCompanion clPrinterCompanion) {

    }

    @Override
    public void onPrinterError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onInsertCreditCard(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void onInsertOrSwipeDebitCard(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void onSwipeDebitCard(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void onCashPaymentSuccess(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void onCashPaymentError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onPaymentSuccess(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void onRemoveCard(String s) {

    }

    @Override
    public void onProvideSignatureRequest(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void onPaymentError(CLErrorResponse clErrorResponse, String s) {

    }

    @Override
    public void onProvideSignatureError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onTCashQRSuccess(CLTCashQRResponse cltCashQRResponse) {

    }

    @Override
    public void onTCashQRError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onCheckTCashQRStatusSuccess(CLTCashQRResponse cltCashQRResponse) {

    }

    @Override
    public void onCheckTCashQRStatusError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onDimoSuccess(CLDimoResponse clDimoResponse) {

    }

    @Override
    public void onDimoError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onCheckDimoStatusSuccess(CLDimoResponse clDimoResponse) {

    }

    @Override
    public void onCheckDimoStatusError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onCancelDimoSuccess(CLDimoResponse clDimoResponse) {

    }

    @Override
    public void onCancelDimoError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onMandiriPaySuccess(CLMandiriPayResponse clMandiriPayResponse) {

    }

    @Override
    public void onMandiriPayError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onCheckMandiriPayStatusSuccess(CLMandiriPayResponse clMandiriPayResponse) {

    }

    @Override
    public void onCheckMandiriPayStatusError(CLErrorResponse clErrorResponse) {

    }

    @Override
    public void onPaymentDebitTransferRequestConfirmation(CLTransferDetail clTransferDetail) {

    }

    @Override
    public void doConnectLocationProvider() {

    }

    @Override
    public void doStartPayment(ICLPaymentService iclPaymentService) {

    }

    @Override
    public void doProceedPayment(CLPayment clPayment) {

    }

    @Override
    public void doProceedTCashQRPayment(CLPayment clPayment) {

    }

    @Override
    public void doProceedDebitTransferPayment(CLPayment clPayment) {

    }

    @Override
    public void doConfirmDebitTransferPayment(boolean b) {

    }

    @Override
    public void doCheckReaderCompanion() {

    }

    @Override
    public void doCheckPrinterCompanion() {

    }

    @Override
    public void doProceedSignature(String s) {

    }

    @Override
    public void doStopUpdateLocation() {

    }

    @Override
    public void doUnregisterReceiver() {

    }

    @Override
    public void doCloseCompanionConnection() {

    }

    @Override
    public void doPrint(CLPaymentResponse clPaymentResponse) {

    }

    @Override
    public void doPrintFreeText(ArrayList<CLPrintObject> arrayList) {

    }

    @Override
    public void doCheckTCashQRStatus(CLTCashQRResponse cltCashQRResponse) {

    }

    @Override
    public void doPrintTCashQr(Bitmap bitmap) {

    }

    @Override
    public void doPrintDimoQr(Bitmap bitmap) {

    }

    @Override
    public void doPrintDimo(CLDimoResponse clDimoResponse) {

    }

    @Override
    public void doProceedDimoPayment(CLPayment clPayment) {

    }

    @Override
    public void doCheckDimoStatus(CLDimoResponse clDimoResponse) {

    }

    @Override
    public void doCancelDimo(CLDimoResponse clDimoResponse) {

    }

    @Override
    public void doPrintMandiriPayQr(Bitmap bitmap) {

    }

    @Override
    public void doPrintMandiriPay(CLMandiriPayResponse clMandiriPayResponse) {

    }

    @Override
    public void doProceedMandiriPayPayment(CLPayment clPayment) {

    }

    @Override
    public void doCheckMandiriPayStatus(CLMandiriPayResponse clMandiriPayResponse) {

    }

    @Override
    public void doLogout() {

    }

    @Override
    public void onSendReceiptSuccess(CLSendReceiptResponse clSendReceiptResponse) {

    }

    @Override
    public void onSendReceiptError(CLErrorResponse clErrorResponse) {

    }


    public CLPrintObject doPrintLogo(CLPrintObject clPrintObject) {
        clPrintObject = new CLPrintObject();
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo_oma_full);
        clPrintObject.setBitmap(largeIcon);
        clPrintObject.setFormat(CLPrintEnum.SMALL_LOGO);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        return clPrintObject;
    }

    public CLPrintObject doPrintCR(CLPrintObject clPrintObject) {
        clPrintObject.setFreeText("");
        clPrintObject.setFormat(CLPrintEnum.TITLE);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        return clPrintObject;
    }

    public CLPrintObject doPrintTitleCenter(CLPrintObject clPrintObject, String theText) {
        clPrintObject.setFreeText(theText);
        clPrintObject.setFormat(CLPrintEnum.TITLE);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        return clPrintObject;
    }

    public CLPrintObject doPrintTitleLeft(CLPrintObject clPrintObject, String theText) {
        clPrintObject.setFreeText(theText);
        clPrintObject.setFormat(CLPrintEnum.BOLD);
        clPrintObject.setAlign(CLPrintAlignEnum.LEFT);
        return clPrintObject;
    }

    public CLPrintObject doPrintAlignLeft(CLPrintObject clPrintObject, String theText) {
        clPrintObject.setFreeText(theText);
        clPrintObject.setFormat(CLPrintEnum.NORMAL_BIG);
        clPrintObject.setAlign(CLPrintAlignEnum.LEFT);
        return clPrintObject;
    }

    public CLPrintObject doPrintSmallLeft(CLPrintObject clPrintObject, String theText) {
        clPrintObject.setFreeText(theText);
        clPrintObject.setFormat(CLPrintEnum.NORMAL);
        clPrintObject.setAlign(CLPrintAlignEnum.LEFT);
        return clPrintObject;
    }

    public CLPrintObject doPrintAlignRight(CLPrintObject clPrintObject, String theText) {
        clPrintObject.setFreeText(theText);
        clPrintObject.setFormat(CLPrintEnum.NORMAL_BIG);
        clPrintObject.setAlign(CLPrintAlignEnum.RIGHT);
        return clPrintObject;
    }

    public CLPrintObject doPrintAlignCenter(CLPrintObject clPrintObject, String theText) {
        clPrintObject.setFreeText(theText);
        clPrintObject.setFormat(CLPrintEnum.NORMAL_BIG);
        clPrintObject.setAlign(CLPrintAlignEnum.CENTER);
        return clPrintObject;
    }

    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }

    public CLPrintObject doPrintLeftByLeft(CLPrintObject clPrintObject, String theText, String theValue, Integer theCenter) {
        theCenter = theCenter != null ? theCenter : 12;
        Integer leftSide = theCenter - 1;
        Integer rightSide = 24 - theCenter - 1;

        theText = (theText.length() > leftSide) ? theText.substring(0, leftSide) : padRight(theText, 10);
        theValue = (theValue.length() > rightSide) ? theValue.substring(0, rightSide) : theValue;

        clPrintObject.setFreeText(theText + ":" + theValue);
        clPrintObject.setFormat(CLPrintEnum.NORMAL_BIG);
        clPrintObject.setAlign(CLPrintAlignEnum.LEFT);
        return clPrintObject;
    }

    public CLPrintObject doPrintLeftByright(CLPrintObject clPrintObject, String theText, String theValue, Integer theCenter) {
        theCenter = theCenter != null ? theCenter : 12;
        Integer leftSide = theCenter - 1;
        Integer rightSide = 24 - theCenter - 1;

        theText = (theText.length() > leftSide) ? theText.substring(0, leftSide) : padRight(theText, 7);
        theValue = padLeft(theValue, 16);

        clPrintObject.setFreeText(theText + ":" + theValue);
        clPrintObject.setFormat(CLPrintEnum.NORMAL_BIG);
        clPrintObject.setAlign(CLPrintAlignEnum.LEFT);
        return clPrintObject;
    }




    @Override
    public void onPrintingSuccess(CLPrinterCompanion clPrinterCompanion) {

    }


    @Override
    public void onPrintingError(CLErrorResponse clErrorResponse) {

    }

}
