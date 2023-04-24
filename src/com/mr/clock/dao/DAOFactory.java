package com.mr.clock.dao;

/**
 * DAO������
 * 
 * @author mingrisoft
 *
 */
public class DAOFactory {
    /**
     * ��ȡDAO����
     * 
     * @return
     */
    public static DAO getDAO() {
        return new DAOMysqlImpl();// ���ػ���Mysql��ʵ����
//         return new DAOSqliteImpl();// ���ػ���Sqlite��ʵ����
    }
}
