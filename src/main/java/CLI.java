package ntou.cs.java2021.helloprompt;

import java.util.ArrayList;
import java.util.List;

public class CLI {
	private ArrayList<Handler> handlers;
	public CLI(){
		handlers = new ArrayList<Handler>();
		handlers.add(new CalendarHandler());
		handlers.add(new WeatherHandler("新竹市"));
	}
	public void run(){
		for(Handler handler: handlers){
			if(handler.ifOutput){
				System.out.println(handler.toString());
			}
		}
	}
}