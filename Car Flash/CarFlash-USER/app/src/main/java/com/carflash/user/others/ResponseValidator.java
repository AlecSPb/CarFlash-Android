package com.carflash.user.others;

import com.carflash.user.accounts.Loginvaluechecker;
import com.carflash.user.accounts.RegistrationModel;
import com.carflash.user.models.About;
import com.carflash.user.models.ActiveRidesResponse;
import com.carflash.user.models.ApplyCouponResponse;
import com.carflash.user.models.CallSupportModel;
import com.carflash.user.models.CancelReasonCustomer;
import com.carflash.user.models.CarTypeResponseModel;
import com.carflash.user.models.DistanceMatrixResponseModel;
import com.carflash.user.models.DoneRideInfo;
import com.carflash.user.models.EditProfileResponse;
import com.carflash.user.models.NewDoneRidemodel;
import com.carflash.user.models.NewRideHistoryModel;
import com.carflash.user.models.NewRideInfoModel;
import com.carflash.user.models.NewRideSync;
import com.carflash.user.models.NewRideSyncModel;
import com.carflash.user.models.NewSosModel;
import com.carflash.user.models.NotificationResponse;
import com.carflash.user.models.PaymentSaved;
import com.carflash.user.models.QueryResponseModel;
import com.carflash.user.models.RateCard;
import com.carflash.user.models.RatingResponse;
import com.carflash.user.models.RideEstimate;
import com.carflash.user.models.RideNow;
import com.carflash.user.models.TermsAndConditionResponse;
import com.carflash.user.models.ViewCarType;
import com.carflash.user.models.ViewCity;
import com.carflash.user.models.ViewPaymentOption;
import com.carflash.user.models.ViewRideInfo;
import com.carflash.user.samwork.Config;

/**
 * Created by lenovo-pc on 9/5/2017.
 */

