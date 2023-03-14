import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FlightAnalyzer {
    public static Long getTimeDifference(String departure, String arrival) throws java.text.ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd.MM.yyHH:mm");

        Date date1 = dateFormat.parse(departure);
        Date date2 = dateFormat.parse(arrival);

        long diff = (int) ((date2.getTime() / 1000) / 60) - (int) ((date1.getTime() / 1000) / 60);
        return diff;
    }

    public static String buildDateString(Object date, Object time) {

        StringBuilder builder = new StringBuilder();
        builder.append(date);
        builder.append(time);

        String builtString = builder.toString();
        return builtString;
    }

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        ArrayList<Long> flightTimes = new ArrayList<Long>();
        try {
            Object obj = parser.parse(new FileReader("./src/tickets.json"));
            JSONObject jObject = (JSONObject) obj;
            JSONArray tickets = (JSONArray) jObject.get("tickets");
            for(Object ticketObj : tickets) {
                JSONObject ticket = (JSONObject) ticketObj;
                String departure = buildDateString(ticket.get("departure_date"), ticket.get("departure_time"));
                String arrival = buildDateString(ticket.get("arrival_date"), ticket.get("arrival_time"));
                flightTimes.add(getTimeDifference(departure, arrival));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        long sum = 0;
        for (Long flightTime : flightTimes) {
            sum += flightTime;
        }
        int averageFlightTime = (int) sum / flightTimes.size();
        System.out.println("average flight time: " + averageFlightTime + " minutes.");

        Collections.sort(flightTimes);
        int index = (int) Math.ceil(flightTimes.size() * 0.9);
        long percentile90 = flightTimes.get(index - 1);
        System.out.println("90th percentile flight time: " + percentile90 + " minutes.");
    }
}
