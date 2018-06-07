package com.carflash.user.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carflash.user.AddCardActivity;
import com.carflash.user.R;
import com.carflash.user.manager.LanguageManager;
import com.carflash.user.models.DeletecardResponse;
import com.carflash.user.models.ViewCard;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.manager.ApiManager;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CardsAdapter extends BaseAdapter implements ApiManager.APIFETCHER {
    Context context;
    ViewCard viewCard;

    SessionManager sessionManager;
    LanguageManager languageManager;
    String user_id, language_id;
    String WalletOrSave ;

    ApiManager apiManager ;

    public CardsAdapter(Context context, ViewCard viewCard , String from , Activity activity) {
        this.context = context;
        this.viewCard = viewCard;
        sessionManager = new SessionManager(context);
        languageManager = new LanguageManager(context);
        user_id = sessionManager.getUserDetails().get(SessionManager.USER_ID);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);
        this.WalletOrSave = from ;
        apiManager  = new ApiManager(this , activity , context );

    }

    @Override
    public int getCount() {
        return viewCard.getDetails().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemlayoutforcards, parent, false);
            myHolder = new MyHolder(convertView);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        if (WalletOrSave.equals("1")){
            myHolder.ll_delete_card.setVisibility(View.GONE);
        }else {
            myHolder.ll_delete_card.setVisibility(View.VISIBLE);
        }


        myHolder.tv_card_number.setText("XXXXXXXXXXXX-" + viewCard.getDetails().get(position).getCard_number());
        myHolder.tv_card_type.setText(viewCard.getDetails().get(position).getCard_type());
        myHolder.ll_delete_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onPopupButtonClick(myHolder.iv_menu_adapter, position);

//                initiatePopupWindow(myHolder.iv_menu_adapter, position);
            }
        });
        return convertView;
    }





    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+Config.ApiKeys.Key_Delete_Cards)) {
                DeletecardResponse deletecardResponse;
                deletecardResponse = gson.fromJson(""+script, DeletecardResponse.class);
                if (deletecardResponse.getResult().toString().equals("1")) {
                    apiManager.execution_method_get(""+Config.ApiKeys.key_view_cards , ""+ Apis.viewCard+"?user_id="+user_id+"&language_id=1", true,ApiManager.ACTION_DO_NOTHING);
                } else {

                }
            }

            if (APINAME.equals(""+ Config.ApiKeys.key_view_cards)) {

                viewCard = gson.fromJson(""+script, ViewCard.class);
                if (viewCard.getResult() == 1) {
                    this.notifyDataSetChanged();
                    AddCardActivity.lv_cards.setVisibility(View.VISIBLE);
                    AddCardActivity.tv_cards.setVisibility(View.GONE);
                } else {
                    AddCardActivity.lv_cards.setVisibility(View.GONE);
                    AddCardActivity.tv_cards.setVisibility(View.VISIBLE);
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }


    static class MyHolder {
        @Bind(R.id.tv_card_number)
        TextView tv_card_number;

        @Bind(R.id.tv_card_type)
        TextView tv_card_type;

        @Bind(R.id.iv_menu_adapter)
        ImageView iv_menu_adapter;

        @Bind(R.id.ll_delete_card)
        LinearLayout ll_delete_card;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void onPopupButtonClick(View button, final int position) {
        PopupMenu popup = new PopupMenu(context, button);
        popup.getMenuInflater().inflate(R.menu.menu_adapter, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                String t = (String) item.getTitle();
                if (t.equals("Delete")) {
                    apiManager.execution_method_get(""+Config.ApiKeys.Key_Delete_Cards, ""+Apis.deleteCard+"card_id="+viewCard.getDetails().get(position).getCard_id()+"&language_id="+language_id, true,ApiManager.ACTION_DO_NOTHING);
                }
                return true;
            }
        });
        popup.show();
    }
}
