package com.palprotech.heylaapp.helper;

/**
 * Created by Narendar on 07/11/17.
 */

public class HeylaAppHelper {

    public static String getTime(String dateTime){
        if((dateTime != null) && !dateTime.isEmpty() && dateTime.length()>=16){
            return dateTime.substring(11,16);

        }

        return null;

    }

    public static String getDate(String dateTime){
        String dateVal = null;
        try {
            if ((dateTime != null) && !dateTime.isEmpty() && dateTime.length() >= 10) {
                dateVal = dateTime.substring(0, 10);
                String month = getMonthName(Integer.parseInt(dateVal.substring(0, 2)));
                String day = dateVal.substring(3, 5);
                String year = dateVal.substring(6,10);
                if( (month != null) && (day != null) && (year != null)){
                    dateVal = month+"  "+ day+ ","+ year+" ";
                }

            }
        }catch (NumberFormatException numE){
            numE.printStackTrace();

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return dateVal;
    }

    private static  String getMonthName(int month){
        String monthVal = null;
        switch (month){
            case 1:
                monthVal = "Jan";
                break;
            case 2:
                monthVal = "Feb";
                break;
            case 3:
                monthVal = "Mar";
                break;
            case 4:
                monthVal = "Apr";
                break;
            case 5:
                monthVal = "May";
                break;
            case 6:
                monthVal = "Jun";
                break;
            case 7:
                monthVal = "Jul";
                break;
            case 8:
                monthVal = "Aug";
                break;
            case 9:
                monthVal = "Sep";
                break;
            case 10:
                monthVal = "Oct";
                break;
            case 11:
                monthVal = "Nov";
                break;
            case 12:
                monthVal = "Dec";
                break;
        }

        return  monthVal;
    }

}
