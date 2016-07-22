package com.wamp42.pikapika.data;


import android.location.Location;

import com.google.gson.Gson;
import com.wamp42.pikapika.models.Coords;
import com.wamp42.pikapika.models.LoginData;
import com.wamp42.pikapika.models.PokemonLocation;
import com.wamp42.pikapika.network.RestClient;

import java.util.HashMap;

import okhttp3.Callback;

/**
 * Created by flavioreyes on 7/19/16.
 */
public class DataManager {

    private static DataManager dataManagerInstance;
    private RestClient restClient = new RestClient();

    public static DataManager getDataManager(){
        if(dataManagerInstance == null)
            dataManagerInstance = new DataManager();
        return dataManagerInstance;
    }

    public void login(String user, String pass,Location location, String loginType, Callback callback){
        Coords coords;
        if(location != null){
            coords = new Coords(location.getLatitude(),location.getLongitude());
        } else {
            //This shouldn't happened
            coords = new Coords(0.0,0.0);
        }
        PokemonLocation pokemonLocation = new PokemonLocation(coords);
        LoginData loginData = new LoginData(user,pass,loginType,pokemonLocation);
        //convert object to json
        String jsonInString = new Gson().toJson(loginData);
        //do the request
        restClient.postJson(jsonInString,"trainers/login",callback);
    }

    public void heartbeat(String token,Callback callback){
        HashMap<String, String> params = new HashMap<>();
        params.put("access_token", token);
        restClient.get("pokemons/heartbeat", params, callback);
    }
}