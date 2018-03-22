package app.com.allinonenews.util;

import com.google.common.base.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mukesh on 6/4/17.
 */

public class AppUtil {
    public static String getTimeInLocal(String time){
        //"2017-04-06T01:30:09Z"
        if (Strings.isNullOrEmpty(time))
            return time;
        try {
            DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = utcFormat.parse(time);

            DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd' , 'HH:mm:ss");
            pstFormat.setTimeZone(TimeZone.getDefault());
            return pstFormat.format(date);

            //System.out.println(pstFormat.format(date));

        } catch (ParseException e) {
            e.printStackTrace();
            return time;
        }

    }
}
