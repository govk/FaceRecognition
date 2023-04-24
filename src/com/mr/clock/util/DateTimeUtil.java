package com.mr.clock.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ����ʱ�乤����
 * 
 * @author mingrisoft
 *
 */

public class DateTimeUtil {

    /**
     * ��ȡ��ǰʱ��
     * 
     * @return
     */
    public static String timeNow() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    /**
     * ��ȡ��ǰ����
     * 
     * @return
     */
    public static String dateNow() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    /**
     * ��ȡ��ǰ���ں�ʱ��
     * 
     * @return
     */
    public static String dateTimeNow() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * ��ȡ�ɵ�ǰ�ꡢ�¡��ա�ʱ���֡�����������ɵ�����
     * 
     * @return
     */
    public static Integer[] now() {
        // �����ꡢ�¡��ա�ʱ���֡��������
        Integer now[] = new Integer[6];
        Calendar c = Calendar.getInstance();// ��������
        now[0] = c.get(Calendar.YEAR);// ��
        now[1] = c.get(Calendar.MONTH) + 1;// ��
        now[2] = c.get(Calendar.DAY_OF_MONTH);// ��
        now[3] = c.get(Calendar.HOUR_OF_DAY);// ʱ
        now[4] = c.get(Calendar.MINUTE);// ��
        now[5] = c.get(Calendar.SECOND);// ��
        return now;
    }

    /**
     * ��ȡָ���·ݵ�������
     * 
     * @param year  ��
     * @param month ��
     * @return ����
     */
    public static int getLastDay(int year, int month) {
        Calendar c = Calendar.getInstance();// ��������
        c.set(Calendar.YEAR, year);// ָ����
        c.set(Calendar.MONTH, month - 1);// ָ����
        // �������µ����һ��
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * ��yyyy-MM-dd HH:mm:ss��ʽ���ַ���ת��ΪDate����
     * 
     * @param datetime ���ϸ�ʽ���ַ���
     * @return
     * @throws ParseException ������ַ������ݲ���תΪ�������ڣ����׳����쳣
     */
    public static Date dateOf(String datetime) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
    }

    /**
     * ����ָ������ʱ���Date����
     * 
     * @param year  ��
     * @param month ��
     * @param day   ��
     * @param time  HH:mm:ss��ʽ��ʱ���ַ���
     * @return
     * @throws ParseException
     */
    public static Date dateOf(int year, int month, int day, String time) throws ParseException {
        String datetime = String.format("%4d-%02d-%02d %s", year, month, day, time);
        return dateOf(datetime);
    }

    /**
     * ���ʱ���ַ����Ƿ����HH:mm:ss��ʽ
     * 
     * @param time ʱ���ַ���
     * @return
     */
    public static boolean checkTimeStr(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            sdf.parse(time);// ��ʱ���ַ���תΪDate����
            return true;
        } catch (ParseException e) {
            return false;// �����쳣���ʾ�ַ�����ʽ����
        }
    }
}
