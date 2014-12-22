package com.muggins.yuntu;


import java.util.ArrayList;
import java.util.List;

import com.amap.api.cloud.model.CloudItem;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
/**************************
 *
 * @author zhaoqin
 * 功能：云图信息点数据显示
 * 更新日期：2014-8-21
 *
 *************************/
public class PoiOverlay {
    private List<CloudItem> mPois;
    private AMap mAMap;
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();

    public PoiOverlay(AMap amap, List<CloudItem> pois) {
        mAMap = amap;
        mPois = pois;
    }

    public void addToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Marker marker = mAMap.addMarker(getMarkerOptions(i));
            marker.setObject(i);
            mPoiMarks.add(marker);
        }
    }

    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0) {
            if (mAMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(
                        new LatLng(mPois.get(index).getLatLonPoint()
                                .getLatitude(), mPois.get(index)
                                .getLatLonPoint().getLongitude()))
                .snippet("地址:" + getSnippet(index))
                .title("心情:" + getCustomfield(index))
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
    }

    protected BitmapDescriptor getBitmapDescriptor(int index) {
        return null;
    }

    protected String getCustomfield(int index){
        int num = mPois.get(index).getCustomfield().toString().length();
        return mPois.get(index).getCustomfield().toString().substring(6, num-1);
    }
    protected String getTitle(int index) {
        return mPois.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return mPois.get(index).getSnippet();
    }

    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    public CloudItem getPoiItem(int index) {
        if (index < 0 || index >= mPois.size()) {
            return null;
        }
        return mPois.get(index);
    }
}
