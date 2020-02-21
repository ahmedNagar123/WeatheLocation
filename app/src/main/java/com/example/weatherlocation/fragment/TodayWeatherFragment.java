package com.example.weatherlocation.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherlocation.Common.Common;
import com.example.weatherlocation.Model.WeatherResult;
import com.example.weatherlocation.R;
import com.example.weatherlocation.Retrofit.IOpenWeatherMap;
import com.example.weatherlocation.Retrofit.RetrofitClint;
import com.squareup.picasso.Picasso;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {

    ImageView img_weather;
    TextView txt_city_name,txt_tempreture,txt_descreption,txt_date_time,txt_wind,txt_pressure,txt_humidity,txt_sunrise,txt_sunset,txt_geo_coord;
    LinearLayout weather_panal;
    ProgressBar loading;


    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    static TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance(){
        if (instance ==null)
            instance=new TodayWeatherFragment();
        return instance;
    }

    public TodayWeatherFragment() {

        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit= RetrofitClint.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_today_weather, container, false);
        txt_city_name=itemView.findViewById(R.id.txt_city_name);
        txt_date_time=itemView.findViewById(R.id.txt_date_time);
        txt_descreption=itemView.findViewById(R.id.txt_descreption);
        txt_geo_coord=itemView.findViewById(R.id.txt_geo_coord);
        txt_humidity=itemView.findViewById(R.id.txt_humidity);
        txt_pressure=itemView.findViewById(R.id.txt_pressure);
        txt_sunrise=itemView.findViewById(R.id.txt_sunrise);
        txt_sunset=itemView.findViewById(R.id.txt_sunset);
        txt_tempreture=itemView.findViewById(R.id.txt_tempreture);
        txt_wind=itemView.findViewById(R.id.txt_wind);
        img_weather=itemView.findViewById(R.id.img_weather);

        weather_panal=itemView.findViewById(R.id.weather_panal);
        loading=itemView.findViewById(R.id.loading);

        getWeatherInformation();


        return itemView;

    }

    private void getWeatherInformation() {
        compositeDisposable.add(mService.getWeatherByLatLng(String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric").
                subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<WeatherResult>() {
            @Override
            public void accept(WeatherResult weatherResult) throws Exception {

                Picasso.get().load(new StringBuilder("http://openweathermap.org/img/w/")
                .append(weatherResult.getWeather().get(0).getIcon())
                .append(".png").toString()).into(img_weather);

                txt_city_name.setText(weatherResult.getName());
                txt_descreption.setText(new StringBuilder("Weather in ").append(weatherResult.getName()).toString());
                txt_tempreture.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                txt_date_time.setText(Common.convertUnixToDate(weatherResult.getDt()));
                txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                txt_sunrise.setText(Common.convertUnixToNour(weatherResult.getSys().getSunrise()));
                txt_sunset.setText(Common.convertUnixToNour(weatherResult.getSys().getSunset()));
                txt_geo_coord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());


                //Display panel
                weather_panal.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);


            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                Toast.makeText(getActivity(), ""+throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

}
