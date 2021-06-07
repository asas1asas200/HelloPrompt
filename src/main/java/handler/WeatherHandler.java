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
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.net.UnknownHostException;


public class WeatherHandler extends Handler{
    private static final String TOKENS_DIRECTORY_PATH = "tokens/";
    private static final String WEATHER_CENTER_FILE_PATH = "weather_center_data.csv";
    private static final String LOCATION_NAME_URL_PATH = "location_name_url.csv";
    private static String PW_url;
    private static String currentUrl;
    private static String key;
    private ArrayList<Map<Object, Object>> predict_weather;
    private Map<Object, Object> current_weather;
    private final String location;

    public WeatherHandler(){
        ifOutput = true;
        this.location = "基隆市";   //預設
    }
    public WeatherHandler(String location){
        ifOutput = true;
        this.location = location;   
    }
    public ArrayList<Map<Object, Object>> getPredictWeather()throws Exception{
        if(predict_weather==null){
            throw new NullPointerException("predict_weather hasn't been initalize");
        }
        return predict_weather;
    }
    public Map<Object, Object> getCurrentWeather()throws Exception{
        if(current_weather==null){
            throw new NullPointerException("current_weather hasn't been initalize");
        }
        return current_weather;
    }
    public String[] openCsvFile(String path)throws IOException{
        String data= new String();
        try{
            InputStream is = new FileInputStream(path);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));
            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null) {
                sb.append(line).append(",");
                line = buf.readLine();
            }
            buf.close();
            data= sb.toString();
            return data.split(",");
        }
        catch(Exception e){
            throw new IOException("files in "+path+" not exist");
        }
    }

    public void produceUrlFromFile(String fileName)throws Exception{

        String[] str= openCsvFile(fileName);
        for(int i=0; i<str.length; i++) {

            if(str[i].contains("授權碼")){
                this.key= str[i+1];
            }
            if(str[i].contains("36hrs預報網址")){
                this.PW_url= str[i+1];
            }
            if(str[i].contains("當下天氣網址")){
                this.currentUrl= str[i+1];
            }
        }
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
        Map<Object, Object> weather= new HashMap<Object, Object>();
        String temp= new String();
        String HUMD= new String(); //相對濕度

        try{
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            for(int i=0; i<allLocation.length(); i++){
                JSONObject J= allLocation.getJSONObject(i);
                String city = J.getJSONArray("parameter").getJSONObject(0).get("parameterValue").toString();
                if(city.equals(location)){
                    
                    JSONArray weatherElement= J.getJSONArray("weatherElement");
                    temp= weatherElement.getJSONObject(3).get("elementValue").toString();
                    HUMD= weatherElement.getJSONObject(4).get("elementValue").toString();
                    break;
                }
            }
        }catch(Exception e){
            throw new JSONException("\nThe JSON file from current weather's url has error");
        }
        weather.put("溫度（攝氏）", temp);
        weather.put("相對濕度", HUMD);
        current_weather=  weather;
    }

    // 指定地區的預測天氣
    public void producePredictWeather(JSONObject json)throws Exception{
        ArrayList<Map<Object, Object>> List= new ArrayList<Map<Object, Object>>();
        try{
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            JSONObject jsonWeather= allLocation.getJSONObject(0);
            JSONArray Elements = jsonWeather.getJSONArray("weatherElement");
            for(int k=0; k<3; k++){
                Map<Object, Object> map= new HashMap<Object, Object>();
                JSONObject sameTimes = Elements.getJSONObject(0).getJSONArray("time").getJSONObject(k);
                map.put("startTime",sameTimes.get("startTime"));
                map.put("endTime",sameTimes.get("endTime"));
                for(int i=0; i<Elements.length();i++){
                    JSONObject Times =Elements.getJSONObject(i).getJSONArray("time").getJSONObject(k);
                    JSONObject parameter = Times.getJSONObject("parameter");
                    ArrayList<Object> plist = new ArrayList<Object>();
                    plist.add(parameter.get("parameterName"));
                    switch(i){
                        case 0:
                        plist.add(parameter.get("parameterValue"));
                        break;
                        case 1:
                        plist.add(parameter.get("parameterUnit"));
                        break;
                        case 2:
                        plist.add(parameter.get("parameterUnit"));
                        break;
                        case 4:
                        plist.add(parameter.get("parameterUnit"));
                        break;
                        default:
                        break;
                    }
                    map.put("info_"+i, plist);
                }
                List.add(map);
            }
        }catch(Exception e){
            throw new JSONException("\nThe JSON file from predict weather's url has error");
        }
        
        predict_weather = List;
    }
    public void weatherInit()throws Exception{
            produceUrlFromFile(TOKENS_DIRECTORY_PATH+WEATHER_CENTER_FILE_PATH);

            String[] list= openCsvFile(TOKENS_DIRECTORY_PATH+LOCATION_NAME_URL_PATH);
            int i;
            for(i=0; i<list.length; i++){
                if(list[i].contains(location)){
                    PW_url= PW_url+list[i+1];
                    break;
                }
            }
            if(i ==list.length){
                throw new FileNotFoundException();
            }
            String datafromHttp = getHttp(this.PW_url);
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
            ArrayList<Map<Object, Object>> list= new ArrayList<Map<Object, Object>>(predict_weather);
            list.add(0, current_weather);
            return list.toString();
        }catch(Exception e){
            return "weather handler fail:\n"+e;
        }
    }

}