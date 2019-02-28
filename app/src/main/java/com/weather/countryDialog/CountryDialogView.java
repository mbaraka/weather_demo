package com.weather.countryDialog;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.weather.R;
import com.weather.model.City;
import com.weather.model.Country;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import io.reactivex.observers.DisposableObserver;

public class CountryDialogView extends LinearLayout implements View.OnClickListener {
	private Spinner countrySpinner;
	private Spinner citySpinner;
	private TextView txtCities;
	private ProgressBar progressBar;
	private Button btnAdd;
	List<City> cityList = new ArrayList<>();
	private City tempCity;

	private List<Country> countryList;
	private CountriesPresenter countriesPresenter = new CountriesPresenter();

	public CountryDialogView(Context context) {
		super(context);
		init();
	}

	public CountryDialogView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CountryDialogView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public CountryDialogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.country_dialog, this, true);
		countrySpinner = findViewById(R.id.countryDialog_spinner_countries);
		adjustCountrySpinner();

		citySpinner = findViewById(R.id.countryDialog_spinner_cities);
		progressBar = findViewById(R.id.countryDialog_prg_city);

		txtCities = findViewById(R.id.countryDialog_txt_cities);

		btnAdd = findViewById(R.id.countryDialog_btn_add);
		btnAdd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (!cityList.contains(tempCity) && tempCity != null) {
			cityList.add(tempCity);
			StringBuilder stringBuilder = new StringBuilder();
			for (City city : cityList) {
				stringBuilder.append(city.name).append(", ");
			}
			txtCities.setText(stringBuilder.toString());
		}
	}

	private void adjustCountrySpinner() {

		countriesPresenter.getCountries().subscribe(new DisposableObserver<List<Country>>() {
			@Override
			public void onNext(List<Country> countries) {
				countryList = countries;
				ArrayAdapter<Country> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countries);
				countrySpinner.setAdapter(adapter);
			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(getContext(), "Sorry couldn't load countries", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onComplete() {

			}
		});

		countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				adjustCitySpinner(countryList.get(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
		});
	}

	private void adjustCitySpinner(Country country) {
		progressBar.setVisibility(View.VISIBLE);
		clearCitiesSpinner();

		CitiesPresenters.INSTANCE.getCities(country).subscribe(new DisposableObserver<List<City>>() {
			@Override
			public void onNext(List<City> cities) {
				progressBar.setVisibility(View.GONE);
				ArrayAdapter<City> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cities);
				citySpinner.setAdapter(adapter);
			}

			@Override
			public void onError(Throwable e) {
				Toast.makeText(getContext(), "Sorry couldn't load cities", Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onComplete() { }
		});

		citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tempCity = (City) citySpinner.getAdapter().getItem(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) { }
		});
	}

	private void clearCitiesSpinner() {
		ArrayAdapter<City> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
		citySpinner.setAdapter(adapter);
		tempCity = null;
	}
}
