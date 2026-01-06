package com.hypercube.evisa.common.api.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TestCalendar {

	public static void main(String[] args) {

	        
	  
		  Calendar calendar = GregorianCalendar.getInstance();
	        calendar.setTime(new Date());
	       
	        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

	        for (int i = 0; i < 7; i++) {
	            calendar.add(Calendar.DATE, -1); // décrémente la date d'un jour à chaque tour de boucle
	            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	            if (dayOfWeek == Calendar.WEDNESDAY) {
	                System.out.println(calendar.getTime());
	            }
	        }
	     



//
//
//	        System.out.println(calendar.DAY_OF_WEEK);
//	        
//            calendar.set(Calendar.DAY_OF_WEEK, calendar.getLeastMaximum(Calendar.DAY_OF_WEEK));
//            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);
//           
//	        System.out.println(calendar.getTime());
//	       
////	int dayOfYear = calendar.get(Calendar.YEAR);
////	        System.out.println("Today is day " + dayOfYear + " of the year.");
////	    }
//	
   }	
}


