package com.example.otocashz;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.EditText;

import com.cashlez.android.sdk.CLCardProcessingMode;
import com.cashlez.android.sdk.CLErrorResponse;
import com.cashlez.android.sdk.CLMiniAtm;
import com.cashlez.android.sdk.CLMiniAtmTransferPay;
import com.cashlez.android.sdk.CLPayment;
import com.cashlez.android.sdk.CLPaymentCapability;
import com.cashlez.android.sdk.CLTransferDetail;
import com.cashlez.android.sdk.bean.ApprovalStatus;
import com.cashlez.android.sdk.companion.printer.CLPrinterCompanion;
import com.cashlez.android.sdk.companion.reader.CLReaderCompanion;
import com.cashlez.android.sdk.login.CLLoginHandler;
import com.cashlez.android.sdk.login.CLLoginResponse;
import com.cashlez.android.sdk.login.ICLLoginService;
import com.cashlez.android.sdk.model.CLPrintObject;
import com.cashlez.android.sdk.payment.CLPaymentResponse;
import com.cashlez.android.sdk.payment.CLVerificationMode;
import com.cashlez.android.sdk.payment.noncash.CLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentHandler;
import com.cashlez.android.sdk.payment.noncash.ICLPaymentService;
import com.cashlez.android.sdk.printing.CLPrinterHandler;
import com.cashlez.android.sdk.printing.ICLPrintingHandler;
import com.cashlez.android.sdk.sendreceipt.CLSendReceiptHandler;
import com.cashlez.android.sdk.sendreceipt.ICLSendReceiptHandler;
import com.cashlez.android.sdk.sendreceipt.ICLSendReceiptService;
import com.cashlez.android.sdk.service.CLErrorStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.cashlez.android.sdk.CLMiniAtmTransferPay.ALL_BANK_CODES;
import static com.example.otocashz.Utility.displayInfo;
import static com.example.otocashz.Utility.viewDialog;


public class UserActivity extends CZPaymentService implements ICLLoginService {

    private static final String TAG = "UserActivity";

    String compName,branchCode,branchName,modulId,modulSubId,roleLevel,roleName,userName,cashlezUser,cashlezPwd,contractTypeJson;
    String todayDate, todayTime;
    String cz_comp, cz_bank_accno, cz_bank_code, cz_bank_branch, cz_acceptedCards;

    TextView greetingTextView, theStatus;
    EditText theAmount, theDesc, theMail, thePhone;
    Spinner spinner;

    BluetoothAdapter bluetoothAdapter;
    final static int REQUEST_ENABLE_BT = 1;

    private CLLoginHandler loginHandler;
    private CLPayment payment;
    private CLPaymentResponse paymentResponse;
    protected ICLPaymentHandler paymentHandler;
    private ICLPrintingHandler printingHandler;

    private CLReaderCompanion readerCompanion = new CLReaderCompanion();
    private CLPrinterCompanion printerCompanion = new CLPrinterCompanion();
    private CLErrorResponse errorResponse;
    private CLMiniAtmTransferPay.CLTransferType transferType;

    private JSONArray ContractType;
    HashMap<Integer,String> spinnerMap = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Bundle bundle = getIntent().getExtras();
        compName = bundle.getString("CompName");
        branchCode = bundle.getString("BranchCode");
        branchName = bundle.getString("BranchName");
        modulId = bundle.getString("ModulId");
        modulSubId = bundle.getString("ModulSubId");
        roleLevel = bundle.getString("ComRoleLevelpCode");
        roleName = bundle.getString("RoleName");
        userName = bundle.getString("UserName");
        cashlezUser = bundle.getString("CashlezUser");
        cashlezPwd = bundle.getString("CashlezPwd");
        contractTypeJson = bundle.getString("ContractType");

        todayDate  = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        todayTime  = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        spinner = findViewById(R.id.spiContractType);
        spinner.setAdapter(spinnerAdapter(contractTypeJson));

        theAmount = findViewById(R.id.edCustDebit);
        theAmount.addTextChangedListener(new HelperTextWatcher(theAmount));

        theDesc = findViewById(R.id.edCustDesc);
        theMail = findViewById(R.id.edCustMail);
        thePhone = findViewById(R.id.edCustHP);

