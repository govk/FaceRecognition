package com.mr.clock.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

import com.mr.clock.pojo.WorkTime;
import com.mr.clock.service.HRService;
import com.mr.clock.session.Session;
import com.mr.clock.util.DateTimeUtil;

/**
 * ���ڱ������
 * 
 * @author mingrisoft
 *
 */
public class AttendanceManagementPanel extends JPanel {
    private MainFrame parent;// ������

    private JToggleButton dayRecordBtn; // �ձ���ť
    private JToggleButton monthRecordBtn;// �±���ť
    private JToggleButton worktimeBtn;// ��Ϣʱ�����ð�ť
    private JButton back;// ���ذ�ť
    private JButton flushD, flushM;// �ֱ����ձ����±�����е�ˢ�°�ť
    private JPanel centerdPanel; // �������
    private CardLayout card;// �������ʹ�õĿ�Ƭ����

    private JPanel dayRecordPanel;// �ձ����
    private JTextArea area;// �ձ��������ı���
    // �ձ��������ꡢ�¡��������б�
    private JComboBox<Integer> yearComboBoxD, monthComboBoxD, dayComboBoxD;
    // �ꡢ�¡��������б�ʹ�õ�����ģ��
    private DefaultComboBoxModel<Integer> yearModelD, monthModelD, dayModelD;

    private JPanel monthRecordPanel;// �±����
    private JTable table;// �±������ı��
    private DefaultTableModel model;// ��������ģ��
    // �±��������ꡢ�������б�
    private JComboBox<Integer> yearComboBoxM, monthComboBoxM;
    // �ꡢ�������б�ʹ�õ�����ģ��
    private DefaultComboBoxModel<Integer> yearModelM, monthModelM;

    private JPanel worktimePanel;// ��Ϣʱ�����
    // �ϰ�ʱ���ʱ���֡����ı���
    private JTextField hourS, minuteS, secondS;
    // �°�ʱ���ʱ���֡����ı���
    private JTextField hourE, minuteE, secondE;
    private JButton updateWorktime;// �滻��Ϣʱ�䰴ť

    public AttendanceManagementPanel(MainFrame parent) {
        this.parent = parent;
        init();// �����ʼ��
        addListener();// Ϊ�����Ӽ���
    }

    /**
     * �����ʼ��
     */
    private void init() {
        WorkTime worktime = Session.worktime;// ��ȡ��ǰ��Ϣʱ��
        // �޸����������
        parent.setTitle("���ڱ��� (�ϰ�ʱ�䣺" + worktime.getStart() + ",�°�ʱ�䣺" + worktime.getEnd() + ")");
        dayRecordBtn = new JToggleButton("�ձ�");
        dayRecordBtn.setSelected(true);// �ձ���ť����ѡ��״̬
        monthRecordBtn = new JToggleButton("�±�");
        worktimeBtn = new JToggleButton("��Ϣʱ������");
        // ��ť�飬��֤������ť��ֻ��һ����ť����ѡ��״̬
        ButtonGroup group = new ButtonGroup();
        group.add(dayRecordBtn);
        group.add(monthRecordBtn);
        group.add(worktimeBtn);

        back = new JButton("����");
        flushD = new JButton("ˢ�±���");
        flushM = new JButton("ˢ�±���");

        ComboBoInit();// �����б��ʼ��
        dayRecordInit();// �ձ�����ʼ��
        MonthRecordInit();// �±�����ʼ��
        worktimeInit();// ��Ϣʱ������ʼ��

        card = new CardLayout();// ��Ƭ����
        centerdPanel = new JPanel(card);// �в������ÿ�Ƭ����
        // day��ǩΪ�ձ����
        centerdPanel.add("day", dayRecordPanel);
        // month��ǩΪ�±����
        centerdPanel.add("month", monthRecordPanel);
        // worktime��ǩΪ��Ϣʱ�����
        centerdPanel.add("worktime", worktimePanel);

        JPanel bottom = new JPanel();// �ײ����
        bottom.add(dayRecordBtn);// ��ӵײ������
        bottom.add(monthRecordBtn);
        bottom.add(worktimeBtn);
        bottom.add(back);

        setLayout(new BorderLayout());// ���ñ߽粼��
        add(centerdPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);//
    }

