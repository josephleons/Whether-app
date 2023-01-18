package com.example.weather_app

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val city :String ="Morogoro dc"
    val app :String="c3482f88c0ac4986993132702231801"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }
    inner class weatherTask():AsyncTask<String,Void,String>(){
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility= View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility= View.GONE
            findViewById<TextView>(R.id.errortext).visibility =View.GONE

        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try {
                response = URL("http://api.weatherapi.com/v1/weather?q=$city &units=metric&app's=$app")
                    .readText(charset("Charsets"))
            }catch (e:Exception){
                response =null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj =JSONObject(result)
                val main =jsonObj.getJSONObject("main")
                val sys =jsonObj.getJSONObject("sys")
                val wind =jsonObj.getJSONObject("wind")
                val weather=jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt :Long=jsonObj.getLong("dt")
                val updatedAtText ="updated at:"+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val temp =main.getString("temp")+"oC"
                val tempMin ="Min Temp:"+main.getString("temp_min")+"oC"
                val tempMax ="Max Tenp:"+main.getString("temp_max")+"oC"
                val pressure =main.getString("pressure")
                val huminity =main.getString("huminity")
                val sunrise :Long =sys.getLong("sunrise")
                val sunset :Long =sys.getLong("sunset")
                val windspeed =wind.getString("speed")
                val weatherDescription =weather.getString("description")
                val address =jsonObj.getString("name")+", "+sys.getString("country")


                findViewById<TextView>(R.id.Address).text=address
                findViewById<TextView>(R.id.update_at).text=updatedAtText
                findViewById<TextView>(R.id.status).text=weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text=temp
                findViewById<TextView>(R.id.temp_min).text=tempMin
                findViewById<TextView>(R.id.temp_max).text=tempMax
                findViewById<TextView>(R.id.sunrise).text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text=SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text=windspeed
                findViewById<TextView>(R.id.Pressure).text=pressure
                findViewById<TextView>(R.id.huminity).text=huminity

                findViewById<ProgressBar>(R.id.loader).visibility=View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility= View.GONE

            }catch (e:java.lang.Exception){
                findViewById<ProgressBar>(R.id.loader).visibility=View.GONE
                findViewById<TextView>(R.id.errortext).visibility= View.VISIBLE

            }
        }
    }
}