package com.mr.clock.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.ConfigurationException;

/**
 * ���ݿ����ӹ�����
 * 
 * @author mingrisoft
 *
 */
public class JDBCUtil {
    private static String driver_name;// ������
    private static String username;// �˺�
    private static String password;// ����
    private static String url;// ���ݿ��ַ
    private static Connection con = null;// ���ݿ�����
    // ���ݿ������ļ���ַ
    private static final String CONFIG_FILE = "src/com/mr/clock/config/jdbc.properties";

    static {
        Properties pro = new Properties();// �����ļ�������
        try {
            // �����ļ����ļ�����
            File config = new File(CONFIG_FILE);
            if (!config.exists()) {// ��������ļ�������
                throw new FileNotFoundException("ȱ���ļ���" + config.getAbsolutePath());
            }
            pro.load(new FileInputStream(config));// ���������ļ�
            driver_name = pro.getProperty("driver_name");// ��ȡָ���ֶ�ֵ
            username = pro.getProperty("username", "");
            password = pro.getProperty("password", "");
            url = pro.getProperty("url");
            if (driver_name == null || url == null) {
                throw new ConfigurationException("jdbc.properties�ļ�ȱ��������Ϣ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            System.err.println("�����ļ���ȡ������:[driver_name=" + driver_name + "],[username=" + username + "],[password="
                    + password + "],[url=" + url + "]");
            e.printStackTrace();
        } 
    }

/**
 * ��ȡ���ݿ����ӡ�����jdbc.properties�е�������Ϣ���ض�Ӧ��Connection����
 * 
 * @return ���������ݿ��Connection����
 */
public static Connection getConnection() {
    try {
        if (con == null || con.isClosed()) {// ������Ӷ��󱻹ر�
            Class.forName(driver_name);// ����������
            // ����URL���˺������ȡ���ݿ�����
            con = DriverManager.getConnection(url, username, password);
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return con;
}

    /**
     * �ر�ResultSet
     * 
     * @param rs
     */
    public static void close(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * �ر�Statement
     * 
     * @param stmt
     */
    public static void close(Statement stmt) {
        try {
            if (stmt != null)
                stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * �ر�PreparedStatement
     * 
     * @param ps
     */
    public static void close(PreparedStatement ps) {
        try {
            if (ps != null)
                ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ��ȫ�Ĺر����ݿ�ӿ�
     * 
     * @param ps
     * @param rs
     */
    public static void close(Statement stmt, PreparedStatement ps, ResultSet rs) {
        close(rs);
        close(ps);
        close(stmt);
    }

    /**
     * �ر�Connection
     */
    public static void closeConnection() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
