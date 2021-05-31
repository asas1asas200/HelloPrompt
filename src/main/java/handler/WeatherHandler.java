package ntou.cs.java2021.helloprompt;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;
import java.time.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;


import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;

// CWB-E7B5B0DB-C3E7-439E-B21D-56D3B7316D36   授權碼
// 我現在是直接拿輸入授權碼之後的網址獲取json檔
// 中央氣象ＡＰＩ
// 36小時： https://opendata.cwb.gov.tw/dist/opendata-swagger.html#/預報/get_v1_rest_datastore_F_C0032_001  全部
  

public class WeatherHandler extends Handler{
    String Urlhrs = "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-E7B5B0DB-C3E7-439E-B21D-56D3B7316D36";
    private ArrayList<Map<Object, Object>> pointed_weather;
    private final String location;

    public WeatherHandler(){
        // ifOutput = true;
        this.location = "基隆市";   //預設
        weatherInit();
    }
    public WeatherHandler(String location){
        // ifOutput = true;
        this.location = location;   
        weatherInit();
    }
    public String getAllWeather(){
        String datafromHttp= new String();
        try{
            datafromHttp = getHttp(this.Urlhrs);
        }catch(Exception e){
            System.err.println("get error when getting weather info through Url");
        }
        return datafromHttp;
    }
    public Map<Object, Object> getCurrentWeather(){
        return pointed_weather.get(0);
    }
    public ArrayList<Map<Object, Object>> getLocalWeather(){
        return pointed_weather;
    }
    // 若需要從本地檔案取得資料
    // public String produceStringFromFile(String fileName)throws IOException{
    //     InputStream is = new FileInputStream(fileName);
	// 	BufferedReader buf = new BufferedReader(new InputStreamReader(is));
	// 	String line = buf.readLine();
	// 	StringBuilder sb = new StringBuilder();
	// 	while (line != null) {
	// 		sb.append(line).append("\n");
	// 		line = buf.readLine();
	// 	}
	// 	buf.close();
    //     return sb.toString();
    // }

    // 取得根據網址 網路上的資料字串
    public String getHttp(String url)throws IOException{
        String allData =new String();
        try{
            Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8.toString());
            scanner.useDelimiter("\\A");
            allData =scanner.next();
        }
        catch(IOException e){
            System.err.println("error http");
        }
        return allData;

    }
    // 找到指定地區的資訊（並未記錄下來）
    public JSONObject getLocalweatherJson(JSONObject json)throws Exception{
        JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
        JSONObject o= new JSONObject();
        for(int i=0; i<allLocation.length(); i++){
            if(allLocation.getJSONObject(i).get("locationName").equals(location)){
                o = allLocation.getJSONObject(i);
                break;
            }
        }
        return o;
        
    }
    // 指定地區的天氣狀態「取得並整理」
    public ArrayList<Map<Object, Object>> getPointedWeather(JSONObject jsonWeather)throws Exception{
        ArrayList<Map<String, String>> weatherList= new ArrayList<Map<String, String>>();
        JSONArray weatherElement = jsonWeather.getJSONArray("weatherElement");
        for(int j=0; j<weatherElement.length(); j++){
            // index:0 天氣狀態, 1 降雨機率, 2最低溫度 3.體感變化 4.最高溫度
            JSONObject element = weatherElement.getJSONObject(j);
            JSONArray timeArray = element.getJSONArray("time");
            for(int k=0; k<timeArray.length(); k++){
                JSONObject info = timeArray.getJSONObject(k);
                JSONObject parameter = info.getJSONObject("parameter");
                Object parameterName = parameter.get("parameterName");
                Object otherparameter;
                Map<String, String> map = new HashMap<String, String>();

                Object start= info.get("startTime");
                Object end= info.get("endTime");
                map.put("startTime", start.toString());
                map.put("endTime", end.toString());
                map.put("Name", parameterName.toString());
                switch (j){
                    case 0:
                    otherparameter = parameter.get("parameterValue");
                    map.put("Value", otherparameter.toString());
                    break;
                    case 1:
                    otherparameter = parameter.get("parameterUnit");
                    map.put("Unit", otherparameter.toString());
                    break;
                    case 2:
                    otherparameter = parameter.get("parameterUnit");
                    map.put("Unit", otherparameter.toString());
                    break;
                    case 4:
                    otherparameter = parameter.get("parameterUnit");
                    map.put("Unit", otherparameter.toString());
                    break;
                    default:
                        break;
                }
                weatherList.add(map);
                
            }
        }
        // 整理
        ArrayList<Map<Object, Object>> List= new ArrayList<Map<Object, Object>>();
        for(int i=0; i<3;i++){
            Map<Object, Object> temp = new HashMap<Object, Object>();
            temp.put("startTime", weatherList.get(i).get("startTime"));
            temp.put("endTime", weatherList.get(i).get("endTime")); 
            int k=0;
            int p=0;
            for(int j=i; j<weatherList.size(); j= j+3){
                if(j%3==i){
                    ArrayList<String> tarray = new ArrayList<String>();
                    tarray.add(weatherList.get(j).get("Name"));
                    switch(p){
                        case 0:
                        tarray.add(weatherList.get(j).get("Value"));
                        break;
                        case 1:
                        tarray.add(weatherList.get(j).get("Unit"));
                        break;
                        case 2:
                        tarray.add(weatherList.get(j).get("Unit"));
                        break;
                        case 4:
                        tarray.add(weatherList.get(j).get("Unit"));
                        break;
                        default:
                        break;
                    }
                    temp.put("info_"+p, tarray);
                    p= p+1;
                    
                }
            }
            List.add(temp);
        }
        
        
        return List;
    }
    public void weatherInit(){
        try{
            String datafromHttp = getHttp(this.Urlhrs);
            JSONObject Jsonfile = new JSONObject(datafromHttp);
            JSONObject localweather= getLocalweatherJson(Jsonfile);
            this.pointed_weather = getPointedWeather(localweather);
        }catch(Exception e){
            System.err.println("weather init has error");
        }
    }
    @Override
	protected void readConfig(String fileName){
	}
    @Override
    public String toString(){
        String output;
        output = getCurrentWeather().toString();
        return output;
    }

}