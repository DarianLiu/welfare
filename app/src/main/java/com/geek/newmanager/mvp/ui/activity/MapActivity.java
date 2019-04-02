package com.geek.newmanager.mvp.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.cmmap.api.maps.Map;
import com.cmmap.api.maps.MapView;
import com.cmmap.api.maps.model.LatLng;
import com.cmmap.api.maps.model.Marker;
import com.cmmap.api.maps.model.MarkerOptions;
import com.geek.newmanager.R;
import com.geek.newmanager.app.EventBusTags;
import com.geek.newmanager.mvp.model.event.LocationEvent;

import org.simple.eventbus.EventBus;

import butterknife.OnClick;

/**
 * 地图
 * Created by LiuLi on 2018/9/8.
 */

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private Button btnSure;

    private Map mMap;

    private double lng = 40.000565, lat = 116.486073;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapView = findViewById(R.id.mMap);
        mMapView.onCreate(savedInstanceState);
        btnSure = findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(v -> {
            LocationEvent event = new LocationEvent();
            event.setLat(lat);
            event.setLng(lng);
            EventBus.getDefault().post(event, "location");
            finish();
        });

        mMap = mMapView.getMap();
        mMap.setMapType(Map.MAP_TYPE_NORMAL);
//        mMap.showMapText(true);//是否显示文字
//        mMap.showBuildings(true);//是否显示建筑物
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(40.000565, 116.486073));
        markerOptions.draggable(true);
        mMap.addMarker(markerOptions);

        mMap.setOnMarkerDragListener(new Map.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                lng = marker.getPosition().longitude;
                lat = marker.getPosition().latitude;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mMap.clear();
        mMap = null;
    }

}
