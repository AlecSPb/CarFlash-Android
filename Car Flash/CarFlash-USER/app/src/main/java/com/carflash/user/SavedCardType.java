package com.carflash.user;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.placer.annotations.Click;
import com.sam.placer.annotations.Layout;
import com.sam.placer.annotations.Position;
import com.sam.placer.annotations.Resolve;
import com.sam.placer.annotations.View;

/**
 * Created by Lenovo on 11/9/2017.
 */
@Layout(R.layout.saved_cards)
public class SavedCardType {

    @Position
    private int mPosition ;

    @View(R.id.last4) private TextView last4_txt;
    @View(R.id.delete_card) private LinearLayout ll_delete_card;
    @View(R.id.month) private TextView month_txt;
    @View(R.id.year) private TextView year_txt;
    @View(R.id.ll_card) private LinearLayout ll_card;

    Context mContext ;
    Saved_Card_Model savedCardModel ;
    Activity mActivity;
    onClickDelete listener;

    public SavedCardType(Context context, Saved_Card_Model savedCardModel, Activity activity, onClickDelete listener){
        mContext = context ;
       this.savedCardModel = savedCardModel;
        mActivity = activity ;
        this.listener = listener;
    }

    @Resolve
    private void onResolved() {
        last4_txt.setText(""+savedCardModel.getDetails().get(mPosition).getLast4());
        month_txt.setText(""+savedCardModel.getDetails().get(mPosition).getExp_month());
        year_txt.setText(""+savedCardModel.getDetails().get(mPosition).getExp_year());
    }

    @Click(R.id.delete_card)
        private void OnClick(){
        listener.clickDelete(mPosition);
       // mPlaceHolderView.removeView(""+savedCardModel.getDetails().get(mPosition));
        }

        @Click(R.id.ll_card)
        private void onLayoutClick(){
            listener.layoutClick(mPosition);

        }

        public interface onClickDelete{
            void clickDelete(int pos);
            void layoutClick(int pos);
        }
}
