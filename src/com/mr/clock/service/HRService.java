package com.mr.clock.service;

import java.awt.image.BufferedImage;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.mr.clock.dao.DAO;
import com.mr.clock.dao.DAOFactory;
import com.mr.clock.pojo.Employee;
import com.mr.clock.pojo.User;
import com.mr.clock.pojo.WorkTime;
import com.mr.clock.session.Session;
import com.mr.clock.util.DateTimeUtil;

/**
 * ���·���
 * 
 * @author mingrisoft
 *
 */
public class HRService {
    private static final String CLOCK_IN = "I";// �����ϰ�򿨱��
    private static final String CLOCK_OUT = "O";// �����°�򿨱��
    private static final String LATE = "L";// �ٵ����
    private static final String LEFT_EARLY = "E";// ���˱��
    private static final String ABSENT = "A";// ȱϯ���

    private static DAO dao = DAOFactory.getDAO();// ���ݿ�ӿ�

    /**
     * ��������Ա��
     */
    public static void loadAllEmp() {
        Session.EMP_SET.clear();// ȫ�ֻỰ�������Ա��
        // ���´����ݿ��м�������Ա���Ķ��󼯺�
        Session.EMP_SET.addAll(dao.getALLEmp());
    }

    /**
     * ����Ա�û���¼
     * 
     * @param username �û���
     * @param password ����
     * @return ��¼�Ƿ�ɹ�
     */
    public static boolean userLogin(String username, String password) {
        User user = new User(username, password);// ����Ա����
        if (dao.userLogin(user)) {// ������ݿ��п��Բ鵽��ع���Ա�û���������
            Session.user = user;// ����¼�Ĺ���Ա��Ϊȫ�ֻỰ�еĹ���Ա
            return true;// ��¼�ɹ�
        } else {
            return false;// ��¼ʧ��
        }
    }

    /**
     * �����Ա��
     * 
     * @param name Ա������
     * @param face Ա��������Ƭ
     * @return ��ӳɹ���Ա������
     */
    public static Employee addEmp(String name, BufferedImage face) {
        // ͨ��UUID������ɸ�Ա����������
        String code = UUID.randomUUID().toString().replace("-", "");
        Employee e = new Employee(null, name, code);// ������Ա������
        dao.addEmp(e);// �����ݿ�����Ա������
        e = dao.getEmp(code);// ���»�ȡ�ѷ���Ա����ŵ�Ա������
        Session.EMP_SET.add(e);// ��Ա�����뵽ȫ��Ա���б���
        return e;// ������Ա������
    }

    /**
     * ɾ��Ա��
     * 
     * @param id Ա�����
     */
    public static void deleteEmp(int id) {
        Employee e = getEmp(id);// ���ݱ�Ż�ȡ��Ա������
        if (e != null) {// ������ڸ�Ա��
            Session.EMP_SET.remove(e);// ��Ա���б���ɾ��
        }
        dao.deleteEmp(id);// �����ݿ���ɾ����Ա����Ϣ
        dao.deleteClockInRecord(id);// �����ݿ���ɾ����Ա�����д򿨼�¼
        ImageService.deleteFaceImage(e.getCode());// ɾ����Ա��������Ƭ�ļ�
        Session.FACE_FEATURE_MAP.remove(e.getCode());// ɾ����Ա����������
        Session.RECORD_MAP.remove(e.getId());// ɾ����Ա���򿨼�¼
    }

    /**
     * ��ȡԱ������
     * 
     * @param id Ա�����
     * @return
     */
    public static Employee getEmp(int id) {
        for (Employee e : Session.EMP_SET) {// ��������Ա��
            if (e.getId().equals(id)) {// ��������һ����
                return e;// ���ظ�Ա��
            }
        }
        return null;// û�ҵ�����null
    }

    /**
     * ��ȡԱ������
     * 
     * @param code Ա��������
     * @return
     */
    public static Employee getEmp(String code) {
        for (Employee e : Session.EMP_SET) {// ��������Ա��
            if (e.getCode().equals(code)) {// �����������һ����
                return e;// ���ظ�Ա��
            }
        }
        return null;// û�ҵ�����null
    }