    /**
     * Ϊ�����Ӽ���
     */
    private void addListener() {
        // �ձ���ť���¼�
        dayRecordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��Ƭ�����л����ձ����
                card.show(centerdPanel, "day");
            }
        });
        // �±���ť���¼�
        monthRecordBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��Ƭ�����л����±����
                card.show(centerdPanel, "month");
            }
        });
        // ��Ϣʱ�����ð�ť���¼�
        worktimeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��Ƭ�����л�����Ϣʱ�����
                card.show(centerdPanel, "worktime");
            }
        });
        
        // ���ذ�ť���¼�
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setPanel(new MainPanel(parent)); // �������л��������
            }
        });
        // �ձ����ˢ�°�ť���¼�
        flushD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDayRecord();// �����ձ�
            }
        });
        // �±����ˢ�°�ť���¼�
        flushM.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMonthRecord();// �����±�
            }
        });
        // �滻��Ϣʱ�䰴ť���¼�
        updateWorktime.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String hs = hourS.getText().trim();// �ϰ��Сʱ
                String ms = minuteS.getText().trim();// �ϰ�ķ���
                String ss = secondS.getText().trim();// �ϰ����
                String he = hourE.getText().trim();// �°��Сʱ
                String me = minuteE.getText().trim();// �°�ķ���
                String se = secondE.getText().trim();// �°����

                boolean check = true;// ʱ��У��ɹ���־
                String startInput = hs + ":" + ms + ":" + ss;// ƴ���ϰ�ʱ��
                String endInput = he + ":" + me + ":" + se;// ƴ���°�ʱ��
                // ����ϰ�ʱ�䲻����ȷ��ʱ���ʽ
                if (!DateTimeUtil.checkTimeStr(startInput)) {
                    check = false;// У��ʧ��
                    // ������ʾ
                    JOptionPane.showMessageDialog(parent, "�ϰ�ʱ��ĸ�ʽ����ȷ");
                }
                // ����°�ʱ�䲻����ȷ��ʱ���ʽ
                if (!DateTimeUtil.checkTimeStr(endInput)) {
                    check = false;// У��ʧ��
                    // ������ʾ
                    JOptionPane.showMessageDialog(parent, "�°�ʱ��ĸ�ʽ����ȷ");
                }

                if (check) {// ���У��ͨ��
                    // ����ѡ��Ի��򣬲���¼�û�ѡ��
                    int confirmation = JOptionPane.showConfirmDialog(parent,
                            "ȷ�������������ã�\n�ϰ�ʱ�䣺" + startInput + "\n�°�ʱ�䣺" + endInput, "��ʾ��", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {// ����û�ѡ��ȷ��
                        WorkTime input = new WorkTime(startInput, endInput);
                        HRService.updateWorkTime(input);// ������Ϣʱ��
                        // �޸ı���
                        parent.setTitle("���ڱ��� (�ϰ�ʱ�䣺" + startInput + ",�°�ʱ�䣺" + endInput + ")");
                    }
                }
            }
        });

        // �ձ�����е����������б�ʹ�õļ�������
        ActionListener dayD_Listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDayRecord();// �����ձ�
            }
        };
        dayComboBoxD.addActionListener(dayD_Listener);// ��Ӽ���

        // �ձ�����е���ݡ��·������б�ʹ�õļ�������
        ActionListener yearD_monthD_Listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ɾ�����������б�ʹ�õļ������󣬷�ֹ���ڸı���Զ������˼���
                dayComboBoxD.removeActionListener(dayD_Listener);
                updateDayModel();// �����������б��е�����
                updateDayRecord();// �����ձ�
                // ����Ϊ���������б���Ӽ�������
                dayComboBoxD.addActionListener(dayD_Listener);
            }
        };

        yearComboBoxD.addActionListener(yearD_monthD_Listener);// ��Ӽ���
        monthComboBoxD.addActionListener(yearD_monthD_Listener);

        // �±�����е���ݡ��·������б�ʹ�õļ�������
        ActionListener yearM_monthM_Listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMonthRecord();// �����±�
            }
        };

        yearComboBoxM.addActionListener(yearM_monthM_Listener);// ��Ӽ���
        monthComboBoxM.addActionListener(yearM_monthM_Listener);
    }

    /**
     * ��Ϣʱ������ʼ��
     */
    private void worktimeInit() {
        WorkTime worktime = Session.worktime;// ��ȡ��ǰ����Ϣʱ��
        // ���ϰ�ʱ����°�ʱ��ָ��ʱ���֡�������
        String startTime[] = worktime.getStart().split(":");
        String endTime[] = worktime.getEnd().split(":");

        Font labelFont = new Font("����", Font.BOLD, 20);// ����

        JPanel top = new JPanel();// �������

        JLabel startLabel = new JLabel("�ϰ�ʱ�䣺");// �ı���ǩ
        startLabel.setFont(labelFont);// ʹ��ָ������
        top.add(startLabel);

        hourS = new JTextField(3);// �ϰ�ʱ���ʱ����򣬳���Ϊ3
        hourS.setText(startTime[0]);// Ĭ��ֵΪ��ǰ�ϰ�ʱ���Сʱ
        top.add(hourS);

        JLabel colon1 = new JLabel(":");
        colon1.setFont(labelFont);
        top.add(colon1);

        minuteS = new JTextField(3);// �ϰ�ʱ��ķ������
        minuteS.setText(startTime[1]);// Ĭ��ֵΪ��ǰ�ϰ�ʱ��ķ���
        top.add(minuteS);

        JLabel colon2 = new JLabel(":");
        colon2.setFont(labelFont);
        top.add(colon2);

        secondS = new JTextField(3);// �ϰ�ʱ����������
        secondS.setText(startTime[2]);// Ĭ��ֵΪ��ǰ�ϰ�ʱ�����
        top.add(secondS);

        JPanel bottom = new JPanel();// �ײ����

        JLabel endLabel = new JLabel("�°�ʱ�䣺");
        endLabel.setFont(labelFont);
        bottom.add(endLabel);

        hourE = new JTextField(3);// �°�ʱ���ʱ�����
        hourE.setText(endTime[0]);// Ĭ��ֵΪ��ǰ�°�ʱ���Сʱ
        bottom.add(hourE);

        JLabel colon3 = new JLabel(":");
        colon3.setFont(labelFont);
        bottom.add(colon3);

        minuteE = new JTextField(3);// �°�ʱ��ķ������
        minuteE.setText(endTime[1]);// Ĭ��ֵΪ��ǰ�°�ʱ��ķ���
        bottom.add(minuteE);

        JLabel colon4 = new JLabel(":");
        colon4.setFont(labelFont);
        bottom.add(colon4);

        secondE = new JTextField(3);// �°�ʱ����������
        secondE.setText(endTime[2]);// Ĭ��ֵΪ��ǰ�°�ʱ�����
        bottom.add(secondE);

        worktimePanel = new JPanel();
        worktimePanel.setLayout(null);// ��Ϣʱ�������þ��Բ���

        JPanel center = new JPanel();// ��Ϣ���������ʾ�����
        center.setLayout(new GridLayout(2, 1));// ����2��1�е����񲼾�
        center.add(top);// ��1�зŶ������
        center.add(bottom);// ��2�зŵײ����

        center.setBounds(100, 60, 400, 150);// ������������Ϳ��
        worktimePanel.add(center);

        updateWorktime = new JButton("�滻��Ϣʱ��");
        updateWorktime.setFont(new Font("����", Font.BOLD, 20));
        updateWorktime.setBounds(220, 235, 170, 55);// ��ť������Ϳ��
        worktimePanel.add(updateWorktime);
    }

    /**
     * �ձ�����ʼ��
     */
    private void dayRecordInit() {
        area = new JTextArea();
        area.setEditable(false);// �ı��򲻿ɱ༭
        area.setFont(new Font("����", Font.BOLD, 24));
        JScrollPane scroll = new JScrollPane(area);// �ı���ŵ����������

        dayRecordPanel = new JPanel();
        dayRecordPanel.setLayout(new BorderLayout());// �ձ������ñ߽粼��
        dayRecordPanel.add(scroll, BorderLayout.CENTER);// ����������в���ʾ

        JPanel top = new JPanel();// �������
        top.setLayout(new FlowLayout());// ����������
        top.add(yearComboBoxD);// �������б�
        top.add(new JLabel("��"));// �ı���ǩ
        top.add(monthComboBoxD);// �������б�
        top.add(new JLabel("��"));
        top.add(dayComboBoxD);// �������б�
        top.add(new JLabel("��"));
        top.add(flushD);// �ձ�����ˢ�°�ť
        dayRecordPanel.add(top, BorderLayout.NORTH);

        updateDayRecord();// �����ձ�
    }

    /**
     * �±�����ʼ��
     */
    private void MonthRecordInit() {
        JPanel top = new JPanel();// �������
        top.add(yearComboBoxM);// �������б�
        top.add(new JLabel("��"));
        top.add(monthComboBoxM);// �������б�
        top.add(new JLabel("��"));
        top.add(flushM);// �±�����ˢ�°�ť

        monthRecordPanel = new JPanel();
        monthRecordPanel.setLayout(new BorderLayout());// �±������ñ߽粼��
        monthRecordPanel.add(top, BorderLayout.NORTH);

        model = new DefaultTableModel();// �������ģ��
        table = new JTable(model);// ��������ģ��
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);// �ر��Զ��������
        JScrollPane tableScroll = new JScrollPane(table);// ������������
        monthRecordPanel.add(tableScroll, BorderLayout.CENTER);

        updateMonthRecord();// �����±�
    }

    /**
     * �����б��ʼ��
     */
    private void ComboBoInit() {
        // �����б������ģ�ͳ�ʼ��
        yearModelD = new DefaultComboBoxModel<>();
        monthModelD = new DefaultComboBoxModel<>();
        dayModelD = new DefaultComboBoxModel<>();
        yearModelM = new DefaultComboBoxModel<>();
        monthModelM = new DefaultComboBoxModel<>();

        // ��ȡ��ǰʱ����ꡢ�¡��ա�ʱ���֡�������
        Integer now[] = DateTimeUtil.now();

        // ��ȡ��ǰʱ��ǰ��ʮ�����ݣ���ӵ��������б������ģ����
        for (int i = now[0] - 10; i <= now[0] + 10; i++) {
            yearModelD.addElement(i);
            yearModelM.addElement(i);
        }
        yearComboBoxD = new JComboBox<>(yearModelD);// �ձ����������б�
        yearComboBoxD.setSelectedItem(now[0]);// Ĭ��ѡ�н���
        yearComboBoxM = new JComboBox<>(yearModelM);// �±����������б�
        yearComboBoxM.setSelectedItem(now[0]);// Ĭ��ѡ�н���

        // ����12����,����ӵ��������б������ģ����
        for (int i = 1; i <= 12; i++) {
            monthModelD.addElement(i);
            monthModelM.addElement(i);
        }
        monthComboBoxD = new JComboBox<>(monthModelD);// �ձ����������б�
        monthComboBoxD.setSelectedItem(now[1]);// Ĭ��ѡ�б���
        monthComboBoxM = new JComboBox<>(monthModelM);// �ձ����������б�
        monthComboBoxM.setSelectedItem(now[1]);// Ĭ��ѡ�б���

        updateDayModel();// �����������б��е�����
        dayComboBoxD = new JComboBox<>(dayModelD);// �ձ����������б�
        dayComboBoxD.setSelectedItem(now[2]);// Ĭ��ѡ�н���
    }

    /**
     * �����������б��е�����
     */
    private void updateDayModel() {
        // ��ȡ�������б�ѡ�е�ֵ
        int year = (int) yearComboBoxD.getSelectedItem();
        // ��ȡ�������б�ѡ�е�ֵ
        int month = (int) monthComboBoxD.getSelectedItem();
        // ��ȡѡ���·ݵ��������
        int lastDay = DateTimeUtil.getLastDay(year, month);
        dayModelD.removeAllElements();// �������Ԫ��
        for (int i = 1; i <= lastDay; i++) {
            dayModelD.addElement(i);// ��ÿһ�춼��ӵ��������б�����ģ����
        }
    }

    /**
     * �����ձ�
     */
    private void updateDayRecord() {
        // ��ȡ�ձ������ѡ�е��ꡢ�¡���
        int year = (int) yearComboBoxD.getSelectedItem();
        int month = (int) monthComboBoxD.getSelectedItem();
        int day = (int) dayComboBoxD.getSelectedItem();
        // ��ȡ�ձ�����
        String report = HRService.getDayReport(year, month, day);
        area.setText(report);// �ձ������ǵ��ı�����
    }

    /**
     * �����±�
     */
    private void updateMonthRecord() {
        // ��ȡ�±������ѡ�е��ꡢ��
        int year = (int) yearComboBoxM.getSelectedItem();
        int month = (int) monthComboBoxM.getSelectedItem();

        int lastDay = DateTimeUtil.getLastDay(year, month);// �����������

        String tatle[] = new String[lastDay + 1];// �����ͷ
        tatle[0] = "Ա������";// ��һ����Ա������
        // ����nΪѡ���·ݵ�ÿһ������
        for (int day = 1; day <= lastDay; day++) {
            tatle[day] = year + "��" + month + "��" + day + "��";
        }
        // ��ȡ�±�����
        String values[][] = HRService.getMonthReport(year, month);
        model.setDataVector(values, tatle);// �����ݺ���ͷ����������ģ����
        int columnCount = table.getColumnCount();// ��ȡ����е���������
        for (int i = 1; i < columnCount; i++) {// ����ÿһ��
            // �ӵ�2�п�ʼ��ûһ�ж���Ϊ100���
            table.getColumnModel().getColumn(i).setPreferredWidth(100);
        }
    }
}