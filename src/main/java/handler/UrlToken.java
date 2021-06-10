
package handler;

import java.util.Date;
import java.util.Formatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.*;

public enum UrlToken {
    宜蘭縣("&locationName=%E5%AE%9C%E8%98%AD%E7%B8%A3"),
    花蓮縣("&locationName=%E8%8A%B1%E8%93%AE%E7%B8%A3"),
    臺東縣("&locationName=%E8%87%BA%E6%9D%B1%E7%B8%A3"),
    澎湖縣("&locationName=%E9%87%91%E9%96%80%E7%B8%A3"),
    金門縣("&locationName=%E9%87%91%E9%96%80%E7%B8%A3"),
    連江縣("&locationName=%E9%80%A3%E6%B1%9F%E7%B8%A3"),
    臺北市("&locationName=%E8%87%BA%E5%8C%97%E5%B8%82"),
    新北市("&locationName=%E6%96%B0%E5%8C%97%E5%B8%82"),
    桃園市("&locationName=%E6%A1%83%E5%9C%92%E5%B8%82"),
    臺中市("&locationName=%E8%87%BA%E4%B8%AD%E5%B8%82"),
    臺南市("&locationName=%E8%87%BA%E5%8D%97%E5%B8%82"),
    高雄市("&locationName=%E9%AB%98%E9%9B%84%E5%B8%82"),
    基隆市("&locationName=%E5%9F%BA%E9%9A%86%E5%B8%82"),
    新竹縣("&locationName=%E6%96%B0%E7%AB%B9%E7%B8%A3"),
    新竹市("&locationName=%E6%96%B0%E7%AB%B9%E5%B8%82"),
    苗栗縣("&locationName=%E8%8B%97%E6%A0%97%E7%B8%A3"),
    彰化縣("&locationName=%E5%BD%B0%E5%8C%96%E7%B8%A3"),
    南投縣("&locationName=%E5%8D%97%E6%8A%95%E7%B8%A3"),
    雲林縣("&locationName=%E9%9B%B2%E6%9E%97%E7%B8%A3"),
    嘉義縣("&locationName=%E5%98%89%E7%BE%A9%E7%B8%A3"),
    嘉義市("&locationName=%E5%98%89%E7%BE%A9%E5%B8%82"),
    屏東縣("&locationName=%E5%B1%8F%E6%9D%B1%E7%B8%A3");
    private final String location;
    private final String apiUrl= "https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-E7B5B0DB-C3E7-439E-B21D-56D3B7316D36&timeFrom=2021-06-09T23%3A59%3A59";
    UrlToken(String location){
        this.location = location;
    }
    UrlToken(){
        location= "基隆市";
    }
    public String getTomorrow(Date today){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(calendar.DATE,+1);//把日期往前減少一天，若想把日期向後推一天則將負數改為正數
        today=calendar.getTime(); 
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(today)+"T00%3A00%3A00";
    }
    public String toString(){
        UrlToken.location.toString();
        Date date=new Date();//取時間
        String tomorrowToken= getTomorrow(date);
        
    }
    
}
