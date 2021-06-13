//@formatter:off
package handler;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

import snippets.ANSIColor;
import snippets.WeatherImage;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;

import java.util.Scanner;
import org.json.JSONException;
import java.io.IOException;
import java.net.UnknownHostException;


public class WeatherHandler extends Handler{
    private ArrayList<Map<String, ArrayList<String>>> predict_weather;
    private Map<String, ArrayList<String>> current_weather;
    private final String location;

    private class PredictInfoParser {
        public final String DESC;
        public final String CHANCE_OF_RAIN;
        public final String LOW_TEMP;
        public final String FEEL;
        public final String HIGH_TEMP;
        public final String BEGIN;
        public final String END;
        public final List<String> WEATHER_IMAGE;
        public final List<String> TEMP_IMAGE;

        public PredictInfoParser(Map<String, ArrayList<String>> weatherInfo){
            DESC = weatherInfo.get("天氣現象").get(0);
            CHANCE_OF_RAIN = weatherInfo.get("降雨機率").get(0);
            LOW_TEMP = weatherInfo.get("最低溫度").get(0);
            FEEL = weatherInfo.get("舒適度").get(0);
            HIGH_TEMP = weatherInfo.get("最高溫度").get(0);
            BEGIN = weatherInfo.get("startTime").get(0)
                                .substring(5, 13)
                                .replace(" ", "@")
                                .replace("-", "/");
            END = weatherInfo.get("endTime").get(0)
                                .substring(5, 13)
                                .replace(" ", "@")
                                .replace("-", "/");
            WEATHER_IMAGE = WeatherImage.getWeatherImage(DESC);
            TEMP_IMAGE = WeatherImage.getTempImage(HIGH_TEMP, LOW_TEMP, false);
        }
    }

    public WeatherHandler(){
        ifOutput = true;
        this.location = "基隆市";   //預設
    }
    public WeatherHandler(String location){
        ifOutput = true;
        this.location = location;   
    }
    public String getHttp(String url)throws Exception{
        try{
            String allData;
            Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8.toString());
            scanner.useDelimiter("\\A");
            allData =scanner.next();
            scanner.close();
            return allData;
        }
        catch(UnknownHostException e){
            throw new UnknownHostException("wrong url resource(check csv file)");
        }
    }
