package com.mr.clock.frame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.mr.clock.pojo.Employee;
import com.mr.clock.service.HRService;
import com.mr.clock.session.Session;

/**
 * Ա���������
 * 
 * @author mingrisoft
 *
 */
public class EmployeeManagementPanel extends JPanel {
    private MainFrame parent;// ������
    private JTable table;// Ա����Ϣ���
    private DefaultTableModel model;// ��������ģ��
    private JButton add;// ¼����Ա����ť
    private JButton delete;// ɾ��Ա����ť
    private JButton back;// ���ذ�ť

    public EmployeeManagementPanel(MainFrame parent) {
        this.parent = parent;
        init();// �����ʼ��
        addListener();// Ϊ�����Ӽ���
    }

    /**
     * �����ʼ��
     */
    private void init() {
        parent.setTitle("Ա������");// �޸ı���
        add = new JButton("¼����Ա��");
        delete = new JButton("ɾ��Ա��");
        back = new JButton("����");

        model = new DefaultTableModel();
        String columnName[] = { "Ա�����", "Ա������" };// �����ͷ
        int count = Session.EMP_SET.size();// Ա������
        String value[][] = new String[count][2];// ���չʾ������
        // ����Ա����ϵĵ�����
        Iterator<Employee> it = Session.EMP_SET.iterator();
        for (int i = 0; it.hasNext(); i++) {// ����ÿ��Ա��������i����
            Employee e = it.next();// ��ȡһ��Ա��
            value[i][0] = String.valueOf(e.getId());// ��һ��ΪԱ�����
            value[i][1] = e.getName();// �ڶ���ΪԱ������

        }

        model.setDataVector(value, columnName);// �����ݺ���ͷ����������ģ����

        table = new EmpTable(model);// �����Զ����Ա����Ϣ�����
        JScrollPane scroll = new JScrollPane(table);// ��������������

        setLayout(new BorderLayout());// ���ñ߽粼��
        add(scroll, BorderLayout.CENTER);// ���������в�

        JPanel bottom = new JPanel();// �ײ����
        bottom.add(add);// ��ӵײ����
        bottom.add(delete);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Ϊ�����Ӽ���
     */
    private void addListener() {
        // ¼����Ա����ť���¼�
        add.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // �������л��������Ա�����
                parent.setPanel(new AddEmployeePanel(parent));
            }
        });
        // ɾ��Ա����ť���¼�
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selecRow = table.getSelectedRow();// ��ȡ���ѡ�е�������
                if (selecRow != -1) {// ������б�ѡ��
                    // ����ѡ��Ի��򣬲���¼�û�ѡ��
                    int deleteCode = JOptionPane.showConfirmDialog(parent, "ȷ��ɾ����Ա����", "��ʾ��",
                            JOptionPane.YES_NO_OPTION);
                    if (deleteCode == JOptionPane.YES_OPTION) {// ����û�ѡ��ȷ��
                        // ��ȡѡ�е�Ա�����
                        String id = (String) model.getValueAt(selecRow, 0);
                        HRService.deleteEmp(Integer.parseInt(id));// ɾ����Ա��
                        model.removeRow(selecRow);// ���ɾ������
                    }
                }
            }
        });
        // ���ذ�ť���¼�
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // ���ذ�ť���¼�
                parent.setPanel(new MainPanel(parent));
            }
        });
    }

    /**
     * �Զ���Ա����Ϣ�����
     * 
     * @author mingrisoft
     *
     */
    private class EmpTable extends JTable {

        public EmpTable(TableModel dm) {
            super(dm);
            setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ֻ�ܵ�ѡ
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;// ��񲻿ɱ༭
        }

        @Override
        public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
            // ��ȡ��Ԫ����Ⱦ����
            DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super.getDefaultRenderer(columnClass);
            // ������־�����ʾ
            cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
            return cr;
        }
    }
}
