package com.weather;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weather.database.DBHelper;
import com.weather.model.City;
import com.weather.model.Country;
import com.weather.model.Weather;
import com.weather.utils.RequestHelper;
import com.weather.utils.RxHelper;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.functions.Function;

public class CountriesPresenter {

    private static final String user = "babo";
    private static final String geoNames = "geonames";
    private static final String TAG = "CountriesPresenter";

    private static final String URL_COUNTY = "http://api.geonames.org/countryInfoJSON?username=" + user;

    private static final String city_geonameId = "city_geonameId";
    private static final String URL_CITY = "http://api.geonames.org/childrenJSON?geonameId=" + city_geonameId + "&username=" + user;

    private HashMap<Country, List<City>> citiesMap = new HashMap<>();

    public Observable<List<Country>> getCountries() {
        return loadFromDB().flatMapObservable((Function<List<Country>, ObservableSource<List<Country>>>) countries -> {
            if (countries.isEmpty()) {
                return requestAllCountries().toObservable();
            } else {
                return Single.just(countries).toObservable();
            }
        });
    }

    private Single<List<Country>> requestAllCountries() {
        RequestHelper requestHelper = new RequestHelper();
        return requestHelper.request(URL_COUNTY)
                .map(json -> {
                    JSONObject jsonObject = new JSONObject(json);
                    String countriesJson = jsonObject.getString(geoNames);
                    Type listType = new TypeToken<ArrayList<Country>>() {}.getType();
                    List<Country> countryList = new Gson().fromJson(countriesJson, listType);
                    saveInDB(countryList);
                    return countryList;
                });
    }

    private Single<List<Country>> loadFromDB() {
        return Single.create((SingleOnSubscribe<List<Country>>) emitter -> {
            List<Country> countries = DBHelper.INSTANCE.getDatabase().countryDao().getAll();
            emitter.onSuccess(countries);
        })
                .compose(RxHelper.INSTANCE.<List<Country>>asyncToUiSingle());
    }

    @SuppressLint("CheckResult")
    private void saveInDB(List<Country> countryList){
        Completable.fromAction(() -> DBHelper.INSTANCE.getDatabase().countryDao().insertAll(countryList)).compose(RxHelper.INSTANCE.asyncToUiCompletable())
        .subscribe(() -> Log.i(TAG, "saved countries"), RxHelper.INSTANCE.errorAction(TAG, "failed to save countries"));
    }

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
