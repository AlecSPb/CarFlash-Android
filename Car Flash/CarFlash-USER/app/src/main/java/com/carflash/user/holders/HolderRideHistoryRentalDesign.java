package com.carflash.user.holders;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.carflash.user.R;
import com.carflash.user.RentalRidesSelectedActivity;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.models.NewRideHistoryModel;
import com.carflash.user.others.MapUtils;
import com.carflash.user.samwork.Config;
import com.carflash.user.urls.Apis;
import com.bumptech.glide.Glide;
import com.sam.placer.PlaceHolderView;
import com.sam.placer.annotations.Click;
import com.sam.placer.annotations.Layout;
import com.sam.placer.annotations.Resolve;
import com.sam.placer.annotations.View;


@Layout(R.layout.holder_ride_history_rental_layout)
public class HolderRideHistoryRentalDesign {

    @View(R.id.image_start) private ImageView image;
    @View(R.id.start_txt) private TextView start_txt;
    @View(R.id.end_txt) private TextView end_txt;
    @View(R.id.time_txt) private TextView time_txt;
    @View(R.id.car_name_txt) private TextView car_name_txt;
    @View(R.id.amount_txt) private TextView amount_txt;
    @View(R.id.ride_status_txt) private TextView ride_status_txt;
    @View(R.id.car_image) private ImageView car_image;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;
    NewRideHistoryModel.DetailsBean.RentalRideBean mMsg ;
    SessionManager sessionManager ;

    public HolderRideHistoryRentalDesign(Context context, PlaceHolderView placeHolderView, NewRideHistoryModel.DetailsBean.RentalRideBean msg , SessionManager sessionManager ) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        this.sessionManager = sessionManager ;
        mMsg = msg ;
    }

    @Resolve
    private void onResolved() {

        time_txt.setText(""+ mMsg.getRegister_date()+" "+mMsg.getBooking_time());
        car_name_txt.setText(""+mMsg.getCar_type_name());

        ride_status_txt.setText(""+ Config.getStatustext(mMsg.getBooking_status(), mContext));

        start_txt.setText(""+mMsg.getPickup_location());
        end_txt.setText(""+mMsg.getEnd_location());
        Glide.with(mContext).load(""+ Apis.imageDomain+mMsg.getCar_type_image()).into(car_image);
        Glide.with(mContext).load(MapUtils.getStaticMapImageUrl(""+mMsg.getPickup_lat() , ""+mMsg.getPickup_long() , mContext)).into(image);

        if(!mMsg.getFinal_bill_amount().equals("")){
            amount_txt.setText(sessionManager.getCurrencyCode() + mMsg.getFinal_bill_amount());
        }
    }




    @Click(R.id.root)
    private void OnClickRoot(){
        mContext.startActivity(new Intent(mContext, RentalRidesSelectedActivity.class)
                .putExtra("ride_id", mMsg.getRental_booking_id())
                .putExtra("ride_status", mMsg.getBooking_status())
                .putExtra("date_time", mMsg.getBooking_date()+" "+mMsg.getBooking_time())
//                .putExtra("ride_type", mMsg.getRide_time())
                .putExtra("image" , "")
                .putExtra("pick_lat" , ""+mMsg.getPickup_lat())
                .putExtra("pick_long" , ""+mMsg.getPickup_long())
                .putExtra("drop_lat" , ""+mMsg.getEnd_lat())
                .putExtra("drop_long" , ""+mMsg.getEnd_long())
                .putExtra("ride_mode" , "2"));   // main url

    }
}
