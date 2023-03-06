package com.example.weather_forcast_app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather_forcast_app.R
import com.example.weather_forcast_app.utils.Constants.Companion.IMG_URL
import com.example.weather_forcast_app.utils.getDateTime
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var progressBar: ProgressBar

    //view
    private lateinit var city: TextView
    private lateinit var date: TextView
    private lateinit var weatherDescription: TextView
    private lateinit var tempTextView: TextView
    private lateinit var tempTypeTextView: TextView //°C مثلا
    private lateinit var tempIcon: ImageView
    private lateinit var hourlyWeatherRc: RecyclerView
    private lateinit var dailyWeatherRc: RecyclerView
    private lateinit var cardView: CardView
    private lateinit var cardView2: CardView

    private lateinit var pressureTxt: TextView
    private lateinit var humidityTxt: TextView
    private lateinit var windTxt: TextView
    private lateinit var cloudTxt: TextView
    private lateinit var ultraVioletTxt: TextView
    private lateinit var visibilityTxt: TextView
    private lateinit var hourlyWeathersAdapter: HourlyAdapter
    private lateinit var daysWeathersAdapter: DailyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        //putDataOnView()
        viewModel.getCurrentWeather(
            29.9642,
            32.5056,
            "metric",
            "en",
            "67ca8d4acae59d540ea421e817caf1bb"
        )
        lifecycleScope.launch {
            viewModel.currentWeather.collect {
                if(it!=null){
                    println(it.toString())
                    var weatherCurrent = it.current
                    var weatherDesc = weatherCurrent.weather.get(0)
                    date.text = getDateTime(
                        weatherCurrent.dt,
                        "EEE, d MMM ","en"
                    )
                    weatherDescription.text = weatherDesc.description
                    tempTextView.text = weatherCurrent.temp.toString()
                    tempTypeTextView.text = "C"
                    Picasso.get().load("${IMG_URL}${weatherDesc.icon}@4x.png")

                    pressureTxt.text = weatherCurrent.pressure.toString()
                    humidityTxt.text = weatherCurrent.humidity.toString()
                    cloudTxt.text = weatherCurrent.clouds.toString()
                    visibilityTxt.text = weatherCurrent.clouds.toString()
                    windTxt.text = weatherCurrent.wind_speed.toString()

                    daysWeathersAdapter.setDays(it.daily)

                    hourlyWeathersAdapter.sethours(it.hourly)
                }


            }
        }


    }

    private fun init(view: View) {


        city = view.findViewById(R.id.tvCity)
        date = view.findViewById(R.id.tvDateTime)
        weatherDescription = view.findViewById(R.id.tvHomeWeatherDescription)
        tempTextView = view.findViewById(R.id.tvTemp)
        tempTypeTextView = view.findViewById(R.id.tvHomeTempDisc) //°C مثلا
        tempIcon = view.findViewById(R.id.ivWeatherIcon)
        hourlyWeatherRc = view.findViewById(R.id.recyclerViewHourly)
        dailyWeatherRc = view.findViewById(R.id.recyclerViewDaily)
        progressBar = view.findViewById(R.id.HomeprogressBar)
        cardView = view.findViewById(R.id.HomecardView)
        hourlyWeathersAdapter = HourlyAdapter(emptyList(), view.context)
        daysWeathersAdapter = DailyAdapter(view.context, emptyList())
        var layoutManager = LinearLayoutManager(view.context)
        hourlyWeatherRc.apply {
            setHasFixedSize(true)
            layoutManager.orientation = RecyclerView.HORIZONTAL
            this.layoutManager = layoutManager
            adapter = hourlyWeathersAdapter
        }

        var layoutManagerDays = LinearLayoutManager(view.context)
        dailyWeatherRc.apply {
            setHasFixedSize(true)
            layoutManagerDays.orientation = RecyclerView.VERTICAL
            this.layoutManager = layoutManagerDays
            adapter = daysWeathersAdapter
        }

        pressureTxt = view.findViewById(R.id.tvPressure)
        humidityTxt = view.findViewById(R.id.tvHumidity)
        windTxt = view.findViewById(R.id.txtViewWindSpeed)
        cloudTxt = view.findViewById(R.id.tvClouds)
        ultraVioletTxt = view.findViewById(R.id.tvUVI)
        visibilityTxt = view.findViewById(R.id.tvVisibility)

    }

    private fun putDataOnView() {
        lifecycleScope.launch {
            viewModel.currentWeather.collect {

                println(it?.current?.dt)
                var weatherCurrent = it?.current
                var weatherDesc = weatherCurrent?.weather?.get(0)
                date.text = getDateTime(
                    weatherCurrent!!.dt,
                    "EEE, d MMM ","en"
                )
                weatherDescription.text = weatherDesc!!.description
                tempTextView.text = weatherCurrent.temp.toString()
                tempTypeTextView.text = "C"
                Picasso.get().load("${IMG_URL}${weatherDesc.icon}@4x.png")

                pressureTxt.text = weatherCurrent.pressure.toString()
                humidityTxt.text = weatherCurrent.humidity.toString()
                cloudTxt.text = weatherCurrent.clouds.toString()
                visibilityTxt.text = weatherCurrent.clouds.toString()
                windTxt.text = weatherCurrent.wind_speed.toString()

                daysWeathersAdapter.setDays(it!!.daily)

                hourlyWeathersAdapter.sethours(it.hourly)
            }



        }
    }
}