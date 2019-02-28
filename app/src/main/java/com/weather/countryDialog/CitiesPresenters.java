package com.weather.countryDialog;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weather.model.City;
import com.weather.model.Country;
import com.weather.utils.RequestHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

public class CitiesPresenters {

    private static final String user = "babo";
    private static final String geoNames = "geonames";
    private static final String TAG = "CitiesPresenters";

    private static final String URL_COUNTY = "http://api.geonames.org/countryInfoJSON?username=" + user;

    private static final String city_geonameId = "city_geonameId";
    private static final String URL_CITY = "http://api.geonames.org/childrenJSON?geonameId=" + city_geonameId + "&username=" + user;

    private HashMap<Country, List<City>> citiesMap = new HashMap<>();


    public Observable<List<City>> getCities(final Country country) {

        if (citiesMap.containsKey(country) && citiesMap.get(country) != null)
            return Observable.just(citiesMap.get(country));

        RequestHelper requestHelper = new RequestHelper();

        return requestHelper.request(URL_CITY.replace(city_geonameId, country.geonameId))
                .map(s -> {
                    JSONObject jsonObject = new JSONObject(s);
                    String json = jsonObject.getString(geoNames);
                    Type listType = new TypeToken<ArrayList<City>>() {
                    }.getType();
                    List<City> list = new Gson().fromJson(json, listType);
                    citiesMap.put(country, list);
                    return list;
                }).toObservable();
    }

}
