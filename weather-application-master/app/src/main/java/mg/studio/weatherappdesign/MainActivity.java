package mg.studio.weatherappdesign;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import mg.studio.weatherappdesign.R;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    int count = 0;

    public void btnClick(View view) {
        ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        // 获取当前的网络连接是否可用
        if (networkInfo == null){
            Toast.makeText(this,"The network connection is unavailable", Toast.LENGTH_SHORT).show();
        }
        else{
            boolean available = networkInfo.isAvailable();
            if (available){
                if(count==0){
                    Log.i("Info", "The network connection is available");
                    Toast.makeText(this, "The network connection is available", Toast.LENGTH_SHORT).show();
                    new DownloadUpdate2().execute();
                    count++;
                }
                else{
                    Toast.makeText(this, "The update has been completed.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Log.i("Info", "The current network connection is unavailable");
                Toast.makeText(this, "The current network connection is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //用于处理异步任务。
    // AsyncTask（异步任务）: 这是一个添加在API level 3中的类，
    // 它可以在这个天气应用中帮助我们正确并简单的使用界面线程（UI thread）。
    // 我们需要从互联网上获取一些数据, 这是一个正常情况下几秒之内就完成的行为 (在一个好的网络环境中).
    // 我们需要使用 asynchronous task 来在界面背后处理下载来的数据并且避免界面停顿的情况.
    private class DownloadUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://mpianatra.com/Courses/info.txt";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
        }
    }

    private class DownloadUpdate2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = "https://mpianatra.com/Courses/forecast.json";
            HttpURLConnection urlConnection = null;
            BufferedReader reader;
            try {
                URL url = new URL(stringUrl);
                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    Log.d("TAG", line);
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();//这里返回的温度
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            try{
                //得到List的jasonArray
                JSONObject jsonObject = new JSONObject(temperature);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                int length = jsonArray.length();
                String[] temp;
                temp = new String[length];
                String[] icon;
                icon = new String[length];
                int i;
                //设置位置
                String location = jsonObject.getJSONObject("city").getString("name");
                ((TextView) findViewById(R.id.tv_location)).setText(location);
                //设置日期
                //日期
                DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
                String s= jsonArray.getJSONObject(0).getString("dt_txt");
                Date date =  formatter.parse(s);
                ((TextView) findViewById(R.id.tv_date)).setText(s);//设置第一天日期
                //下面设置未来四天是星期几
                Calendar calendar = new GregorianCalendar(); // 定义calendar对象
                calendar.setTime(date); // 把当前时间赋值给calendar
                calendar.add(calendar.DATE, 1); // 在日期中增加天数1天
                Date d1 = calendar.getTime(); // 把calendar转换回日期格式
                String s1 = getWeekOfDate(d1);
                ((TextView) findViewById(R.id.txt_01)).setText(s1);
                calendar.add(calendar.DATE, 1);
                Date d2 = calendar.getTime();
                String s2 = getWeekOfDate(d2);
                ((TextView) findViewById(R.id.txt_02)).setText(s2);
                calendar.add(calendar.DATE, 1);
                Date d3 = calendar.getTime();
                String s3 = getWeekOfDate(d3);
                ((TextView) findViewById(R.id.txt_03)).setText(s3);
                calendar.add(calendar.DATE, 1);
                Date d4 = calendar.getTime();
                String s4 = getWeekOfDate(d4);
                ((TextView) findViewById(R.id.txt_04)).setText(s4);
                //设置温度
                for(i=0;i<length;i++){
                    temp[i] = jsonArray.getJSONObject(i).
                            getJSONObject("main").getString("temp");
                }
                //把华氏温度转为摄氏温度
                for(i=0;i<5;i++){
                    int m=i*8;
                    Double temp0 = Double.parseDouble(temp[m]);
                    temp0 = temp0-273.15;
                    temp[m] = String.valueOf(Double.valueOf(temp0).intValue());
                }
                ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temp[0]);
                ((TextView) findViewById(R.id.temp_01)).setText(temp[8]+"℃");
                ((TextView) findViewById(R.id.temp_02)).setText(temp[16]+"℃");
                ((TextView) findViewById(R.id.temp_03)).setText(temp[24]+"℃");
                ((TextView) findViewById(R.id.temp_04)).setText(temp[32]+"℃");
                //设置图标
                for(i=0;i<length;i++){
                    icon[i] = jsonArray.getJSONObject(i).
                            getJSONArray("weather").getJSONObject(0).getString("main");
                }
                ((ImageView)findViewById(R.id.img_weather_condition)).setImageDrawable(getResources().getDrawable(setImage(icon[0])));
                ((ImageView)findViewById(R.id.img_01)).setImageDrawable(getResources().getDrawable(setImage(icon[2])));
                ((ImageView)findViewById(R.id.img_02)).setImageDrawable(getResources().getDrawable(setImage(icon[10])));
                ((ImageView)findViewById(R.id.img_03)).setImageDrawable(getResources().getDrawable(setImage(icon[18])));
                ((ImageView)findViewById(R.id.img_04)).setImageDrawable(getResources().getDrawable(setImage(icon[26])));
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    //通过输入日期获得星期几
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        Calendar calendar = Calendar.getInstance();
        if(date != null){
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekOfDays[w];
    }

    //通过图标名称设置图片
    public static int setImage(String icon){
        if(icon.equals(new String("Clouds"))){
            return R.drawable.partly_sunny_small;
        }
        else if(icon.equals(new String("Clear"))){
            return R.drawable.sunny_small;
        }
        else if(icon.equals(new String("Rainny"))){
            return R.drawable.rainy_small;
        }
        else if(icon.equals(new String("Windy"))){
            return R.drawable.windy_small;
        }
        else return R.drawable.notification;
    }
}
