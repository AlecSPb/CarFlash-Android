package com.carflash.user.paystack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.carflash.user.PaystackResponseModel;
import com.carflash.user.R;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.tokenGenrator.TokenGenrator;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashMap;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.model.Card;
import co.paystack.android.model.Token;

public class PayWithPaystackActivity extends AppCompatActivity implements View.OnClickListener, ApiManager.APIFETCHER {

    private ProgressBar progressBar;
    private static final int GROUP_LEN = 4;
    private static final String TAG = "PayWithPaystackActivity";
    private EditText mEditCardNumber;
    private EditText mEditExpiryMonth;
    private EditText mEditExpiryYear;
    private EditText mEditCVV;
    private RelativeLayout pay_layout;
    private static final int REQUEST_CODE_PAYMENT = 133;
    private static final int REQUEST_RENTAL_CODE_PAYMENT = 144;
    private String tokenVal, tokenAdd;
    private Bundle bundle;
    TokenGenrator tokenGenrator;
    ApiManager apiManager;
    GsonBuilder gsonBuilder;
    Gson gson;
    String reference_number;
    int status;
    String received_token;
    private SessionManager sessionManager;
    String email;
    PaystackResponseModel responseModel;
    int result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_pay_with_paystack);

        PaystackSdk.initialize(this);
        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        apiManager = new ApiManager(this, this, this);
        sessionManager = new SessionManager(this);
         email = sessionManager.getEmail();
        Log.e("PAY_ISER_EMAIL---", email);


        //setup views
     //   mEditEmail = (EditText) findViewById(R.id.edit_email);
        mEditCardNumber = (EditText) findViewById(R.id.edit_card_number);
        mEditExpiryMonth = (EditText) findViewById(R.id.edit_expiry_month);
        mEditExpiryYear = (EditText) findViewById(R.id.edit_expiry_year);
        mEditCVV = (EditText) findViewById(R.id.edit_cvv);
        pay_layout = (RelativeLayout) findViewById(R.id.payLayout);
        progressBar = (ProgressBar)findViewById(R.id.pay_progress);

        //add text changed listener
        mEditCardNumber.addTextChangedListener(cardWatcher);
        mEditExpiryMonth.addTextChangedListener(new ExpiryWatcher(mEditExpiryMonth));
        mEditExpiryYear.addTextChangedListener(new ExpiryWatcher(mEditExpiryYear));

        //add clicklistener on pay layout
        pay_layout.setOnClickListener(this);



    }

    /**
     * Text Watcher to format card number
     */
    private TextWatcher cardWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String number = s.toString();
            if (number.length() >= GROUP_LEN) {
                String formatted = Utils.formatForViewing(number, GROUP_LEN);
                if (!number.equalsIgnoreCase(formatted)) {
                    mEditCardNumber.removeTextChangedListener(cardWatcher);
                    mEditCardNumber.setText(formatted);
                    mEditCardNumber.setSelection(formatted.length());
                    mEditCardNumber.addTextChangedListener(cardWatcher);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {

        if (REQUEST_CODE_PAYMENT == 133) {
//validate fields
            //String number, Integer expiryMonth, Integer expiryYear, String cvcT
            try {
                progressBar.setVisibility(View.VISIBLE);
                String number = Utils.cleanNumber(mEditCardNumber.getText().toString().trim());
                int expiryMonth = Integer.parseInt(mEditExpiryMonth.getText().toString().trim());
                int expiryYear = Integer.parseInt(mEditExpiryYear.getText().toString().trim());
                String cvv = mEditCVV.getText().toString().trim();

                Card card = new Card.Builder(number, expiryMonth, expiryYear, cvv).build();

                if (card.isValid()) {
                    //create token
                    createToken(card);

                    Log.e("CARD___", String.valueOf(card));

                } else {
                    Toast.makeText(this, "Card not valid", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("**" + TAG, "" + e.getMessage());

            }
        }else {

            try {
                progressBar.setVisibility(View.VISIBLE);
                String number = Utils.cleanNumber(mEditCardNumber.getText().toString().trim());
                int expiryMonth = Integer.parseInt(mEditExpiryMonth.getText().toString().trim());
                int expiryYear = Integer.parseInt(mEditExpiryYear.getText().toString().trim());
                String cvv = mEditCVV.getText().toString().trim();

                Card card = new Card.Builder(number, expiryMonth, expiryYear, cvv).build();

                if (card.isValid()) {
                    //create token
                    createToken(card);

                    Log.e("CARD___", String.valueOf(card));

                } else {
                    Toast.makeText(this, "Card not valid", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("**" + TAG, "" + e.getMessage());

            }
        }
    }


    /**
     * Expiry Watcher
     */
    private class ExpiryWatcher implements TextWatcher {

        private EditText editText;

        public ExpiryWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try{
                int number = Integer.parseInt(s.toString());
                int length = s.length();
                switch (editText.getId()) {
                    case R.id.edit_expiry_month:
                        if(length == 1) {
                            if(number > 1) {
                                //add a 0 in front
                                setText("0"+number);
                            }
                        } else {
                            if(number > 12) {
                                setText("12");
                            }

                            //request focus on the next field
                            mEditExpiryYear.requestFocus();
                        }
                        break;
                    case R.id.edit_expiry_year:
                        String stringYear = (Calendar.getInstance().get(Calendar.YEAR) + "").substring(2);
                        int currentYear = Integer.parseInt(stringYear);

                        if(length == 1) {
                            int firstDigit = Integer.parseInt(String.valueOf(currentYear).substring(0, length));
                            if(number < firstDigit) {
                                setText(firstDigit+"");
                            }
                        } else {
                            if(number < currentYear){
                                setText(currentYear+"");
                            }

                            mEditCVV.requestFocus();
                        }
                        break;
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
        }

        private void setText(String text) {
            editText.setText(text);
            editText.setSelection(editText.getText().toString().trim().length());
        }
    }

    private void createToken(Card card) {


        //show progress
        PaystackSdk.createToken(card, new Paystack.TokenCallback() {
            @Override
            public void onCreate(Token token) {
                if(token != null) {
                    //dismiss progress

                    tokenVal = token.last4;
                    tokenAdd = token.token;

                    Log.e("**"+TAG, String.valueOf(token.last4+token.token));
                    SendTokenToServer(email, token.last4+token.token, getIntent().getStringExtra("pay_amount"));


                }else{
                    Toast.makeText(PayWithPaystackActivity.this, "Null Token ", Toast.LENGTH_SHORT).show();
                }

                //send the token to the server and then pass the result to previous activty

            }


            @Override
            public void onError(Exception error) {
                //dismiss progress, show error to the user
                Log.e("**"+TAG, String.valueOf(error));

            }
        });
    }

    private void SendTokenToServer(String userEmail, String token, String pay_amount) {
        Log.e("USER_EMAIL---", userEmail);
        Log.e("USER_TOKEN---", token);
        Log.e("USER_PAYAMOUNT---", pay_amount);


        received_token = token;

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("reference_token",received_token);
        hashMap.put("email",userEmail);
        hashMap.put("amount",pay_amount);

        Log.d("**HASHMAP===", String.valueOf(hashMap));

      //  apiManager.execution_method_get(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK,  Apis.PAY_WITH_PAYSTACK+"reference_token="+received_token+"&email="+userEmail+"&amount="+pay_amount, true, ApiManager.ACTION_SHOW_DIALOG);
        progressBar.setVisibility(View.GONE);
        apiManager.execution_paystack(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK,  Apis.PAY_WITH_PAYSTACK, hashMap, true, ApiManager.ACTION_SHOW_DIALOG);

      //  Log.e("**PAY_API==", Apis.PAY_WITH_PAYSTACK+"reference_token="+received_token+"&email="+userEmail+"&amount="+pay_amount);
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        Log.d("**OBJECTSCRIPT_PAYMENT--", String.valueOf(script));

        if(APINAME.equals(Config.ApiKeys.KEY_PAY_WITH_PAYSTACK))
        {
           // progressBar.setVisibility(View.GONE);
            tokenGenrator=gson.fromJson(""+script,TokenGenrator.class);
            /* status = tokenGenrator.getResult();

            String message = tokenGenrator.getMessage().toString();
            Log.e("Message",""+message);
            Log.d("**STATUS_CARD_PAYMENT--", String.valueOf(status));

            reference_number = tokenGenrator.getData().getReference();
           Log.d("**REFERENCE_NUMBER---", reference_number);

          //  if (!reference_number.equals(null) || status == false){
                if (status == 1){

                try {

                    HashMap<String, String> body = new HashMap<String, String>();
                    body.put("token", received_token);
                    body.put("user_id", getIntent().getStringExtra("user_id"));

                apiManager.execution_method_post(Config.ApiKeys.CONFIRM_PAYMENT,  Apis.CONFIRM_PAYMENT, body, true, ApiManager.ACTION_SHOW_DIALOG);
                    Log.e("**API_CONFIRM_PAYMENT===",Apis.CONFIRM_PAYMENT+"token="+received_token);
                    Log.d("**USER_ID===", getIntent().getStringExtra("user_id"));
                    finilalizeActivity();
                }catch (Exception e){
                    Toast.makeText(this, "PAY API NOT RUNNING---", Toast.LENGTH_SHORT).show();
                }
            }else {

                Toast.makeText(this, "Invalid Credentials !", Toast.LENGTH_SHORT).show();
            }*/
        }

        //confirm payment
        if(APINAME.equals(Config.ApiKeys.CONFIRM_PAYMENT)){
            Log.d("**OBJECTSCRIPT_CONFIRM_PAYMENT--", String.valueOf(script));
            responseModel = gson.fromJson("" + script, PaystackResponseModel.class);
            result = responseModel.getResult();

            Log.e("RESULT_CONFIRM_PAYMENT---", String.valueOf(result));
        }


    }
    @Override
    public void onFetchResultZero(String s) {

    }


    private void finilalizeActivity() {

        if (REQUEST_CODE_PAYMENT == 133){
            //for now send the dummy response to the previous activity to get the payment done
            Intent intent = new Intent();
            intent.putExtra("response", status);
            intent.putExtra("payment_result", result);
            setResult(REQUEST_CODE_PAYMENT, intent);
            finish();
    }else {
            Intent intent = new Intent();
            intent.putExtra("response", status);
            intent.putExtra("payment_result", result);
            setResult(REQUEST_RENTAL_CODE_PAYMENT, intent);
            finish();
        }
    }

}
