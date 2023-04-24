package com.mr.clock.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.mr.clock.pojo.Employee;
import com.mr.clock.pojo.User;
import com.mr.clock.pojo.WorkTime;
import com.mr.clock.util.JDBCUtil;

/**
 * ����MySQL��DAOʵ����
 * 
 * @author mingrisoft
 *
 */
public class DAOMysqlImpl implements DAO {
    Connection con = null;
    Statement stmt = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public Set<Employee> getALLEmp() {
        Set<Employee> set = new HashSet<Employee>();// ȫ��Ա������
        String sql = "select id, name,code from t_emp ";
        con = JDBCUtil.getConnection();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String code = rs.getString("code");
                Employee e = new Employee(id, name, code);
                set.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
        return set;
    }

    @Override
    public Employee getEmp(int id) {
        String sql = "select name,code from t_emp where id = ?";// ��ִ�е�SQL���
        con = JDBCUtil.getConnection();// �����ݿ�����
        try {
            ps = con.prepareStatement(sql);// ����ִ��SQL���Ľӿ�
            ps.setInt(1, id);// ��SQL����е�һ��?��ΪԱ����ŵ�ֵ
            rs = ps.executeQuery();// ִ��SQL���
            if (rs.next()) {// ����в�ѯ���
                String name = rs.getString("name");// ��ȡname�ֶε�ֵ
                String code = rs.getString("code");// ��ȡcode�ֶε�ֵ
                // ��id��name��code������ֵ��װ��Ա������
                Employee e = new Employee(id, name, code);
                return e;// ���ش�Ա������
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
        return null;// �޴�Ա���򷵻�null
    }

    @Override
    public Employee getEmp(String code) {
        String sql = "select id,name from t_emp where code = ?";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, code);
            rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Employee e = new Employee(id, name, code);
                return e;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
        return null;// �޴�Ա���򷵻�null
    }

    @Override
    public void addEmp(Employee e) {
        String sql = "insert into t_emp(name,code) values(?,?)";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, e.getName());
            ps.setString(2, e.getCode());
            ps.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
    }

    @Override
    public void deleteEmp(Integer id) {
        String sql = "delete from t_emp where id = ?";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
    }

    @Override
    public WorkTime getWorkTime() {
        String sql = "select start,end from t_work_time ";
        con = JDBCUtil.getConnection();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String start = rs.getString("start");
                String end = rs.getString("end");
                return new WorkTime(start, end);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
        return null;// �޼�¼�򷵻�null
    }

    @Override
    public void updateWorkTime(WorkTime time) {
        String sql = "update t_work_time set start = ?, end = ? ";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, time.getStart());
            ps.setString(2, time.getEnd());
            ps.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
    }

    @Override
    public void addCLockInRecord(int empID, Date now) {
        // ���ڸ�ʽ��
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(now);// ������תΪ�ַ���
        String sql = "insert into t_lock_in_record(emp_id,lock_in_time) values(?,?)";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, empID);
            ps.setString(2, time);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
    }

    @Override
    public void deleteClockInRecord(int empID) {
        String sql = "delete from t_lock_in_record where emp_id = ?";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, empID);
            ps.executeUpdate();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
    }

    @Override
    public boolean userLogin(User user) {
        String sql = "select id from t_user where username = ? and password = ? ";
        con = JDBCUtil.getConnection();
        try {
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            rs = ps.executeQuery();
            return rs.next();// ����в�ѯ����򷵻�true
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
        return false;// �޲�ѯ����򷵻�false
    }

    @Override
    public String[][] getAllClockInRecord() {
        // �����ѯ���ݵļ��ϡ���Ϊ��ȷ������������ʹ�ü��϶����Ƕ�ά���顣
        HashSet<String[]> set = new HashSet<>();
        String sql = "select emp_id, lock_in_time from t_lock_in_record ";
        con = JDBCUtil.getConnection();
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String emp_id = rs.getString("emp_id");
                String lock_in_time = rs.getString("lock_in_time");
                // ֱ�ӽ���ѯ������������ַ����������ʽ�ŵ�������
                set.add(new String[] { emp_id, lock_in_time });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.close(stmt, ps, rs);// �ر����ݿ�ӿڶ���
        }
        if (set.isEmpty()) {// ��������ǿյģ���ʾ����û���κδ�����
            return null;
        } else {// ������ڴ�����
            //������ά������Ϊ���ؽ������������Ϊ����Ԫ�ظ���������Ϊ2
            String result[][] = new String[set.size()][2];
            Iterator<String[]> it = set.iterator();//�������ϵ�����
            for (int i = 0; it.hasNext(); i++) {//�������ϣ�ͬʱ��i����
                result[i] = it.next();//�����е�ÿһ��Ԫ�ض���Ϊ�����ÿһ������
            }
            return result;
        }
    }
}