        theStatus = findViewById(R.id.txtStatus);
        theStatus.setText("..waiting..");

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            viewDialog(this, "BlueTooth", "BlueTooth not supported in this device", "YES");
        }else{
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

            //register BroadcastReceiver
            BroadcastReceiver newReceiver = new HelperBTStateChangedBroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }
            };

            registerReceiver(newReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }

        if (compName == "OTO") {
            cz_comp = getResources().getString(R.string.cz_nasabah_OTO);
            cz_bank_accno = getResources().getString(R.string.cz_bank_accno_OTO);
            cz_bank_code = getResources().getString(R.string.cz_bank_code_OTO);
            cz_bank_branch = getResources().getString(R.string.cz_bank_branch_OTO);
        } else {
            cz_comp = getResources().getString(R.string.cz_nasabah_SOF);
            cz_bank_accno = getResources().getString(R.string.cz_bank_accno_SOF);
            cz_bank_code = getResources().getString(R.string.cz_bank_code_OTO);
            cz_bank_branch = getResources().getString(R.string.cz_bank_branch_SOF);
        }

        greetingTextView = findViewById(R.id.greeting_text_view);
        greetingTextView.setText(userName);

        loginHandler = new CLLoginHandler(this, this);
        paymentHandler = new CLPaymentHandler(this, savedInstanceState);
        printingHandler = new CLPrinterHandler(this);
        paymentResponse = new CLPaymentResponse();

        executeLogin();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, "BlueTooth Turned On", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            }
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    public void onDebitTransferNotSupported() {
        displayInfo(this, getString(R.string.debit_transfer_not_supported));
    }

    public void onInitAutoDebitTransfer(List<String> acceptedCards) {
        transferType = CLMiniAtmTransferPay.CLTransferType.AUTO;
        cz_acceptedCards = acceptedCards.toString();
    }

    public void onInitManualDebitTransfer(String acceptedBankCode) {
        transferType = CLMiniAtmTransferPay.CLTransferType.MANUAL;
        if (!ALL_BANK_CODES.equals(acceptedBankCode)) {
            cz_bank_code = acceptedBankCode;
        }
    }

    public void doCheckReader() {
        Log.d(TAG, "Check Device");
        ICLPaymentService paymentService = new CZPaymentService() {
            @Override
            public void onReaderSuccess(CLReaderCompanion clReaderCompanion) {
                readerCompanion = clReaderCompanion;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theStatus.setText("Reader Ready");
                    }
                });
            }

            @Override
            public void onReaderError(CLErrorResponse clErrorResponse) {
                errorResponse = clErrorResponse;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theStatus.setText(errorResponse.getErrorMessage());
                    }
                });
            }
        };
        paymentHandler.doStartPayment(paymentService);
        paymentHandler.doCheckReaderCompanion();

        CLPaymentCapability paymentCapability = new CLPaymentCapability();
        CLMiniAtm miniAtm = paymentCapability.getMiniAtm();
        if (miniAtm == null) {
            onDebitTransferNotSupported();
            return;
        }

        CLMiniAtmTransferPay debitTransfer = miniAtm.getTransferPay();
        if (debitTransfer == null) {
            onDebitTransferNotSupported();
            return;
        }

        CLMiniAtmTransferPay.CLTransferType transferType = debitTransfer.getTransferType();
        if (transferType == CLMiniAtmTransferPay.CLTransferType.AUTO) {
            List<String> acceptedCards = new ArrayList<>();
            for (String bankCode : debitTransfer.getBanks()) {
                acceptedCards.add(bankCode.substring(0, 4));
            }
            onInitAutoDebitTransfer(acceptedCards);
        } else if (transferType == CLMiniAtmTransferPay.CLTransferType.MANUAL) {
            onInitManualDebitTransfer(debitTransfer.getTransferBankCodePreset());
        }

    }

    public void doCheckPrinter() {
        Log.d(TAG, "Check Printer");
        ICLPaymentService paymentService = new CZPaymentService() {
            @Override
            public void onPrinterSuccess(CLPrinterCompanion clPrinterCompanion) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theStatus.setText("Printer Ready");
                    }
                });
            }

            @Override
            public void onPrinterError(CLErrorResponse clErrorResponse) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        theStatus.setText(errorResponse.getErrorMessage());
                    }
                });
            }

        };
        paymentHandler.doStartPayment(paymentService);
        paymentHandler.doCheckPrinterCompanion();
    }

    public void goTransaction(View view)
    {
        Log.d(TAG, "Transaction Process");
        Toast.makeText(getApplicationContext(), "Making Payment", Toast.LENGTH_SHORT).show();

        if (theAmount.getText().toString().equalsIgnoreCase("") || theDesc.getText().toString().equalsIgnoreCase("")) {
            if (theAmount.getText().toString().equalsIgnoreCase("")) {
                theAmount.setHint(getResources().getString(R.string.err_mandatory));
                theAmount.setError(getResources().getString(R.string.err_mandatory));
            }
            if (theDesc.getText().toString().equalsIgnoreCase("")) {
                theDesc.setHint(getResources().getString(R.string.err_mandatory));
                theDesc.setError(getResources().getString(R.string.err_mandatory));
            }
        } else {
            if (! doMakePayment()) {
                viewDialog(this, "Warning!", "Transaction Failed", "OK");
            }
        }

    }

    public Boolean doMakePayment() {
        paymentHandler.doConnectLocationProvider();
        doCheckReader();

        if (theStatus.getText().toString() != "Reader Ready") {
            return false;
        }

        CLPayment payment = new CLPayment();
        payment.setAmount(theAmount.getText().toString());
        payment.setDescription(spinner.getSelectedItem().toString() + " | " + theDesc.getText().toString());
        payment.setCardProcessingMode(CLCardProcessingMode.LOCAL_CARD);
        payment.setVerificationMode(CLVerificationMode.PIN);

        CLTransferDetail transferDetail = new CLTransferDetail(cz_bank_code, cz_bank_accno);
        payment.setTransferDetail(transferDetail);

        doProceedDebitTransferPayment(payment);
        doConfirmDebitTransferPayment(true);
        //onPaymentDebitTransferRequestConfirmation(transferDetail);

        //payment.setImage(payment.getItemImage());

        paymentHandler.doProceedPayment(payment);
        if (paymentResponse != null) {
            paymentResponse.setEmailAddress(theMail.getText().toString().trim());
            paymentResponse.setHpNo(thePhone.getText().toString().trim());

            ICLSendReceiptHandler sendReceiptFlow = new CLSendReceiptHandler(this, this);
            paymentResponse.setEmailAddress(paymentResponse.getEmailAddress());
            paymentResponse.setHpNo(paymentResponse.getHpNo());
            paymentResponse.setHideLocation(false);
            paymentResponse.setEmailAddressChecked(true);
            paymentResponse.setHPChecked(true);

            sendReceiptFlow.doSendReceipt(paymentResponse);

            doCheckPrinter();

            if (theStatus.getText().toString() != "Printer Ready") {
                return false;
            } else {
                doPrintReceipt();
            }
        }
        paymentHandler.doStopUpdateLocation();
        return true;
    }

    public void doPrintReceipt() {
        ArrayList<CLPrintObject> freeText = new ArrayList<>();
        CLPrintObject clPrintObject = new CLPrintObject();

        freeText.add(doPrintLogo(clPrintObject));


        clPrintObject = new CLPrintObject();
        freeText.add(doPrintTitleCenter(clPrintObject, branchName));

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
        freeText.add(doPrintLeftByLeft(clPrintObject,"DATE", todayDate, 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"TIME", todayTime, 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByLeft(clPrintObject,"INVOICE NO", "123456", 12));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintLeftByright(clPrintObject,"TOTAL", theAmount.getText().toString(), 10));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintTitleLeft(clPrintObject,"DESC"));


        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintAlignLeft(clPrintObject,theDesc.getText().toString()));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        clPrintObject = new CLPrintObject();
        freeText.add(doPrintCR(clPrintObject));

        printingHandler.doPrintFreeText(freeText);
    }

    public void goLogOut(View view) {
        executeLogOut();
        finish();
    }


    public void goConfig(View view) {
        Toast.makeText(getApplicationContext(), "Device Configuration Test", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Config Device");
        Intent i = new Intent(getApplicationContext(), ConfigActivity.class);
        startActivity(i);
    }

    private void executeGetUserInfo() {
        //not Implemented Yet
    }

    private void executeLogin() {
        Log.d(TAG, "Login Device Initialized");
        loginHandler.doLogin("Oto", "123456");
    }

    private void executeLogOut() {
        Log.d(TAG, "LogOut Device");
        Toast.makeText(getApplicationContext(), "Logging Out", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(getApplicationContext(), Login.class);
        //startActivity(i);
    }

    private ArrayAdapter<String> spinnerAdapter(String contractTypeJson)
    {
        try
        {
            ContractType = new JSONArray(contractTypeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String[] spinnerArray = new String[ContractType.length()];
        for (int i = 0; i < ContractType.length(); i++)
        {
            try {
                JSONObject item = (JSONObject)ContractType.get(i);
                spinnerMap.put(i,item.getString("ParaSeq"));
                spinnerArray[i] = item.getString("ParaDesc");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return new ArrayAdapter(this, R.layout.spinner_contract_type,R.id.txt,spinnerArray);
    }

    @Override
    public void onStartActivation(String s) {
        //not implementing
    }

    @Override
    public void onLoginSuccess(CLLoginResponse clLoginResponse) {
        onLoginOK();
    }

    public void onLoginOK() {
        Toast.makeText(getApplicationContext(), "Device Login Accepted", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Device Login Accepted");
    }

    @Override
    public void onLoginError(CLErrorResponse clErrorResponse) {
        onLoginFailed(clErrorResponse.getErrorMessage());
    }

    public void onLoginFailed(String failedMessage) {
        Toast.makeText(getApplicationContext(), failedMessage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Login Device Failed");
        Log.d(TAG, failedMessage);
        executeLogOut();
    }

    @SuppressWarnings("unused")

    @Override
    public void onNewVersionAvailable(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onApplicationExpired(CLErrorResponse clErrorResponse) {
        //not implementing
    }

    @Override
    public void onReaderSuccess(final CLReaderCompanion readerCompanion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                theStatus.setText(String.format("%s: %s\n%s: %s",
                        getString(R.string.reader_connection_status), String.valueOf(readerCompanion.isConnected()),
                        getString(R.string.message), readerCompanion.getMessage()));
            }
        });
    }

    @Override
    public void onReaderError(final CLErrorResponse errorResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                theStatus.setText(errorResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onPrinterSuccess(final CLPrinterCompanion printerCompanion) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                theStatus.setText(String.format("%s: %s\n%s: %s",
                        getString(R.string.printer_connection_status), String.valueOf(printerCompanion.isConnected()),
                        getString(R.string.message), printerCompanion.getMessage()));
            }
        });
    }

    @Override
    public void onPrinterError(final CLErrorResponse errorResponse) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                theStatus.setText(errorResponse.getErrorMessage());
            }
        });
    }

    @Override
    public void onInsertCreditCard(CLPaymentResponse paymentResponse) {
        displayInfo(this, paymentResponse.getMessage());
    }

    @Override
    public void onInsertOrSwipeDebitCard(CLPaymentResponse paymentResponse) {
        displayInfo(this, paymentResponse.getMessage());
    }

    @Override
    public void onSwipeDebitCard(CLPaymentResponse paymentResponse) {
        displayInfo(this, paymentResponse.getMessage());
    }

    @Override
    public void onRemoveCard(final String removeCard) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayInfo(UserActivity.this, removeCard);
            }
        });
    }

    @Override
    public void onPaymentSuccess(CLPaymentResponse paymentResponse) {
        this.paymentResponse = paymentResponse;

        displayInfo(this, paymentResponse.getMessage());
        if (paymentResponse.getTransactionStatus() != null) {
            if (paymentResponse.getTransactionStatus() == ApprovalStatus.APPROVED.getCode()
                    || paymentResponse.getTransactionStatus() == ApprovalStatus.PENDING.getCode()) {
                displayInfo(this, "APPROVED/PENDING");
            }
        }
        finish();
    }

    @Override
    public void onPaymentError(CLErrorResponse errorResponse, String trxId) {
        //Log.d(TAG, "TransactionId: " + trxId);
        displayInfo(this, errorResponse.getErrorMessage());
        if (errorResponse.getErrorCode() == CLErrorStatus.DEBIT_TRANSFER_CANCELLED.getCode()) {
            finish();
        }
    }

    public void onConfirmDebitTransfer(CLTransferDetail transferDetail) {
        cz_bank_code = transferDetail.getDestBankCode();
        cz_bank_branch = transferDetail.getDestBankName();
        cz_bank_accno = transferDetail.getDestBankAccount();
        cz_comp = transferDetail.getDestAccHolderName();
    }
}


