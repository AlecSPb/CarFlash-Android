package com.carflash.user.drawroutemaps;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.carflash.user.R;
import com.carflash.user.manager.SessionManager;
import com.carflash.user.others.AerialDistance;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ocittwo on 11/14/16.
 *
 * @Author Ahmad Rosid
 * @Email ocittwo@gmail.com
 * @Github https://github.com/ar-android
 * @Web http://ahmadrosid.com
 */
public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private PolylineOptions lineOptions;
    private GoogleMap mMap;
    private int routeColor;
    private  View mprogress_view ;
    private  boolean mshow_progress ;
    Context mContext ;
    Activity mActivity ;
    SessionManager mSessionManager ;

    public RouteDrawerTask(GoogleMap mMap , Context mContext  , Activity activity , SessionManager sessionManager , View view  , boolean showprogress) {
        this.mMap = mMap;
        this.mActivity = activity ;
        this.mContext = mContext ;
        this.mSessionManager = sessionManager ;
        mprogress_view = view ;
        mshow_progress = showprogress ;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("RouteDrawerTask", jsonData[0]);
            DataRouteParser parser = new DataRouteParser();
            Log.d("RouteDrawerTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("RouteDrawerTask", "Executing routes");
            Log.d("RouteDrawerTask", routes.toString());

        } catch (Exception e) {
            Log.d("RouteDrawerTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        if (result != null)
            try {drawPolyLine(result); }catch (Exception e){ }
    }

    private void drawPolyLine(List<List<HashMap<String, String>>> result) throws Exception{
        ArrayList<LatLng> points = null;
        lineOptions = null;

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(6);
            routeColor = ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorRouteLine);
            if (routeColor == 0)
                lineOptions.color(Color.parseColor("#333333"));
            else
                lineOptions.color(routeColor);
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null && mMap != null) {
            mMap.addPolyline(lineOptions);
        } else {
            Log.d("onPostExecute", "without Polylines draw");
        }



        Double aerialDistance  = 0.0 ;
        for(int i =0 ; i < points.size() - 1 ; i ++){
            aerialDistance = aerialDistance + AerialDistance.aerialDistanceFunctionInMeters(points.get(i).latitude , points.get(i+1).longitude , points.get(i+1).latitude , points.get(i).longitude);
        }
        aerialDistance = aerialDistance /30 ;
        if(aerialDistance <= 50){
            mSessionManager.setTailFactor(""+50);
        }else{
            mSessionManager.setTailFactor(""+aerialDistance);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < points.size() ; i++){
            builder.include(points.get(i));
        }
        if(mprogress_view!=null && mshow_progress){
            mprogress_view.setVisibility(View.GONE);
        }
        LatLngBounds bounds = builder.build();
        Point displaySize = new Point();
        mActivity.getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 670, 100));
    }

}
