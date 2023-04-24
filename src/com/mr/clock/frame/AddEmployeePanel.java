package com.mr.clock.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.arcsoft.face.FaceFeature;
import com.mr.clock.pojo.Employee;
import com.mr.clock.service.CameraService;
import com.mr.clock.service.FaceEngineService;
import com.mr.clock.service.HRService;
import com.mr.clock.service.ImageService;
import com.mr.clock.session.Session;

/**
 * �����Ա�����
 * 
 * @author mingrisoft
 *
 */
public class AddEmployeePanel extends JPanel {
    private MainFrame parent;// ������
    private JLabel message;// ��ʾ
    private JTextField nameField;// �����ı���
    private JButton submit;// �ύ��ť
    private JButton back;// ���ذ�ť
    private JPanel center;// �в����

    public AddEmployeePanel(MainFrame parent) {
        this.parent = parent;
        init();// �����ʼ��
        addListener();// Ϊ�����Ӽ���
    }

    /**
     * �����ʼ��
     */
    private void init() {
        parent.setTitle("¼����Ա��");// �޸����������
        JLabel nameLabel = new JLabel("Ա������:", JLabel.RIGHT);
        nameField = new JTextField(15);// �ı�����Ϊ15
        submit = new JButton("���ղ�¼��");
        back = new JButton("����");

        setLayout(new BorderLayout());// ���ñ߽粼��

        JPanel bottom = new JPanel();// �ײ����
        bottom.add(nameLabel);// ��ӵײ������
        bottom.add(nameField);
        bottom.add(submit);
        bottom.add(back);
        add(bottom, BorderLayout.SOUTH);

        center = new JPanel();// �в����
        center.setLayout(null);// �в������þ��Բ���

        message = new JLabel("���ڴ�����ͷ......");
        message.setFont(new Font("����", Font.BOLD, 40));// ��������
        message.setBounds((640 - 400) / 2, 20, 400, 50);// �������������Ϳ��
        center.add(message);

        JPanel blackPanel = new JPanel();// ��ɫ���
        blackPanel.setBackground(Color.BLACK);// ����ɫΪ��ɫ
        blackPanel.setBounds(150, 75, 320, 240);// ������������Ϳ��
        center.add(blackPanel);

        add(center, BorderLayout.CENTER);

        // ����ͷ�����߳�
        Thread cameraThread = new Thread() {
            public void run() {
                if (CameraService.startCamera()) {// �������ͷ�ɹ�����
                    message.setText("��������������ͷ");// ������ʾ��Ϣ
                    // ��ȡ����ͷ�������
                    JPanel cameraPanel = CameraService.getCameraPanel();
                    // ������������Ϳ��
                    cameraPanel.setBounds(150, 75, 320, 240);
                    center.add(cameraPanel);// �ŵ��в����
                } else {
                    // ������ʾ
                    JOptionPane.showMessageDialog(parent, "δ��⵽����ͷ��");
                    back.doClick();// �������ذ�ť�ĵ���¼�
                }
            }
        };
        cameraThread.start();// �����߳�
    }

    /**
     * Ϊ�����Ӽ���
     */
    private void addListener() {
        // �ύ��ť���¼�
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e1) {
                String name = nameField.getText().trim();// ��ȡ�ı����������
                if (name == null || "".equals(name)) {// ����ǿ�����
                    JOptionPane.showMessageDialog(parent, "���ֲ���Ϊ�գ�");
                    return;// �жϷ���
                }
                if (!CameraService.cameraIsOpen()) {// �������ͷδ����
                    JOptionPane.showMessageDialog(parent, "����ͷ��δ���������Ժ�");
                    return;
                }
                // ��ȡ��ǰ����ͷ��׽��֡
                BufferedImage image = CameraService.getCameraFrame();
                // ��ȡ��ͼ�����������沿����
                FaceFeature ff = FaceEngineService.getFaceFeature(image);
                if (ff == null) {// ����������沿����
                    JOptionPane.showMessageDialog(parent, "δ��⵽��Ч������Ϣ");
                    return;
                }
                Employee e = HRService.addEmp(name, image);// �����Ա��
                ImageService.saveFaceImage(image, e.getCode());// ����Ա����Ƭ�ļ�
                Session.FACE_FEATURE_MAP.put(e.getCode(), ff);// ȫ�ֻػ���¼�����沿����
                JOptionPane.showMessageDialog(parent, "Ա����ӳɹ���");// ������ʾ��
                back.doClick();// �������ذ�ť�ĵ���¼�
            }
        });
        // ���ذ�ť���¼�
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CameraService.releaseCamera();// �ͷ�����ͷ
                // �������л���Ա���������
                parent.setPanel(new EmployeeManagementPanel(parent));
            }
        });
    }
}
