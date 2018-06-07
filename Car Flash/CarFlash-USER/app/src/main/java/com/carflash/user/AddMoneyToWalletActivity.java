package com.carflash.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.carflash.user.adapter.CardsAdapter;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.AddWalletMoneyResponse;
import com.carflash.user.models.SaveCardResponse;
import com.carflash.user.models.ViewCard;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

public class AddMoneyToWalletActivity extends AppCompatActivity implements  ApiManager.APIFETCHER{

    EditText ed_enter_money ;
    TextView amount_first , amount_second , amount_third , Add_Money , done ;
    LinearLayout ll_credit_card , ll_saved_cards ;
    EditText edt_card_number, edt_expiry, edt_cvv;
    String a;
    int keyDel;
    SessionManager sessionManager;
    LanguageManager languageManager;
    String language_id;
    public int pos = 0;
    ViewCard viewCard;
    public static ListView lv_cards;
    public static TextView tv_cards;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money_to_wallet);
        apiManager = new ApiManager(this , this , this );
        ed_enter_money = (EditText)findViewById(R.id.ed_enter_money);
        amount_first = (TextView) findViewById(R.id.am_first_txt);
        amount_second = (TextView) findViewById(R.id.am_second_txt);
        amount_third = (TextView) findViewById(R.id.am_third_txt);
        Add_Money = (TextView)findViewById(R.id.txt_add_money);
        ll_credit_card = (LinearLayout)findViewById(R.id.ll_credit_card);
        edt_card_number = (EditText) findViewById(R.id.edt_card_number);
        edt_expiry = (EditText) findViewById(R.id.edt_expiry);
        edt_cvv = (EditText) findViewById(R.id.edt_cvv);
        done = (TextView) findViewById(R.id.done_money);
        languageManager = new LanguageManager(this);
        sessionManager = new SessionManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);
        lv_cards = (ListView) findViewById(R.id.lv_cards);
        tv_cards = (TextView) findViewById(R.id.tv_card);
        ll_saved_cards = (LinearLayout)findViewById(R.id.ll_saved_cards);

        apiManager.execution_method_get(""+ Config.ApiKeys.key_view_cards, ""+ Apis.viewCard+"user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&language_id=1", true,ApiManager.ACTION_SHOW_TOP_BAR);

        findViewById(R.id.ll_back_wallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        amount_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_enter_money.setText(sessionManager.getCurrencyCode()+"100");
                amount_first.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                amount_first.setTextColor(getResources().getColor(R.color.pure_white));
                amount_second.setBackground(getResources().getDrawable(R.drawable.primary_color_layout_border));
                amount_third.setBackground(getResources().getDrawable(R.drawable.primary_color_layout_border));
                amount_third.setTextColor(getResources().getColor(R.color.colorPrimary));
                amount_second.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        amount_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_enter_money.setText("200");
                amount_second.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                amount_second.setTextColor(getResources().getColor(R.color.pure_white));
                amount_first.setBackground(getResources().getDrawable(R.drawable.primary_color_layout_border));
                amount_third.setBackground(getResources().getDrawable(R.drawable.primary_color_layout_border));
                amount_first.setTextColor(getResources().getColor(R.color.colorPrimary));
                amount_third.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        amount_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_enter_money.setText("300");
                amount_third.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                amount_third.setTextColor(getResources().getColor(R.color.pure_white));
                amount_first.setBackground(getResources().getDrawable(R.drawable.primary_color_layout_border));
                amount_second.setBackground(getResources().getDrawable(R.drawable.primary_color_layout_border));
                amount_first.setTextColor(getResources().getColor(R.color.colorPrimary));
                amount_second.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        Add_Money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(AddMoneyToWalletActivity.this, CreditCardPaymentActivity.class).putExtra("From", "PaymentFailedActivity"), 103);
