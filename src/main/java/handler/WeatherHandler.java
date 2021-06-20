package handler;

import org.json.JSONArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Date;
import java.util.Arrays;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;
import org.json.JSONException;

import snippets.ANSIColor;
import snippets.WeatherImage;

/**
 * This handler can collect weather information if weatherInfo was set.<br>
 * 
 * @see Handler
 * 
 */
public class WeatherHandler extends Handler {

    /**
     * This class can produce the url of the weather information from network.
     * <p>
     * Can get weather in the next 36 hours.
     * </p>
     */
    private static class WeatherAPI {
        private static final String AUTHORITY_FILE_PATH = "/authority_key.csv";

        // url : apiUrl+ 授權碼+ 想搜尋的類別+ 時間+ 地點
        private final String apiUrl = "https://opendata.cwb.gov.tw/api/v1/rest/datastore";
        private final String immediate = "/O-A0003-001";
        private final String current = "/O-A0001-001";
        private final String predict = "/F-C0032-001"; // 後36hrs預報

        private static String key;
        private static List<String> weatherElements = Arrays.asList("WDIR", "WDSD", "D_TX", "D_TXT", "D_TN", "D_TNT",
                "TEMP", "HUMD");
        private final String now;
        private final String locationUrl;

        public WeatherAPI(String location) throws Exception {
            now = getTime();
            locationUrl = "&locationName=" + location;
            if (locationUrl == null) {
                throw new IllegalArgumentException("Unknown location");
            }
            produceDateFromFile(AUTHORITY_FILE_PATH);
        }

        public String getCurrentDataUrl() {
            // 若需要指抓取特定 element
            // StringBuilder elements = new StringBuilder();
            // for (int i = 0; i < weatherElements.size(); i++) {
            // if (i == 0) {
            // elements.append(weatherElements.get(i));
            // }
            // elements.append("," + weatherElements.get(i));
            // }
            // return apiUrl + current + "?Authorization=" + key + "&elementName=" +
            // elements
            // + "&parameterName%EF%BC%8C=CITY";
            return apiUrl + current + "?Authorization=" + key + "&parameterName%EF%BC%8C=CITY";
        }

        public String getImmediateDataUrl() {
            // 若需要指抓取特定 element
            // StringBuilder elements = new StringBuilder();
            // for (int i = 0; i < weatherElements.size(); i++) {
            // if (i == 0) {
            // elements.append(weatherElements.get(i));
            // }
            // elements.append("," + weatherElements.get(i));
            // }
            // return apiUrl + immediate + "?Authorization=" + key + "&elementName=" +
            // elements
            // + "&parameterName%EF%BC%8C=CITY";
            return apiUrl + immediate + "?Authorization=" + key + "&parameterName%EF%BC%8C=CITY";
        }

        public String getPredictDataUrl() {
            return apiUrl + predict + "?Authorization=" + key + "&timeFrom=" + now + locationUrl;
        }

        /**
         * Open and read a .csv file.
         * 
         * @return {@code String[]} csv data.
         */
        public String[] openCsvFile(String path) throws IOException {
            String data = new String();
            try {
                InputStream is = WeatherHandler.class.getResourceAsStream(path);
                BufferedReader buf = new BufferedReader(new InputStreamReader(is));
                String line = buf.readLine();
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    sb.append(line).append(",");
                    line = buf.readLine();
                }
                buf.close();
                data = sb.toString();
                return data.split(",");
            } catch (Exception e) {
                throw new IOException("files in " + path + " not exist");
            }
        }

        public void produceDateFromFile(String fileName) throws Exception {

            String[] str = openCsvFile(fileName);
            for (int i = 0; i < str.length; i++) {

                if (str[i].contains("授權碼")) {
                    key = str[i + 1];
                }
            }
        }

        public String getTime() {
            Date current = new Date();// 取時間
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh'%3A'mm'%3A'ss");
            return formatter.format(current);
        }

