package com.carflash.user;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by lenovo-pc on 6/14/2017.
 */

public abstract class BaseActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getConnectivityStatus(hasConnectivity());

    }


    protected abstract void getConnectivityStatus(boolean val);





        public  boolean hasConnectivity()  {
//            boolean connected = false;
//            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//                //we are connected to a network
//                connected = true;
//            }
//            else
//                connected = false;

            return true;
        }




}