    /**
     * ��Ӵ򿨼�¼
     * 
     * @param e
     */
    public static void addClockInRecord(Employee e) {
        Date now = new Date();// ��ǰʱ��
        // Ϊ��Ա����ӵ�ǰʱ��Ĵ򿨼�¼
        dao.addCLockInRecord(e.getId(), now);
        // ���ȫ�ֻỰ��û�и�Ա���Ĵ򿨼�¼
        if (!Session.RECORD_MAP.containsKey(e.getId())) {
            // Ϊ��Ա����ӿռ�¼
            Session.RECORD_MAP.put(e.getId(), new HashSet<>());
        }
        // �ڸ�Ա���Ĵ򿨼�¼������µĴ�ʱ��
        Session.RECORD_MAP.get(e.getId()).add(now);
    }

    /**
     * ���������˵Ĵ򿨼�¼
     */
    public static void loadAllClockInRecord() {
        // ��ȡ�򿨼�¼����
        String record[][] = dao.getAllClockInRecord();
        if (record == null) {// ������ݿ��в����ڴ�����
            System.err.println("�����޴�����");
            return;
        }
        // �������д򿨼�¼
        for (int i = 0, length = record.length; i < length; i++) {
            String r[] = record[i];// ��ȡ��i�м�¼
            Integer id = Integer.valueOf(r[0]);// ��ȡԱ�����
            // ���ȫ�ֻỰ��û�и�Ա���Ĵ򿨼�¼
            if (!Session.RECORD_MAP.containsKey(id)) {
                // Ϊ��Ա����ӿռ�¼
                Session.RECORD_MAP.put(id, new HashSet<>());
            }
            try {
                // ����ʱ���ַ���תΪ���ڶ���
                Date recodeDate = DateTimeUtil.dateOf(r[1]);
                // �ڸ�Ա���Ĵ򿨼�¼������µĴ�ʱ��
                Session.RECORD_MAP.get(id).add(recodeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ������Ϣʱ��
     */
    public static void loadWorkTime() {
        // �����ݿ��л�ȡ��Ϣʱ�䣬����ֵ��ȫ�ֻػ�
        Session.worktime = dao.getWorkTime();
    }

    /**
     * ������Ϣʱ��
     * 
     * @param time �µ���Ϣʱ��
     */
    public static void updateWorkTime(WorkTime time) {
        dao.updateWorkTime(time);// �������ݿ��е���Ϣʱ��
        Session.worktime = time;// ����ȫ�ֻỰ�е���Ϣʱ��
    }

    /**
     * ��ȡĳһ������Ա���Ĵ����ݣ� ��ΪԱ������ֵΪԱ�����ڱ�ǡ��򿨼�¼��ƴ�������ı�־�������������
     * 
     * @param year  ��
     * @param month ��
     * @param day   ��
     * @return ��ΪԱ������ֵΪԱ�����ڱ��
     */
    private static Map<Employee, String> getOneDayRecordData(int year, int month, int day) {
        Map<Employee, String> record = new HashMap<>();// ��ΪԱ������ֵΪ���ڱ��
        // ʱ���
        Date zeroTime = null, noonTime = null, lastTime = null, workTime = null, closingTime = null;
        try {
            // ���
            zeroTime = DateTimeUtil.dateOf(year, month, day, "00:00:00");
            // ����12��
            noonTime = DateTimeUtil.dateOf(year, month, day, "12:00:00");
            // һ�������һ��
            lastTime = DateTimeUtil.dateOf(year, month, day, "23:59:59");
            WorkTime wt = Session.worktime;
            // �ϰ�ʱ��
            workTime = DateTimeUtil.dateOf(year, month, day, wt.getStart());
            // �°�ʱ��
            closingTime = DateTimeUtil.dateOf(year, month, day, wt.getEnd());
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        for (Employee e : Session.EMP_SET) {// ��������Ա��
            String report = "";// Ա���򿨼�¼����ʼΪ��
            // ����򿨼�¼�д��ڸ�Ա���ļ�¼
            if (Session.RECORD_MAP.containsKey(e.getId())) {
                boolean isAbsent = true;// Ĭ��Ϊȱϯ״̬
                // ��ȡ��Ա�������д򿨼�¼
                Set<Date> lockinSet = Session.RECORD_MAP.get(e.getId());
                for (Date r : lockinSet) {// �������д򿨼�¼
                    // ���Ա���ڴ��������д򿨼�¼
                    if (r.after(zeroTime) && r.before(lastTime)) {
                        isAbsent = false;// ��ȱϯ
                        // �ϰ�ǰ��
                        if (r.before(workTime) || r.equals(workTime)) {
                            report += CLOCK_IN;// ׷���ϰ������򿨱��
                        }
                        // �°���
                        if (r.after(closingTime) || r.equals(closingTime)) {
                            report += CLOCK_OUT;// ׷���°������򿨱��
                        }
                        // �ϰ������ǰ��
                        if (r.after(workTime) && r.before(noonTime)) {
                            report += LATE;// ׷�ӳٵ����
                        }
                        // ������°�ǰ��
                        if (r.after(noonTime) && r.before(closingTime)) {
                            report += LEFT_EARLY;// ׷�����˱��
                        }
                    }
                }
                if (isAbsent) {// �����ڴ�����û�д򿨼�¼
                    report = ABSENT;// ָ��Ϊȱϯ���
                }
            } else {// ����򿨼�¼��û�д��˼�¼
                report = ABSENT;// ָ��Ϊȱϯ���
            }
            record.put(e, report);// �����Ա���Ĵ򿨼�¼
        }
        return record;
    }

    /**
     * ��ȡ�ձ�����
     * 
     * @param year  ��
     * @param month ��
     * @param day   ��
     * @return �������ձ��ַ���
     */
    public static String getDayReport(int year, int month, int day) {
        Set<String> lateSet = new HashSet<>();// �ٵ�����
        Set<String> leftSet = new HashSet<>();// ��������
        Set<String> absentSet = new HashSet<>();// ȱϯ����
        // ��ȡ��һ�������˵Ĵ�����
        Map<Employee, String> record = HRService.getOneDayRecordData(year, month, day);
        for (Employee e : record.keySet()) {// ����ÿһ��Ա��
            String oneRecord = record.get(e);// ��ȡ��Ա���Ŀ��ڱ��
            // ����гٵ���ǣ�����û�������ϰ�򿨱��
            if (oneRecord.contains(LATE) && !oneRecord.contains(CLOCK_IN)) {
                lateSet.add(e.getName());// ��ӵ��ٵ�����
            }
            // ��������˱�ǣ�����û�������°�򿨱��
            if (oneRecord.contains(LEFT_EARLY) && !oneRecord.contains(CLOCK_OUT)) {
                leftSet.add(e.getName());// ��ӵ���������
            }
            // �����ȱϯ���
            if (oneRecord.contains(ABSENT)) {
                absentSet.add(e.getName());// ��ӵ�ȱϯ����
            }
        }

        StringBuilder report = new StringBuilder();// �����ַ���
        int count = Session.EMP_SET.size();// ��ȡԱ������
        // ƴ�ӱ�������
        report.append("-----  " + year + "��" + month + "��" + day + "��  -----\n");
        report.append("Ӧ��������" + count + "\n");

        report.append("ȱϯ������" + absentSet.size() + "\n");
        report.append("ȱϯ������");
        if (absentSet.isEmpty()) {// ���ȱϯ�����ǿյ�
            report.append("���գ�\n");
        } else {
            // ����ȱϯ�����ı�������
            Iterator<String> it = absentSet.iterator();
            while (it.hasNext()) {// ��������
                // �ڱ��������ȱϯԱ��������
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        report.append("�ٵ�������" + lateSet.size() + "\n");
        report.append("�ٵ�������");
        if (lateSet.isEmpty()) {// ����ٵ������ǿյ�
            report.append("���գ�\n");
        } else {
            // �����ٵ������ı�������
            Iterator<String> it = lateSet.iterator();
            while (it.hasNext()) {// ��������
                // �ڱ�������ӳٵ�Ա��������
                report.append(it.next() + " ");
            }
            report.append("\n");
        }

        report.append("����������" + leftSet.size() + "\n");
        report.append("����������");
        if (leftSet.isEmpty()) {// ������������ǿյ�
            report.append("���գ�\n");
        } else {
            // �������������ı�������
            Iterator<String> it = leftSet.iterator();
            while (it.hasNext()) {// ��������
                // �ڱ������������Ա��������
                report.append(it.next() + " ");
            }
            report.append("\n");
        }
        return report.toString();
    }

    /**
     * ��ȡ�±����ݡ���ά�����һ��ΪԱ�����ƣ��ڶ���ֵ���һ��Ϊyear��month��1�������һ�յĴ����
     * 
     * @param year  ��
     * @param month ��
     * @return
     */
    public static String[][] getMonthReport(int year, int month) {
        int lastDay = DateTimeUtil.getLastDay(year, month);// �����������
        int count = Session.EMP_SET.size();// ������
        // �������ݼ�ֵ�ԣ���ΪԱ������ֵΪ��Ա���ӵ�һ���������һ��Ŀ��ڱ���б�
        Map<Employee, ArrayList<String>> reportCollectioin = new HashMap<>();
        for (int day = 1; day <= lastDay; day++) {// �ӵ�һ����������һ��
            // ��ȡ��һ�������˵Ĵ�����
            Map<Employee, String> recordOneDay = HRService.getOneDayRecordData(year, month, day);
            for (Employee e : recordOneDay.keySet()) {// ����ÿһ����
                // ���������û�д�Ա���ļ�¼
                if (!reportCollectioin.containsKey(e)) {
                    // Ϊ��Ա����ӿ��б��б���Ϊ�������
                    reportCollectioin.put(e, new ArrayList<>(lastDay));
                }
                // ���Ա���Ĵ򿨼�¼�б��������һ��Ŀ��ڱ��
                reportCollectioin.get(e).add(recordOneDay.get(e));
            }
        }

        // �����������飬����ΪԱ������������Ϊ������� + 1�������У�
        String report[][] = new String[count][lastDay + 1];
        int row = 0;// ���������ӵ�һ�п�ʼ����
        // �����������ݼ�ֵ���е�ÿһ��Ա��
        for (Employee e : reportCollectioin.keySet()) {
            report[row][0] = e.getName();// ��һ��ΪԱ����
            // ��ȡ��Ա�����ڱ���б�
            ArrayList<String> list = reportCollectioin.get(e);
            // ����ÿһ�����ڱ��
            for (int i = 0, length = list.size(); i < length; i++) {
                report[row][i + 1] = "";// �ӵڶ��п�ʼ��Ĭ��ֵΪ���ַ���
                String record = list.get(i);// ��ȡ���еĿ��ڱ��
                if (record.contains(ABSENT)) {// �������ȱϯ���
                    report[row][i + 1] = "��ȱϯ��";// ���б��Ϊȱϯ
                }
                // ������������ϰ�򿨼�¼�������°�򿨼�¼
                else if (record.contains(CLOCK_IN) && record.contains(CLOCK_OUT)) {// �����ȫ��
                    report[row][i + 1] = "";// ���б��Ϊ���ַ���
                } else {
                    // ����гٵ���¼�������������ϰ�򿨼�¼
                    if (record.contains(LATE) && !record.contains(CLOCK_IN)) {
                        report[row][i + 1] += "���ٵ���";// ���б��Ϊ�ٵ�
                    }
                    // ��������˼�¼���������°�򿨼�¼
                    if (record.contains(LEFT_EARLY) && !record.contains(CLOCK_OUT)) {
                        report[row][i + 1] += "�����ˡ�";// ���б��Ϊ����
                    }

                    // ����޳ٵ���¼���������ϰ�򿨼�¼
                    if (!record.contains(LATE) && !record.contains(CLOCK_IN)) {
                        report[row][i + 1] += "���ϰ�δ�򿨡�";// ���б��Ϊ�ϰ�δ��
                    }
                    // ��������˼�¼���������°�򿨼�¼
                    if (!record.contains(LEFT_EARLY) && !record.contains(CLOCK_OUT)) {
                        report[row][i + 1] += "���°�δ�򿨡�";// ���б��Ϊ�°�δ��
                    }
                }
            }
            row++;// ����������
        }
        return report;
    }
}
