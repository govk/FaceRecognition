package com.mr.clock.frame;

import java.awt.Container;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.mr.clock.session.Session;

/**
 * ������
 * 
 * @author mingrisoft
 *
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        Session.init();// ȫ�ֻỰ��ʼ��
        addListener();// ��Ӽ���
        setSize(640, 480);// ������
        // ����رհ�ť�������κ��¼�
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        Toolkit tool = Toolkit.getDefaultToolkit(); // ����ϵͳĬ��������߰�
        Dimension d = tool.getScreenSize(); // ��ȡ��Ļ�ߴ磬�����������
        // ������������Ļ�м���ʾ
        setLocation((d.width - getWidth()) / 2, (d.height - getHeight()) / 2);
    }

    /**
     * ����������
     */
    private void addListener() {
        addWindowListener(new WindowAdapter() {// ��Ӵ����¼�����
            @Override
            public void windowClosing(WindowEvent e) {// ����ر�ʱ
                // ����ѡ��Ի��򣬲���¼�û�������ѡ��
                int closeCode = JOptionPane.showConfirmDialog(MainFrame.this, "�Ƿ��˳�����", "��ʾ��",
                        JOptionPane.YES_NO_OPTION);
                if (closeCode == JOptionPane.YES_OPTION) {// ����û�ѡ��ȷ��
                    Session.dispose();// �ͷ�ȫ����Դ
                    System.exit(0);// �رճ���
                }
            }
        });
    }

    /**
     * �����������е����
     * 
     * @param panel ���������
     */
    public void setPanel(JPanel panel) {
        Container c = getContentPane();// ��ȡ����������
        c.removeAll();// ɾ���������������
        c.add(panel);// ����������
        c.validate();// ����������֤�������
    }
}
