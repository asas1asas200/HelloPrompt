package handler;


public class Location {
    private static final String KLU     = "&locationName=%E5%9F%BA%E9%9A%86%E5%B8%82";  // 基隆市
    private static final String TPH     = "&locationName=%E6%96%B0%E5%8C%97%E5%B8%82";  // 新北市
    private static final String TPE     = "&locationName=%E8%87%BA%E5%8C%97%E5%B8%82";  // 臺北市
    private static final String TYC     = "&locationName=%E6%A1%83%E5%9C%92%E5%B8%82";  // 桃園市
    private static final String HSH     = "&locationName=%E6%96%B0%E7%AB%B9%E7%B8%A3";  // 新竹縣
    private static final String HSC     = "&locationName=%E6%96%B0%E7%AB%B9%E5%B8%82";  // 新竹市
    private static final String MAL     = "&locationName=%E8%8B%97%E6%A0%97%E7%B8%A3";  // 苗栗縣
    private static final String TXG     = "&locationName=%E8%87%BA%E4%B8%AD%E5%B8%82";  // 臺中市
    private static final String CWH     = "&locationName=%E5%BD%B0%E5%8C%96%E7%B8%A3";  // 彰化縣
    private static final String NTO     = "&locationName=%E5%8D%97%E6%8A%95%E7%B8%A3";  // 南投縣
    private static final String YLH     = "&locationName=%E9%9B%B2%E6%9E%97%E7%B8%A3";  // 雲林縣
    private static final String CHY     = "&locationName=%E5%98%89%E7%BE%A9%E7%B8%A3";  // 嘉義縣
    private static final String CYI     = "&locationName=%E5%98%89%E7%BE%A9%E5%B8%82";  // 嘉義市
    private static final String TNN     = "&locationName=%E8%87%BA%E5%8D%97%E5%B8%82";  // 臺南市
    private static final String KHH     = "&locationName=%E9%AB%98%E9%9B%84%E5%B8%82";  // 高雄市
    private static final String IUH     = "&locationName=%E5%B1%8F%E6%9D%B1%E7%B8%A3";  // 屏東縣
    private static final String HWA     = "&locationName=%E8%8A%B1%E8%93%AE%E7%B8%A3";  // 花蓮縣
    private static final String TTT     = "&locationName=%E8%87%BA%E6%9D%B1%E7%B8%A3";  // 臺東縣
    private static final String PEH     = "&locationName=%E9%87%91%E9%96%80%E7%B8%A3";  // 澎湖縣
    private static final String KMN     = "&locationName=%E9%87%91%E9%96%80%E7%B8%A3";  // 金門縣
    private static final String LNN     = "&locationName=%E9%80%A3%E6%B1%9F%E7%B8%A3";  // 連江縣
    private static final String ILN     = "&locationName=%E5%B1%8F%E6%9D%B1%E7%B8%A3";  // 宜蘭縣

    private static String locationUrl;

    public String getLocationUrl(String location){
        switch(location){
            case "基隆市":
            this.locationUrl= KLU;
            break;
            case "新北市":
            this.locationUrl= TPH;
            break;
            case "臺北市":
            this.locationUrl= TPE;
            break;
            case "桃園市":
            this.locationUrl= TYC;
            break;
            case "新竹縣":
            this.locationUrl= HSH;
            break;
            case "新竹市":
            this.locationUrl= HSC;
            break;
            case "苗栗縣":
            this.locationUrl= MAL;
            break;
            case "臺中市":
            this.locationUrl= TXG;
            break;
            case "彰化縣":
            this.locationUrl= CWH;
            break;
            case "南投縣":
            this.locationUrl= NTO;
            break;
            case "雲林縣":
            this.locationUrl= YLH;
            break;
            case "嘉義縣":
            this.locationUrl= CHY;
            break;
            case "嘉義市":
            this.locationUrl= CYI;
            break;
            case "臺南市":
            this.locationUrl= TNN;
            break;
            case "高雄市":
            this.locationUrl= KHH;
            break;
            case "屏東縣":
            this.locationUrl= IUH;
            break;
            case "花蓮縣":
            this.locationUrl= HWA;
            break;
            case "臺東縣":
            this.locationUrl= TTT;
            break;
            case "澎湖縣":
            this.locationUrl= PEH;
            break;
            case "金門縣":
            this.locationUrl= KMN;
            break;
            case "連江縣":
            this.locationUrl= LNN;
            break;
            case "宜蘭縣":
            this.locationUrl= ILN;
            break;
            default:
            this.locationUrl= "";
            break;
        }
        return locationUrl;
    }
}