// TEMP 溫度 攝氏單位
// HUMD 空氣濕度 相對濕度(0< HUMD<1)
// WDIR 風向，單位 度，一般風向 0 表示無風
// WDSD 風速，單位 公尺/秒
// D_TX 本日最高溫，單位 攝氏
// D_TXT 本日最高溫發生時間，hhmm (小時分鐘)
// D_TN 本日最低溫，單位 攝氏
// D_TNT 本日最低溫發生時間，hhmm (小時分鐘)
    public void produceCurrentWeather(JSONObject json)throws Exception{
        List<String> elementsName_EN = Arrays.asList("TEMP", "HUMD","WDIR","WDSD","D_TX","D_TXT","D_TN","D_TNT");
        List<String> elementsName_CH = Arrays.asList("現在溫度","濕度","風向","風速","本日最高溫","本日最高溫發生時間","本日最低溫","本日最低溫發生時間");
        Map<String, ArrayList<String>> weather= new HashMap<String, ArrayList<String>>();
        String temp;
        String humd; //相對濕度
        ArrayList<String> tempList = new ArrayList<String>();
        ArrayList<String> humdList = new ArrayList<String>();
        try{
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            for(int i=0; i<allLocation.length(); i++){
                JSONObject J= allLocation.getJSONObject(i);
                String city = J.getJSONArray("parameter").getJSONObject(0).get("parameterValue").toString();
                if(city.equals(location)){
                    JSONArray weatherElement= J.getJSONArray("weatherElement");
                    for(int en=0; en<weatherElement.length(); en++){
                        ArrayList<String> elementList = new ArrayList<String>();
                        String elementN= weatherElement.getJSONObject(en).get("elementName").toString();
                        String elementV= weatherElement.getJSONObject(en).get("elementValue").toString();
                        elementList.add(elementV);
                        for(int k=0; k<elementsName_EN.size(); k++){
                            if(elementN.equals(elementsName_EN.get(k))){
                                weather.put(elementsName_CH.get(k),elementList);
                            }
                        }
                        
                    }
                }
            }
        }catch(Exception e){
            throw new JSONException("\nThe JSON file from current weather's url has error");
        }
        current_weather=  weather;
    }

    // 指定地區的預測天氣
    public void producePredictWeather(JSONObject json)throws Exception{
        List<String> conditons= Arrays.asList("天氣現象", "降雨機率", "最低溫度", "舒適度" ,"最高溫度");
        ArrayList<Map<String, ArrayList<String>>> weatherList= new ArrayList<Map<String, ArrayList<String>>>();
        try{
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            // JSONObject jsonWeather= allLocation.getJSONObject(0);
            JSONArray elements = allLocation.getJSONObject(0).getJSONArray("weatherElement");
            JSONArray timeArray = elements.getJSONObject(0).getJSONArray("time");
            for(int k=0; k<timeArray.length(); k++){
                Map<String, ArrayList<String>> map= new HashMap<String, ArrayList<String>>();
                JSONObject sameTimes = timeArray.getJSONObject(k);
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
                    map.put(conditons.get(i), plist);
                }
                weatherList.add(map);
            }
        }catch(JSONException e){
            // throw new JSONException("\nThe JSON file from predict weather's url has error");
        } finally {
            predict_weather = weatherList;
        }
    }

    // 建立資料
    public void weatherInit()throws Exception{
            WeatherAPI weatherAPI =new WeatherAPI(location);

            String datafromHttp = getHttp(weatherAPI.getPredictDataUrl());
            JSONObject predict_json = new JSONObject(datafromHttp);
            producePredictWeather(predict_json);

            String dataCurrentHttp = getHttp(weatherAPI.getCurrentDataUrl());
            JSONObject Jsonfile_C = new JSONObject(dataCurrentHttp);
            produceCurrentWeather(Jsonfile_C);
    }
    @Override
	protected void readConfig(String fileName){
	}

    private String render(){
        StringBuilder output = new StringBuilder();
        List<PredictInfoParser> predictInfos = new ArrayList<PredictInfoParser>();
        for(Map<String, ArrayList<String>> weatherInfo: predict_weather){
            predictInfos.add(new PredictInfoParser(weatherInfo));
        }

        // Width of per time: 12
        for(PredictInfoParser info: predictInfos){
            output.append(String.format("    %s ~ %s   ", info.BEGIN, info.END));
            output.append("|");
        }
        output.append("\n");

        // Width of WeatherImage: 13
        // Width of TempImage: 13
        for(int i=0;i<5;i++){
            for(PredictInfoParser info: predictInfos){
                output.append(info.TEMP_IMAGE.get(i));
                output.append(info.WEATHER_IMAGE.get(i));
                output.append("|");
            }
            output.append("\n");
        }

        for(PredictInfoParser info: predictInfos){
            // 9 is temp image layout space: 13 - " (_)".length()
            String sizeFormat = "%" + (13 + 9 -info.DESC.length()) + "s";// only support chinese character
            output.append(info.TEMP_IMAGE.get(5));
            output.append(String.format(sizeFormat, info.DESC));
            output.append("|");
        }
        output.append("\n");

        for(PredictInfoParser info: predictInfos){
            output.append("FEEL: ");
            // 26 is temp + weather image layout length
            // 11 is rainy + "FEEL: " layout length
            String sizeFormat = "%-" + (26 - 11 - info.FEEL.length()) + "s";// only support chinese character
            output.append(String.format(sizeFormat, info.FEEL));
            output.append("🌧️ :" + ANSIColor.BLUE+ String.format("%2s", info.CHANCE_OF_RAIN) + ANSIColor.RESET);
            output.append("|");
        }
        output.append("\n");

        return output.toString();
    }

    @Override
    public String toString(){
        try{
            weatherInit();
        }catch(Exception e){
            return "weather handler fail:\n"+e;
        }
        return render();
    }

}