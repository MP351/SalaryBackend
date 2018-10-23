package Database;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EnrollPeriod {
    private Calendar calendar;

    public EnrollPeriod(String period)  {
        int month;
        int year;
        try {
            checkFormat(period);
            month = Integer.parseInt(period.split("-")[1]);
            year = Integer.parseInt(period.split("-")[0]);
            calendar = new GregorianCalendar(year, month, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int compareTo(EnrollPeriod prd) {
        if (this.getYear() > prd.getYear())
            return 1;
        else if (this.getYear() < prd.getYear())
            return -1;
        else
            if (this.getMonth() > prd.getMonth())
                return 1;
            else if (this.getMonth() < prd.getMonth())
                return -1;
            else return 0;
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    @Override
    public String toString() {
        return calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH);
    }

    private void checkFormat(String period) throws Exception {
        Pattern pattern = Pattern.compile("^\\d{4}-((0[1-9])|(0[0-1])|(1[0-1])|([0-9]))$");
        Matcher m = pattern.matcher(period);
        if (!m.matches())
            throw new Exception("Wrong period format " + period +
                    ". \n Should be YYYY-MM");

    }

    public void add(int monthsCount) {
        calendar.add(Calendar.MONTH, monthsCount);
    }
}
