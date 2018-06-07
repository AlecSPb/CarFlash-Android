package com.carflash.user.others;

import android.util.Log;

import com.carflash.user.models.CarTypeResponseModel;
import com.carflash.user.models.firebasemodels.DriverLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lenovo-pc on 5/9/2017.
 */

public class DriverPool {

    private static final String TAG = "DriverPool";


    public static List<DriverLocation> Driver_pool = new ArrayList<>();


    public static void addDriverInLocalPool (DriverLocation driverLocation){
        try {
            if(Driver_pool.contains(driverLocation)){ // replacinf the exsisting driver from pool
                int position_of_exsisting_driver  = Driver_pool.indexOf(driverLocation);
                Driver_pool.set(position_of_exsisting_driver , driverLocation);

            }else { // adding new driver to pool
                Driver_pool.add(driverLocation);
            }
        }catch (Exception e){
            Log.e(TAG , "Exception occur while adding driver in pool "+e.getMessage());
        }
        Driver_pool.add(driverLocation);
    }

    public static void removeDriverFromPool(DriverLocation driverLocation){
        try {
            int index_of_driver = Driver_pool.indexOf(driverLocation);
            Driver_pool.remove(index_of_driver);
        }catch (Exception e){
            Log.d(TAG , ""+e.getMessage());
        }

    }

    public static List<DriverLocation> getnearestFromAllCategory(LatLng location, CarTypeResponseModel car_type_response ){ // gives nearest driver in all categories

        DriverLocation driver = null;
        List<DriverLocation>  data = new ArrayList<>();

        try{
            for(int i = 0 ; i < car_type_response.getDetails().size() ; i++){
                double  distance = 0.0 ;

                for(int j = 0 ; j < Driver_pool.size() ; j++){
                    if(car_type_response.getDetails().get(i).getCar_type_id().equals(""+Driver_pool.get(j).driver_car_type_id)){
                        if(distance == 0.0){
                            distance = AerialDistance.aerialDistanceFunctionInMeters(location.latitude,location.longitude,Double.parseDouble(Driver_pool.get(j).getDriver_current_latitude()), Double.parseDouble(Driver_pool.get(j).driver_current_longitude));
                            driver = Driver_pool.get(j);
                        }else if(distance > AerialDistance.aerialDistanceFunctionInMeters(location.latitude , location.longitude ,Double.parseDouble(Driver_pool.get(j).driver_current_latitude),Double.parseDouble(Driver_pool.get(j).driver_current_longitude))) {
                            distance = AerialDistance.aerialDistanceFunctionInMeters(location.latitude , location.longitude ,Double.parseDouble(Driver_pool.get(j).driver_current_latitude),Double.parseDouble(Driver_pool.get(j).driver_current_longitude));
                            driver = Driver_pool.get(j);
                        }
                    }
                }
                data.add(driver);
            }

        }catch (Exception e ){

            Log.e("***getting null id frodriver" , ""+e.getMessage());
        }

        return data;

    }

    public static Observable<List<DriverLocation>> getCategorisedNearestDriver (LatLng location ,  CarTypeResponseModel car_type_response){
        return Observable.just(getnearestFromAllCategory(location , car_type_response));
    }


    public static void showallneardriverforallcategories(LatLng location ,  CarTypeResponseModel car_type_response){

        getCategorisedNearestDriver(location , car_type_response)
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private static Observer<List<DriverLocation>> getObserver() {
        return new Observer<List<DriverLocation>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG , "Observer Subscribed");
            }

            @Override
            public void onNext(@NonNull List<DriverLocation> driverLocations) {
                Log.d(TAG , "size of list for nearest driver  = "+driverLocations.size());
                for(int i =0 ; i < driverLocations.size() ; i++){
                    try {
                        Log.d(TAG , "ddddddddd = "+driverLocations.get(i).driver_id);
                    }catch (Exception e){
                        Log.d(TAG , "ddddddddd error = "+e.getMessage());
                    }

                }


            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG , "Observer error = "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG , "Observer OnComplete");
            }
        };
    }





}
