package handler;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.util.Scanner;
import org.json.JSONException;
import java.io.IOException;
import java.net.UnknownHostException;


public class WeatherHandler extends Handler{
    private static final String WEATHER_CENTER_FILE_PATH = "/weather_center_data.csv";
    private static final String LOCATION_NAME_URL_PATH = "/location_name_url.csv";
    private static String predictUrl;
    private static String currentUrl;
    private static String key;
    private ArrayList<Map<String, ArrayList<String>>> predict_weather;
    private Map<String, ArrayList<String>> current_weather;
    private final String location;

    public WeatherHandler(){
        ifOutput = true;
        this.location = "基隆市";   //預設
    }
    public WeatherHandler(String location){
        ifOutput = true;
        this.location = location;   
    }
    public ArrayList<Map<String, ArrayList<String>>> getPredictWeather()throws Exception{
        if(predict_weather==null){
            throw new NullPointerException("predict_weather hasn't been initalize");
        }
        return predict_weather;
    }
    public Map<String, ArrayList<String>> getCurrentWeather()throws Exception{
        if(current_weather==null){
            throw new NullPointerException("current_weather hasn't been initalize");
        }
        return current_weather;
    }
    public String getHttp(String url)throws Exception{
        String allData =new String();
        try{
            Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8.toString());
            scanner.useDelimiter("\\A");
            allData =scanner.next();
            return allData;
        }
        catch(IOException e){
            throw new UnknownHostException("wrong url resource(check csv file)");
        }
    }
    public void produceCurrentWeather(JSONObject json)throws Exception{
        Map<String, ArrayList<String>> weather= new HashMap<String, ArrayList<String>>();
        String temp= new String();
        String humd= new String(); //相對濕度
        ArrayList<String> tempList = new ArrayList<String>();
        ArrayList<String> humdList = new ArrayList<String>();
        try{
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            for(int i=0; i<allLocation.length(); i++){
                JSONObject J= allLocation.getJSONObject(i);
                String city = J.getJSONArray("parameter").getJSONObject(0).get("parameterValue").toString();
                if(city.equals(location)){
                    
                    JSONArray weatherElement= J.getJSONArray("weatherElement");
                    temp= weatherElement.getJSONObject(3).get("elementValue").toString();
                    tempList.add(temp);
                    humd= weatherElement.getJSONObject(4).get("elementValue").toString();
                    humdList.add(humd);
                    break;
                }
            }
        }catch(Exception e){
            throw new JSONException("\nThe JSON file from current weather's url has error");
        }
        weather.put("溫度（攝氏）",tempList);
        weather.put("相對濕度",humdList);
        current_weather=  weather;
    }

    // 指定地區的預測天氣
    public void producePredictWeather(JSONObject json)throws Exception{
        // System.out.println(json);
        ArrayList<Map<String, ArrayList<String>>> List= new ArrayList<Map<String, ArrayList<String>>>();
        try{
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            JSONObject jsonWeather= allLocation.getJSONObject(0);
            JSONArray elements = jsonWeather.getJSONArray("weatherElement");
            for(int k=0; k<3; k++){
                Map<String, ArrayList<String>> map= new HashMap<String, ArrayList<String>>();
                JSONObject sameTimes = elements.getJSONObject(0).getJSONArray("time").getJSONObject(k);
                ArrayList<String> sttlist = new ArrayList<String>();
                ArrayList<String> endtlist = new ArrayList<String>();
                sttlist.add((String)sameTimes.get("startTime"));
                endtlist.add((String)sameTimes.get("endTime"));
                map.put("startTime",sttlist);
                map.put("endTime",endtlist);
                for(int i=0; i<elements.length();i++){
                    JSONObject times =elements.getJSONObject(i).getJSONArray("time").getJSONObject(k);
                    JSONObject parameter = times.getJSONObject("parameter");
                    ArrayList<String> plist = new ArrayList<String>();
                    plist.add((String)parameter.get("parameterName"));
                    switch(i){
                        case 0:
                        plist.add((String)parameter.get("parameterValue"));
                        break;
                        case 1:
                        plist.add((String)parameter.get("parameterUnit"));
                        break;
                        case 2:
                        plist.add((String)parameter.get("parameterUnit"));
                        break;
                        case 4:
                        plist.add((String)parameter.get("parameterUnit"));
                        break;
                        default:
                        break;
                    }
                    map.put("info_"+i, plist);
                }
                List.add(map);
            }
        }catch(Exception e){
            System.out.println(e);
            throw new JSONException("\nThe JSON file from predict weather's url has error");
        }
        predict_weather = List;
    }

    // 建立資料
    public void weatherInit()throws Exception{
            WeatherAPI weatherAPI =new WeatherAPI(location);
            this.predictUrl= weatherAPI.getPredictDataUrl();
            this.currentUrl= weatherAPI.getCurrentDataUrl();

            String datafromHttp = getHttp(this.predictUrl);
            JSONObject predict_json = new JSONObject(datafromHttp);
            producePredictWeather(predict_json);

            String dataCurrentHttp = getHttp(this.currentUrl);
            JSONObject Jsonfile_C = new JSONObject(dataCurrentHttp);
            produceCurrentWeather(Jsonfile_C);
    }
    @Override
	protected void readConfig(String fileName){
	}
    @Override
    public String toString(){
        try{
            weatherInit();
            ArrayList<Map<String, ArrayList<String>>> list= new ArrayList<Map<String, ArrayList<String>>>(predict_weather);
            list.add(0, current_weather);
            return list.toString();
        }catch(Exception e){
            return "weather handler fail:\n"+e;
        }
    }

}