        // 回傳後36hrs的url (String)
        public String toString() {
            return apiUrl + predict + "?Authorization=" + key + "&timeFrom=" + now + locationUrl;

        }

    }

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

        public PredictInfoParser(Map<String, ArrayList<String>> weatherInfo) {
            DESC = weatherInfo.get("天氣現象").get(0);
            CHANCE_OF_RAIN = weatherInfo.get("降雨機率").get(0);
            LOW_TEMP = weatherInfo.get("最低溫度").get(0);
            FEEL = weatherInfo.get("舒適度").get(0);
            HIGH_TEMP = weatherInfo.get("最高溫度").get(0);
            BEGIN = weatherInfo.get("startTime").get(0).substring(5, 13).replace(" ", "@").replace("-", "/");
            END = weatherInfo.get("endTime").get(0).substring(5, 13).replace(" ", "@").replace("-", "/");
            WEATHER_IMAGE = WeatherImage.getWeatherImage(DESC);
            TEMP_IMAGE = WeatherImage.getTempImage(HIGH_TEMP, LOW_TEMP, false);
        }
    }

    private ArrayList<Map<String, ArrayList<String>>> predict_weather;
    private Map<String, String> current_weather;
    private Map<String, String> immediate_weather;
    private final String location;

    /**
     * Initialize WeatherHandler.<br>
     * Set default location as 基隆市
     */
    public WeatherHandler() {
        this.location = "基隆市"; // 預設
    }

    /**
     * Initialize WeatherHandler.
     * 
     * @param location living city name(chinese)
     */
    public WeatherHandler(String location) {
        ifOutput = true;
        this.location = location;
    }

    /**
     * Get data in string from given url.
     * 
     * @param url weather API.
     * @return {@code String} resource from url.
     * @throws UnknownHostException If the given url has error.
     */
    public String getHttp(String url) throws UnknownHostException {
        try {
            String allData;
            Scanner scanner = new Scanner(new URL(url).openStream(), StandardCharsets.UTF_8.toString());
            scanner.useDelimiter("\\A");
            allData = scanner.next();
            scanner.close();
            return allData;
        } catch (UnknownHostException e) {
            throw new UnknownHostException("wrong url resource(check csv file)");
        } catch (Exception e) {
            System.err.println("System error");
        }
        return "";
    }

    /**
     * Produce data from given JSONObject.
     * <p>
     * 搜集並整理「最近」天氣資料.
     * 
     * @param json a json file containing weather information.
     * @return {@code Map<String, String>} contain many weather element.
     * @throws JSONException If input JSONObject can be achieve the method's
     *                       require.
     */
    public Map<String, String> producePerspectiveWeather(JSONObject json) throws JSONException {
        List<String> elementsName_EN = Arrays.asList("Weather", "TEMP", "HUMD", "WDIR", "WDSD", "D_TX", "D_TXT", "D_TN",
                "D_TNT", "PRES", "24R", "H_FX", "H_XD", "H_UVI", "D_TS");
        List<String> elementsName_CH = Arrays.asList("天氣狀態", "現在溫度", "濕度", "風向", "風速", "本日最高溫", "本日最高溫發生時間", "本日最低溫",
                "本日最低溫發生時間", "氣壓", "日累積雨量", "小時最大陣風風速", "小時最大陣風風向", "小時紫外線指數", "本日總日照時數");
        Map<String, String> weather = new HashMap<String, String>();
        try {
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            for (int i = 0; i < allLocation.length(); i++) {
                JSONObject J = allLocation.getJSONObject(i);
                String obsTime = J.getJSONObject("time").get("obsTime").toString();
                String city = J.getJSONArray("parameter").getJSONObject(0).get("parameterValue").toString();
                if (city.equals(location)) {
                    weather.put("觀測時間", obsTime);
                    JSONArray weatherElement = J.getJSONArray("weatherElement");
                    for (int en = 0; en < weatherElement.length(); en++) {
                        ArrayList<String> elementList = new ArrayList<String>();
                        String elementN = weatherElement.getJSONObject(en).get("elementName").toString();
                        String elementV = weatherElement.getJSONObject(en).get("elementValue").toString();
                        if (elementV.equals("-99"))
                            elementV = "無資料";
                        int k;
                        for (k = 0; k < elementsName_EN.size(); k++) {
                            if (elementN.equals(elementsName_EN.get(k))) {
                                if (weather.get(elementsName_CH.get(k)) == null
                                        || weather.get(elementsName_CH.get(k)).equals("無資料"))
                                    weather.put(elementsName_CH.get(k), elementV);
                                break;
                            }
                        }
                        if (k == elementsName_EN.size()) {
                            if (weather.get(elementN) == null || weather.get(elementN).equals("無資料"))
                                weather.put(elementN, elementV);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new JSONException("\nThe JSON file from prespect weather's url has error");
        }
        return weather;
    }

    /**
     * Produce data from given JSONObject.
     * <p>
     * 搜集並整理「預報」天氣資料<br>
     * 自動存到參數predict_weather.
     * 
     * @param json a json file containing weather information.
     */
    public void producePredictWeather(JSONObject json) {
        List<String> conditons = Arrays.asList("天氣現象", "降雨機率", "最低溫度", "舒適度", "最高溫度");
        ArrayList<Map<String, ArrayList<String>>> weatherList = new ArrayList<Map<String, ArrayList<String>>>();
        try {
            JSONArray allLocation = json.getJSONObject("records").getJSONArray("location");
            // JSONObject jsonWeather= allLocation.getJSONObject(0);
            JSONArray elements = allLocation.getJSONObject(0).getJSONArray("weatherElement");
            JSONArray timeArray = elements.getJSONObject(0).getJSONArray("time");
            for (int k = 0; k < timeArray.length(); k++) {
                Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
                JSONObject sameTimes = timeArray.getJSONObject(k);
                ArrayList<String> sttlist = new ArrayList<String>();
                ArrayList<String> endtlist = new ArrayList<String>();
                sttlist.add((String) sameTimes.get("startTime"));
                endtlist.add((String) sameTimes.get("endTime"));
                map.put("startTime", sttlist);
                map.put("endTime", endtlist);

                for (int i = 0; i < elements.length(); i++) {
                    JSONObject times = elements.getJSONObject(i).getJSONArray("time").getJSONObject(k);
                    JSONObject parameter = times.getJSONObject("parameter");
                    ArrayList<String> plist = new ArrayList<String>();
                    plist.add((String) parameter.get("parameterName"));
                    switch (i) {
                        case 0:
                            plist.add((String) parameter.get("parameterValue"));
                            break;
                        case 1:
                            plist.add((String) parameter.get("parameterUnit"));
                            break;
                        case 2:
                            plist.add((String) parameter.get("parameterUnit"));
                            break;
                        case 4:
                            plist.add((String) parameter.get("parameterUnit"));
                            break;
                        default:
                            break;
                    }
                    map.put(conditons.get(i), plist);
                }
                weatherList.add(map);
            }
        } catch (JSONException e) {
            // throw new JSONException("\nThe JSON file from predict weather's url has
            // error");
        } finally {
            predict_weather = weatherList;
        }
    }

    /**
     * Build data.
     * <p>
     * build predict_weather, current_weather and immediate_weather<br>
     * Use class weatherAPI get each url.<br>
     * Catch nessesary data form url.
     * 
     * @throws Exception If any exception occurs.
     */
    public void weatherInit() throws Exception {
        WeatherAPI weatherAPI = new WeatherAPI(location);

        String datafromHttp = getHttp(weatherAPI.getPredictDataUrl());
        JSONObject predict_json = new JSONObject(datafromHttp);
        producePredictWeather(predict_json);

        String dataCurrentHttp = getHttp(weatherAPI.getCurrentDataUrl());
        JSONObject Jsonfile_C = new JSONObject(dataCurrentHttp);
        current_weather = producePerspectiveWeather(Jsonfile_C);

        String dataImmediateHttp = getHttp(weatherAPI.getImmediateDataUrl());
        JSONObject Jsonfile_N = new JSONObject(dataImmediateHttp);
        immediate_weather = producePerspectiveWeather(Jsonfile_N);
    }

    private String render() {
        StringBuilder output = new StringBuilder();
        List<PredictInfoParser> predictInfos = new ArrayList<PredictInfoParser>();
        for (Map<String, ArrayList<String>> weatherInfo : predict_weather) {
            predictInfos.add(new PredictInfoParser(weatherInfo));
        }

        // Width of per time: 12
        for (PredictInfoParser info : predictInfos) {
            output.append(String.format("    %s ~ %s   ", info.BEGIN, info.END));
            output.append("|");
        }
        output.append("\n");

        // Width of WeatherImage: 13
        // Width of TempImage: 13
        for (int i = 0; i < 5; i++) {
            for (PredictInfoParser info : predictInfos) {
                output.append(info.TEMP_IMAGE.get(i));
                output.append(info.WEATHER_IMAGE.get(i));
                output.append("|");
            }
            output.append("\n");
        }

        for (PredictInfoParser info : predictInfos) {
            // 9 is temp image layout space: 13 - " (_)".length()
            String sizeFormat = "%" + (13 + 9 - info.DESC.length()) + "s";// only support chinese character
            output.append(info.TEMP_IMAGE.get(5));
            output.append(String.format(sizeFormat, info.DESC));
            output.append("|");
        }
        output.append("\n");

        for (PredictInfoParser info : predictInfos) {
            output.append("FEEL: ");
            // 26 is temp + weather image layout length
            // 11 is rainy + "FEEL: " layout length
            String sizeFormat = "%-" + (26 - 11 - info.FEEL.length()) + "s";// only support chinese character
            output.append(String.format(sizeFormat, info.FEEL));
            output.append("🌧️ :" + ANSIColor.BLUE + String.format("%2s", info.CHANCE_OF_RAIN) + ANSIColor.RESET);
            output.append("|");
        }
        output.append("\n");

        return output.toString() + '\n' + current_weather.toString() + '\n' + immediate_weather.toString();
    }

    /**
     * Initiallize weather data and show it out as image.
     * <p>
     * Use the method weatherInit() and then use method render() tranfer data into
     * image.
     * 
     */
    @Override
    public void run() {
        ifOutput = true;
        try {
            weatherInit();
        } catch (Exception e) {
            result = "weather handler fail:\n" + e;
        }
        result = render();
    }
}