//                dialogForPayment();

            }
        });

        edt_expiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pos = edt_expiry.getText().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (edt_expiry.getText().length() == 2 && pos != 3) {
                    edt_expiry.setText(edt_expiry.getText().toString() + "/");
                    edt_expiry.setSelection(3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_card_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = edt_card_number.getText().toString().split("-");
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    edt_card_number.setOnKeyListener(new View.OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        if (((edt_card_number.getText().length() + 1) % 5) == 0) {

                            if (edt_card_number.getText().toString().split("-").length <= 3) {
                                edt_card_number.setText(edt_card_number.getText() + "-");
                                edt_card_number.setSelection(edt_card_number.getText().length());
                            }
                        }
                        a = edt_card_number.getText().toString();
                    } else {
                        a = edt_card_number.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    edt_card_number.setText(a);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String card_number = edt_card_number.getText().toString();
                String expiry = edt_expiry.getText().toString();
                String cvv = edt_cvv.getText().toString();

                if(card_number.equals("")||expiry.equals("")||cvv.equals("")){
                    Toast.makeText(AddMoneyToWalletActivity.this, R.string.missing_details, Toast.LENGTH_SHORT).show();
                }else {
                    String[] parts = expiry.split("/");
                    String month = parts[0];
                    String year = parts[1];

                    Card card = new Card(card_number, Integer.parseInt(month), Integer.parseInt(year), cvv);


                    try {
                        Stripe stripe = new Stripe(AddMoneyToWalletActivity.this.getString(R.string.stripe_key));
                        stripe.createToken(card,
                                new TokenCallback() {
                                    public void onSuccess(Token token) {

                                        String stripe_token = token.getId() + "";

                                        Log.e("Id  ", token.getId() + "");
                                        apiManager.execution_method_get(""+Config.ApiKeys.Key_Save_cards , ""+Apis.saveCard+"user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&user_email="+sessionManager.getUserDetails().get(SessionManager.USER_EMAIL)+"&stripe_token="+stripe_token+"&language_id="+language_id, true,ApiManager.ACTION_SHOW_TOP_BAR);
                                    }

                                    public void onError(Exception error) {
                                        Toast.makeText(AddMoneyToWalletActivity.this, getString(R.string.incorrect_card_number), Toast.LENGTH_SHORT).show();
                                        Log.e("err", error.toString());
                                    }
                                });
                    } catch (Exception e) {
                        Toast.makeText(AddMoneyToWalletActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("exception ", e.toString());
                    }
                }
            }
        });


        lv_cards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String card_id = viewCard.getDetails().get(position).getCard_id();
                if (ed_enter_money.getText().toString().equals("")){
                    Toast.makeText(AddMoneyToWalletActivity.this, R.string.please_select_or_enter_money, Toast.LENGTH_SHORT).show();
                }else {
                    apiManager.execution_method_get(""+Config.ApiKeys.Key_Add_Money_to_Wallet , ""+Apis.AddMoneyTOWallet+"user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&amount="+ed_enter_money.getText().toString()+"&card_id="+card_id, true,ApiManager.ACTION_SHOW_TOP_BAR);
                }
            }
        });
    }

    public void dialogForPayment() {
        final Dialog dialog = new Dialog(AddMoneyToWalletActivity.this, android.R.style.Theme_Holo_Light_DarkActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialogue_for_wallet_payment);

        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.rg);
        LinearLayout ll_done_payment = (LinearLayout) dialog.findViewById(R.id.ll_done_payment);

        ll_done_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton r = (RadioButton) dialog.findViewById(radioGroup.getCheckedRadioButtonId());
                String option = r.getText().toString();

                if (option.equalsIgnoreCase(getString(R.string.ADD_MONEY_TO_WALLET_ACTIVITY__use_saved_cards))) {

                    dialog.dismiss();
                    ll_saved_cards.setVisibility(View.VISIBLE);
                    ll_credit_card.setVisibility(View.GONE);

                } else if (option.equalsIgnoreCase(getString(R.string.ADD_MONEY_TO_WALLET_ACTIVITY__with_new_card))) {

                    dialog.dismiss();
                    ll_credit_card.setVisibility(View.VISIBLE);
                    ll_saved_cards.setVisibility(View.GONE);

                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }




    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+Config.ApiKeys.Key_Save_cards)) {
                SaveCardResponse saveCardResponse;
                saveCardResponse = gson.fromJson(""+script, SaveCardResponse.class);
                if (saveCardResponse.getResult() == 1) {
                    Toast.makeText(this, "" + saveCardResponse.getMsg(), Toast.LENGTH_LONG).show();
                    ll_credit_card.setVisibility(View.GONE);
                    ll_saved_cards.setVisibility(View.VISIBLE);
                    apiManager.execution_method_get(""+ Config.ApiKeys.key_view_cards, ""+ Apis.viewCard+"user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&language_id=1", true,ApiManager.ACTION_SHOW_TOP_BAR);
                    edt_expiry.setText("");
                    edt_card_number.setText("");
                    edt_cvv.setText("");
//                finish();
                } else {
                    Toast.makeText(this, "" + saveCardResponse.getMsg(), Toast.LENGTH_LONG).show();
                }
            }
            else if (APINAME.equals(""+Config.ApiKeys.key_view_cards)) {

                viewCard = gson.fromJson(""+script, ViewCard.class);
                if (viewCard.getResult() == 1) {

                    lv_cards.setAdapter(new CardsAdapter(this, viewCard , "1" , this));
                    lv_cards.setVisibility(View.VISIBLE);
                    tv_cards.setVisibility(View.GONE);
                } else {
                    lv_cards.setVisibility(View.GONE);
                    tv_cards.setVisibility(View.VISIBLE);
                }
            }
            else if (APINAME.equals(""+Config.ApiKeys.Key_Add_Money_to_Wallet)){

                AddWalletMoneyResponse walletMoneyResponse = new AddWalletMoneyResponse();
                walletMoneyResponse = gson.fromJson(""+script , AddWalletMoneyResponse.class);
                if (walletMoneyResponse.getResult()==1){

                    Toast.makeText(this, ""+walletMoneyResponse.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(this, ""+walletMoneyResponse.getMsg(), Toast.LENGTH_SHORT).show();
                }


            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == 103) {
                    String card_id=data.getStringExtra("CREDIT_ID");

                    apiManager.execution_method_get(""+Config.ApiKeys.Key_Add_Money_to_Wallet , ""+Apis.AddMoneyTOWallet+"user_id="+sessionManager.getUserDetails().get(SessionManager.USER_ID)+"&amount="+ed_enter_money.getText().toString().replace(sessionManager.getCurrencyCode(),"")+"&card_id="+card_id, true,ApiManager.ACTION_SHOW_TOP_BAR);


                }
            } catch (Exception e) {
                Log.e("res ", e.toString());
            }

        }
    }
}
