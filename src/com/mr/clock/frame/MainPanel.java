package com.mr.clock.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;

import com.mr.clock.pojo.Employee;
import com.mr.clock.service.CameraService;
import com.mr.clock.service.FaceEngineService;
import com.mr.clock.service.HRService;
import com.mr.clock.session.Session;
import com.mr.clock.util.DateTimeUtil;

/**
 * �����
 * 
 * @author mingrisoft
 *
 */
public class MainPanel extends JPanel {
    private MainFrame parent;// ������
    private JToggleButton daka;// �򿨰�ť
    private JButton kaoqin;// ���ڰ�ť
    private JButton yuangong;// Ա����ť
    private JTextArea area;// ��ʾ��Ϣ�ı���
    private DetectFaceThread dft;// ����ʶ���߳�
    private JPanel center;// �в����

    public MainPanel(MainFrame parent) {
        this.parent = parent;
        init();// �����ʼ��
        addListener();// Ϊ�����Ӽ���
    }

    /**
     * �����ʼ��
     */
    private void init() {
        parent.setTitle("MR����ʶ���ϵͳ");

        center = new JPanel();// �в����
        center.setLayout(null);// ���þ��Բ���

        area = new JTextArea();
        area.setEditable(false);// �ı��򲻿ɱ༭
        area.setFont(new Font("����", Font.BOLD, 18));// ����
        JScrollPane scroll = new JScrollPane(area);// �ı������������
        scroll.setBounds(0, 0, 275, 380);// ����������������
        center.add(scroll);

        daka = new JToggleButton("��  ��");
        daka.setFont(new Font("����", Font.BOLD, 40));// ����
        daka.setBounds(330, 300, 240, 70);// �򿨰�ť����������
        center.add(daka);

        JPanel blakPanel = new JPanel();// �������
        blakPanel.setBounds(286, 16, 320, 240);// ��ɫ������������
        blakPanel.setBackground(Color.BLACK);// ��ɫ����
        center.add(blakPanel);

        setLayout(new BorderLayout());// �������ñ߽粼��
        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel();// �ײ����
        kaoqin = new JButton("���ڱ���");
        yuangong = new JButton("Ա������");
        bottom.add(kaoqin);// ��ӵײ����
        bottom.add(yuangong);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Ϊ�����Ӽ���
     */
    private void addListener() {
        // ���ڰ�ť���¼�
        kaoqin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Session.user == null) {// ���û�й���Ա��¼
                    // ������¼�Ի���
                    LoginDialog ld = new LoginDialog(parent);
                    ld.setVisible(true);// չʾ��¼�Ի���
                }
                if (Session.user != null) {// �������Ա�ѵ�¼
                    // �������ڱ������
                    AttendanceManagementPanel amp = new AttendanceManagementPanel(parent);
                    parent.setPanel(amp);// �������л����������
                    releaseCamera();// �ͷ�����ͷ
                }
            }
        });
        // Ա����ť���¼�
        yuangong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Session.user == null) {// ���û�й���Ա��¼
                    // ������¼�Ի���
                    LoginDialog ld = new LoginDialog(parent);
                    ld.setVisible(true);// չʾ��¼�Ի���
                }
                if (Session.user != null) {// �������Ա�ѵ�¼
                    // ����Ա���������
                    EmployeeManagementPanel emp = new EmployeeManagementPanel(parent);
                    parent.setPanel(emp);// �������л����������
                    releaseCamera();// �ͷ�����ͷ��Դ
                }
            }
        });
        // �򿨰�ť���¼�
        daka.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (daka.isSelected()) {// ����򿨰�ť��ѡ��״̬
                    // �ı��������ʾ��Ϣ
                    area.append("���ڿ�������ͷ�����Ժ�.......\n");
                    daka.setEnabled(false);// �򿨰�ť������
                    daka.setText("�ر�����ͷ");// ���Ĵ򿨰�ť���ı�
                    // ������������ͷ����ʱ�߳�
                    Thread cameraThread = new Thread() {
                        public void run() {
                            // �������ͷ������������
                            if (CameraService.startCamera()) {
                                area.append("����������ͷ�򿨡�\n");// �����ʾ
                                daka.setEnabled(true);// �򿨰�ť����
                                // ��ȡ����ͷ�������
                                JPanel cameraPanel = CameraService.getCameraPanel();
                                // ����������������
                                cameraPanel.setBounds(286, 16, 320, 240);
                                center.add(cameraPanel);// �ŵ��в���嵱��
                            } else {
                                // ������ʾ
                                JOptionPane.showMessageDialog(parent, "δ��⵽����ͷ��");
                                releaseCamera();// �ͷ�����ͷ��Դ
                                return;// ֹͣ����
                            }
                        }
                    };
                    cameraThread.start();// ������ʱ�߳�
                    dft = new DetectFaceThread();// ��������ʶ���߳�
                    dft.start();// ��������ʶ���߳�
                } else {// ����򿨰�ť����ѡ��״̬
                    releaseCamera();// �ͷ�����ͷ��Դ
                }
            }
        });
    }

    /**
     * �ͷ�����ͷ������е�һЩ����Դ
     */
    private void releaseCamera() {
        CameraService.releaseCamera();// �ͷ�����ͷ
        area.append("����ͷ�ѹرա�\n");// �����ʾ��Ϣ
        if (dft != null) {// �������ʶ���̱߳�����
            dft.stopThread();// ֹͣ�߳�
        }
        daka.setText("��  ��");// ���Ĵ򿨰�ť���ı�
        daka.setSelected(false);// �򿨰�ť��Ϊδѡ��״̬
        daka.setEnabled(true);// �򿨰�ť����
    }

    /**
     * ����ʶ���߳�
     * 
     * @author mingrisoft
     *
     */
    private class DetectFaceThread extends Thread {
        boolean work = true;// ����ʶ���߳��Ƿ����ɨ��image

        @Override
        public void run() {
            while (work) {
                // �������ͷ�ѿ���
                if (CameraService.cameraIsOpen()) {
                    // ��ȡ����ͷ�ĵ�ǰ֡
                    BufferedImage frame = CameraService.getCameraFrame();
                    if (frame != null) {// ������Ի�õ���Ч֡
                        // ��ȡ��ǰ֡�г��ֵ�������Ӧ��������
                        String code = FaceEngineService.detectFace(FaceEngineService.getFaceFeature(frame));
                        if (code != null) {// ��������벻Ϊnull�����������д���ĳԱ��������
                            Employee e = HRService.getEmp(code);// �����������ȡԱ������
                            HRService.addClockInRecord(e);// Ϊ��Ա����Ӵ򿨼�¼
                            // �ı��������ʾ��Ϣ
                            area.append("\n" + DateTimeUtil.dateTimeNow() + " \n");
                            area.append(e.getName() + " �򿨳ɹ���\n\n");
                            releaseCamera();// �ͷ�����ͷ
                        }
                    }
                }
            }
        }

        public synchronized void stopThread() {// ֹͣ����ʶ���߳�
            work = false;
        }
    }
}
