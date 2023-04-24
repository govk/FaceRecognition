package com.mr.clock.frame;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.mr.clock.service.HRService;

/**
 * ��¼�Ի���
 * 
 * @author mingrisoft
 *
 */
public class LoginDialog extends JDialog {
    private JTextField usernameField = null;// �û����ı���
    private JPasswordField passwordField = null;// ���������
    private JButton loginBtn = null;// ��¼��ť
    private JButton cancelBtn = null;// ȡ����ť
    private final int WIDTH = 300, HEIGHT = 150;// �Ի���Ŀ��

    public LoginDialog(Frame owner) {
        super(owner, "����Ա��¼", true);// ����������
        setSize(WIDTH, HEIGHT);// ���ÿ��
        // ��������������ʾ
        setLocation(owner.getX() + (owner.getWidth() - WIDTH) / 2, owner.getY() + (owner.getHeight() - HEIGHT) / 2);
        init();// �����ʼ��
        addListener();// Ϊ�����Ӽ���
    }

    /**
     * �����ʼ��
     */
    private void init() {
        JLabel usernameLabel = new JLabel("�� �� �� ��", JLabel.CENTER);
        JLabel passwordLabel = new JLabel("��    �� ��", JLabel.CENTER);
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginBtn = new JButton("�� ¼");
        cancelBtn = new JButton("ȡ ��");

        Container c = getContentPane();
        c.setLayout(new GridLayout(3, 2));// 3��2�е����񲼾�
        c.add(usernameLabel);
        c.add(usernameField);
        c.add(passwordLabel);
        c.add(passwordField);
        c.add(loginBtn);
        c.add(cancelBtn);
    }

    /**
     * Ϊ�����Ӽ���
     */
    private void addListener() {
        // ȡ����ť���¼�
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDialog.this.dispose();// ���ٵ�¼�Ի���
            }
        });
        // ��¼��ť���¼�
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ��ȡ�û�������û���
                String username = usernameField.getText().trim();
                // ��ȡ�û����������
                String password = new String(passwordField.getPassword());
                // ����û����������Ƿ���ȷ
                boolean result = HRService.userLogin(username, password);
                if (result) {// �����ȷ
                    LoginDialog.this.dispose();// ���ٵ�¼�Ի���
                } else {
                    // ��ʾ�û������������
                    JOptionPane.showMessageDialog(LoginDialog.this, "�û�������������");
                }
            }
        });
        // ����������û��س����¼�
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginBtn.doClick();// ������¼��ť����¼�
            }
        });
        // �û����ı�����û��س����¼�
        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.grabFocus();// ����������ȡ���
            }
        });
    }
}