public class ResponseValidator {
    public static boolean isResponseParsable(Object script , String key_Model){
        boolean returning_value = false ;
        try {
            if(key_Model.equals(""+ Config.ApiKeys.KEY_UpdateDevice_ID)){

            }if(key_Model.equals(""+ Config.ApiKeys.KEY_CallSupport)){
                CallSupportModel callSupportModel = SingletonGson.getInstance().fromJson(""+script , CallSupportModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_PaymentOption)){
                ViewPaymentOption rs = SingletonGson.getInstance().fromJson(""+script , ViewPaymentOption.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_View_Cars)){
                CarTypeResponseModel rs = SingletonGson.getInstance().fromJson(""+script , CarTypeResponseModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_FLAG)){

            }if(key_Model.equals(""+ Config.ApiKeys.Key_Rice_Now)){
                RideNow rs = SingletonGson.getInstance().fromJson(""+script , RideNow.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Ride_Later)){
                RideNow rs = SingletonGson.getInstance().fromJson(""+script , RideNow.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Estimate)){
                RideEstimate rs = SingletonGson.getInstance().fromJson(""+script , RideEstimate.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Cancel_by_user)){

            }if(key_Model.equals(""+ Config.ApiKeys.Key_ViewRideInfo)){
                ViewRideInfo rs = SingletonGson.getInstance().fromJson(""+script , ViewRideInfo.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_cancelRideReason)){
                CancelReasonCustomer rs = SingletonGson.getInstance().fromJson(""+script , CancelReasonCustomer.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Cancel_Ride)){

            }if(key_Model.equals(""+ Config.ApiKeys.Key_Google_Distance_matrix)){
                DistanceMatrixResponseModel rs = SingletonGson.getInstance().fromJson(""+script , DistanceMatrixResponseModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Rating_Api)){
                RatingResponse rs = SingletonGson.getInstance().fromJson(""+script , RatingResponse.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_payment_api)){
                PaymentSaved rs = SingletonGson.getInstance().fromJson(""+script , PaymentSaved.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_view_Done_Ride)){
                DoneRideInfo rs = SingletonGson.getInstance().fromJson(""+script , DoneRideInfo.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Virew_Cities)){
                ViewCity rs = SingletonGson.getInstance().fromJson(""+script , ViewCity.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Virew_Rate_Card_Cities)){
                RateCard rs = SingletonGson.getInstance().fromJson(""+script , RateCard.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_View_car_by_Cities)){
                ViewCarType rs = SingletonGson.getInstance().fromJson(""+script , ViewCarType.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Customer_Sync)){
                NewRideSync rs = SingletonGson.getInstance().fromJson(""+script , NewRideSync.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Key_Customer_Sync_End)){
                NewRideSync rs = SingletonGson.getInstance().fromJson(""+script , NewRideSync.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KeyApplyCoupon)){
                ApplyCouponResponse rs = SingletonGson.getInstance().fromJson(""+script , ApplyCouponResponse.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KeyAboutUs)){
                About rs = SingletonGson.getInstance().fromJson(""+script , About.class);
            }if(key_Model.equals(""+ Config.ApiKeys.Ket_terms_and_condition)){
                TermsAndConditionResponse rs = SingletonGson.getInstance().fromJson(""+script , TermsAndConditionResponse.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_EDIT_PROFILE)){
                EditProfileResponse rs = SingletonGson.getInstance().fromJson(""+script , EditProfileResponse.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_ACTIVES_RIDES)){
                ActiveRidesResponse rs = SingletonGson.getInstance().fromJson(""+script , ActiveRidesResponse.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_SOS)){
                NewSosModel rs = SingletonGson.getInstance().fromJson(""+script , NewSosModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_RIDE_SYNC)){
                NewRideSyncModel rs = SingletonGson.getInstance().fromJson(""+script , NewRideSyncModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_RIDE_INFO)){
                NewRideInfoModel rs = SingletonGson.getInstance().fromJson(""+script , NewRideInfoModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_DONE_RIDE_INFO)){
                NewDoneRidemodel rs = SingletonGson.getInstance().fromJson(""+script , NewDoneRidemodel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_RATING)){

            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_RIDE_HISTORY)){
                NewRideHistoryModel rs = SingletonGson.getInstance().fromJson(""+script , NewRideHistoryModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_RIDE_DETAILS)){
//                NewRideDetailsModel rs = SingletonGson.getInstance().fromJson(""+script , NewRideDetailsModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_RENTAL_CANCEL_RIDE)){
                NewRideHistoryModel rs = SingletonGson.getInstance().fromJson(""+script , NewRideHistoryModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_RENTAL_MAKE_PAYMENT)){

            }if(key_Model.equals(""+ Config.ApiKeys.KEY_RENTAL_RATING)){

            }if(key_Model.equals(""+ Config.ApiKeys.KEY_CUSTOMER_SUPPORT)){
                QueryResponseModel rs = SingletonGson.getInstance().fromJson(""+script , QueryResponseModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REST_NOTIFICATIONS)){
                NotificationResponse rs = SingletonGson.getInstance().fromJson(""+script , NotificationResponse.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_REGISTER)){
                RegistrationModel rs = SingletonGson.getInstance().fromJson(""+script , RegistrationModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_PHONE_LOGIN)){
                Loginvaluechecker rs = SingletonGson.getInstance().fromJson(""+script , Loginvaluechecker.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_CHANGE_PASSWORD)){

            }if(key_Model.equals(""+ Config.ApiKeys.KEY_PHONE_INFO)){
                RegistrationModel rs = SingletonGson.getInstance().fromJson(""+script , RegistrationModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_FACEBOOK_SIGNUP)){
                RegistrationModel rs = SingletonGson.getInstance().fromJson(""+script , RegistrationModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_FACEBOOK_LOGIN)){
                RegistrationModel rs = SingletonGson.getInstance().fromJson(""+script , RegistrationModel.class);
            }if(key_Model.equals(""+ Config.ApiKeys.KEY_GOOGLE_LOGIN)){
                RegistrationModel rs = SingletonGson.getInstance().fromJson(""+script , RegistrationModel.class);
            }
        }catch (Exception e){

        }


        return  returning_value ;
    }
}
