package com.hypercube.evisa.common.api.util;

import java.io.ByteArrayOutputStream;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;

import org.apache.commons.io.FileUtils;

import com.hypercube.evisa.common.api.constants.CommonsConstants;
import com.hypercube.evisa.common.api.customsrepoimpl.ApplicantTravelHistoryCustomsRepoImpl;
import com.hypercube.evisa.common.api.model.GenericSearchDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * @author sivasreenivas
 *
 */
@Slf4j
public final class CommonsUtil {

    /**
     * 
     */
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    /**
     * 
     */
    private static final String DATE_PARAM_VALUE_FORMAT = "ddMMyyyyHHmmss";
    /**
     * 
     */
    private static final String SHORT_DATE_PARAM_VALUE_FORMAT = "ddMMyyyy";
    /**
     * 
     */
    private static final DateFormat DATE_PARAM_VALUE_FORMATTER = new SimpleDateFormat(DATE_PARAM_VALUE_FORMAT);
    /**
     * 
     */
    private static long currentSessionID = System.currentTimeMillis();

    /**
     * 
     */
    private CommonsUtil() {
        log.info("Default Constructor - CommonsUtil");
    }

    /**
     * @return
     */
    public static String generateAccessTokenString() {
        log.info("CommonUtil::generateAccessTokenString");
        return getRandomeString(UUID.randomUUID());
    }

    /**
     * @return
     */
    public static synchronized String getNextSessionID() {
        currentSessionID++;
        String sessionID = ""; // max length 20
        String instanceID = System.getProperty("java.mhb.instance.name");
        if (instanceID == null || instanceID.length() == 0)
            instanceID = "A01";
        sessionID = instanceID + generateFixDigitSeqNo(currentSessionID, CommonsConstants.SEVEN);
        return sessionID;
    }

    /**
     * @param originalUniqueSeqNo
     * @param fixDigit
     * @return
     */
    private static String generateFixDigitSeqNo(long originalUniqueSeqNo, int fixDigit) {
        long modulusSeqNo = originalUniqueSeqNo % ((long) Math.pow(CommonsConstants.TEN, fixDigit) - 1);
        String newFixDigitSeqNo = String.valueOf(modulusSeqNo);
        for (int i = 0; i < fixDigit - newFixDigitSeqNo.length(); i++)
            newFixDigitSeqNo = "0" + newFixDigitSeqNo;
        return newFixDigitSeqNo;
    }

    /**
     * @param randomUUID
     * @return
     */
    private static String getRandomeString(UUID randomUUID) {
        return randomUUID.toString();
    }

