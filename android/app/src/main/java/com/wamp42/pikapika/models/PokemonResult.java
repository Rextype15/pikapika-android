package com.wamp42.pikapika.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wamp42.pikapika.R;
import com.wamp42.pikapika.data.PokemonHelper;

import java.util.Locale;

/**
 * Created by flavioreyes on 7/21/16.
 */
public class PokemonResult {

    final public static int POKEMON_SIZE = 60;

    //new parameters
    private String id;
    private String number;
    private String name;
    private PokemonPosition position;
    private int timeleft = 0;

    private long initTime=0;

    public PokemonResult(){}

    public void drawMark(GoogleMap map, Context context){
        if(position == null)
            return;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(position.getLat(), position.getLng()));

        markerOptions.title(name);

        int [] secondsArray = splitToComponentTimes(timeleft);
        String timeLeftStr = context.getString(R.string.time_left);
        String timeStr = String.format(Locale.ENGLISH,"%dm %ds",secondsArray[0],secondsArray[1]);
        markerOptions.snippet(timeLeftStr+": " + timeStr);

        //set the marker-icon
        String idStr = getStrId();
        int iconId = context.getResources().getIdentifier("pokemon_"+idStr+"", "drawable", context.getPackageName());
        if(iconId > 0) {
            Bitmap bitmapIcon = BitmapFactory.decodeResource(context.getResources(),iconId);
            float wt_px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, POKEMON_SIZE, context.getResources().getDisplayMetrics());
            int iconSize = (int)wt_px;
            Bitmap resizedIcon = Bitmap.createScaledBitmap(bitmapIcon, iconSize, iconSize, false);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizedIcon));
        }
        Marker marker = map.addMarker(markerOptions);
        //maps the marker and the pokemon id
        PokemonHelper.markersMap.put(marker.getId(),this);
        initTime = System.currentTimeMillis();
    }

    public String getStrId() {
        String pokemonNumber = number;
        String strId = String.valueOf(pokemonNumber);
        int length = strId.length();
        if(length == 1)
            strId = "00"+strId;
        else if (length == 2)
            strId = "0"+strId;
        return strId;
    }

    public int getTimeleft() {
        return timeleft;
    }

    public long getInitTime() {
        return initTime;
    }

    public String getName() {
        return name;
    }

    public String getTimeleftParsed(Context context,long currentMilli) {
        long realTimeLeft = timeleft-currentMilli;
        if(realTimeLeft > 0) {
            int[] secondsArray = splitToComponentTimes(realTimeLeft);
            String timeLeftStr = context.getString(R.string.time_left);
            String timeStr = String.format(Locale.ENGLISH, "%dm %ds", secondsArray[0], secondsArray[1]);
            return timeLeftStr + ": " + timeStr;
        }
        return context.getString(R.string.it_has_gone);
    }

    public static int[] splitToComponentTimes(long longVal)
    {
        int seconds = (int)longVal/1000;
        int minutes = seconds / 60;
        int remainder = seconds - minutes * 60;
        int secs = remainder;

        int[] ints = { minutes , secs};
        return ints;
    }
}
