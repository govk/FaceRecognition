package com.mr.clock.dao;

import java.util.Date;
import java.util.Set;

import com.mr.clock.pojo.Employee;
import com.mr.clock.pojo.User;
import com.mr.clock.pojo.WorkTime;

/**
 * ���ݿ���ʽӿ�
 * 
 * @author mingrisoft
 *
 */
public interface DAO {
    /**
     * ��ȡ����Ա��
     * 
     * @return ����Ա�����󼯺�
     */
    public Set<Employee> getALLEmp();

    /**
     * ����Ա����Ż�ȡԱ������
     * 
     * @param id Ա�����
     * @return ����Ա������
     */
    public Employee getEmp(int id);

    /**
     * �����������ȡԱ������
     * 
     * @param code ������
     * @return ����Ա������
     */
    public Employee getEmp(String code);

    /**
     * �����Ա��
     * 
     * @param e ��Ա������
     */
    public void addEmp(Employee e);

    /**
     * ɾ��ָ��Ա��
     * 
     * @param id Ա�����
     */
    public void deleteEmp(Integer id);

    /**
     * ��ȡ��Ϣʱ��
     * 
     * @return ��Ϣʱ�����
     */
    public WorkTime getWorkTime();

    /**
     * ������Ϣʱ��
     * 
     * @param time
     */
    public void updateWorkTime(WorkTime time);

    /**
     * ָ��Ա����Ӵ򿨼�¼
     * 
     * @param empID Ա�����
     * @param now   ������
     */
    public void addCLockInRecord(int empID, Date now);

    /**
     * ɾ��ָ��Ա�����д򿨼�¼
     * 
     * @param empID Ա�����
     */
    public void deleteClockInRecord(int empID);

    /**
     * ��ȡ����Ա���Ĵ򿨼�¼
     * 
     * @return ��������¼Ա����ţ���������¼������
     */
    public String[][] getAllClockInRecord();

    /**
     * ��֤����Ա��¼
     * 
     * @param user ����Ա�˺�
     * @return ����˺�������ȷ���򷵻�true�����򷵻�false
     */
    public boolean userLogin(User user);
}