    /**
     * @param count
     * @return
     */
    public static String randomAlphaNumeric(int count) {
        log.info("CommonsUtil::randomAlphaNumeric");
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            Random random = new Random();
            int character = random.nextInt(ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    /**
     * @param expiryTimeInMinutes
     * @return
     */
    public static Date calculateExpiryDate(int expiryTimeInMinutes, Calendar cal) {
        log.info("CommonsUtil::calculateExpiryDate");
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

    /**
     * @param date
     * @return
     */
    public static long getTime(Date date) {
        return date.getTime();
    }

    /**
     * @param originalDate
     * @return
     */
    public static Date cloneDate(Date originalDate) {
        if (originalDate == null) {
            return null;
        } else {
            return new Date(originalDate.getTime());
        }
    }

    /**
     * @param date
     * @return
     */
    public static String toDateString(Date date) {
        if (date == null)
            return null;
        return DATE_PARAM_VALUE_FORMATTER.format(new java.util.Date(date.getTime()));
    }

    /**
     * @param startDateTmp
     * @return
     */
    public static Date toStringStartDate(String startDateTmp) {
        // Format - ddMMyyyy or ddMMyyyyHHmmss
        if (startDateTmp.length() <= SHORT_DATE_PARAM_VALUE_FORMAT.length())
            startDateTmp += "000001";
        try {
            return DATE_PARAM_VALUE_FORMATTER.parse(startDateTmp);
        } catch (ParseException e) {
            log.error("ParseException {} {}", e.getMessage(), e.getStackTrace());
        }
        return null;
    }

    /**
     * @param endDateTmp
     * @return
     */
    public static Date toStringEndDate(String endDateTmp) {
        // Format - ddMMyyyy or ddMMyyyyHHmmss
        if (endDateTmp.length() <= SHORT_DATE_PARAM_VALUE_FORMAT.length())
            endDateTmp = endDateTmp + "235959";
        try {
            return DATE_PARAM_VALUE_FORMATTER.parse(endDateTmp);
        } catch (ParseException e) {
            log.error("ParseException {} {}", e.getMessage(), e.getStackTrace());
        }
        return null;
    }

    /**
     * @param source
     * @param destination
     * @return
     */
    public static boolean copyFiles(String source, String destination) {
        log.info("CommonsUtil::copyFiles");
        try {
            FileUtils.copyFile(new File(source), new File(destination));
            return true;
        } catch (IOException e) {

            log.error("IOException::", e);
            return false;
        }
    }

    /**
     * @param filepath
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(String filePath) {
        log.info("<<<CommonsUtil::deleteFile>>>");
        try {
            FileUtils.touch(new File(filePath));
            File fileToDelete = FileUtils.getFile(filePath);
            boolean success = FileUtils.deleteQuietly(fileToDelete);
            return success;
        } catch (IOException e) {
            log.error("IOException::", e);
            return false;
        }

    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    public static <E> List<E> cloneList(List<E> objectList) {
        if (objectList == null) {
            return objectList;
        } else {
            return new ArrayList<>(objectList);
        }
    }

    /**
     * @return
     */
    public static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    /**
     * @param calendar
     * @return
     */
    public static Calendar setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * @param calendar
     * @return
     */
    public static Calendar setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }

    /**
     * @param reportsSearchDTO
     * @return
     */
    public static GenericSearchDTO getSearchDates(String period) {
        log.info("ApplicationHeaderCustomsRepoImpl-getSearchDates");

        GenericSearchDTO genericSearchDTO = new GenericSearchDTO();

        if ("T".equals(period)) {
            genericSearchDTO
                    .setStartDate(CommonsUtil.setTimeToBeginningOfDay(CommonsUtil.getCalendarForNow()).getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("W".equals(period)) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
            //calendar.add(Calendar.DATE, -7);
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getActualMinimum(Calendar.DAY_OF_WEEK));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            System.out.println("date week"+ calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        } else if ("M".equals(period)) {
            Calendar calendar = CommonsUtil.getCalendarForNow();

            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);
            
            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
            System.out.println("date Month"+ calendar.getTime());
        } else if ("Y".equals(period)) {
            Calendar calendar = CommonsUtil.getCalendarForNow();
            calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
            calendar = CommonsUtil.setTimeToBeginningOfDay(calendar);

            genericSearchDTO.setStartDate(calendar.getTime());
            genericSearchDTO.setEndDate(new Date());
        }

        return genericSearchDTO;
    }

    /**
     * @param data
     * @return
     * @throws IOException
     */
    /* compress the image bytes before storing it in the database */
    public static byte[] compressBytes(byte[] data) throws IOException {
        log.info("CommonsUtil-compressBytes");
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            log.error("compressBytes-IOException: {} {}", e.getMessage(), e.getCause());
        } finally {
            outputStream.close();
        }
        log.info("Compressed Image Byte Size - {}", outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    /**
     * @param data
     * @return
     * @throws IOException
     */
    /* uncompress the image bytes before returning it to the angular application */
    public static byte[] decompressBytes(byte[] data) throws IOException {
        log.info("CommonsUtil-decompressBytes");
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
            log.error("compressBytes-IOException: {} {}", ioe.getMessage(), ioe.getCause());
        } catch (DataFormatException e) {
            log.error("compressBytes-DataFormatException: {} {}", e.getMessage(), e.getCause());
        } finally {
            outputStream.close();
        }
        return outputStream.toByteArray();
    }

    /**
     * @param date
     * @param days
     * @return
     */
    public static Date addDays(Date date, int days) {
        log.info("CommonsUtil-addDays");
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    
    /**
     * @param date
     * @param month
     * @return
     */
    public static Date setMonthToJavaUtilDate(Date date, int month) {
        log.info("ApplicationTrackerServiceImpl-setMonthToJavaUtilDate");
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MONTH, month);
        return now.getTime();
    }

}
