package com.mr.clock.main;

import com.mr.clock.frame.MainFrame;
import com.mr.clock.frame.MainPanel;

/**
 * ������
 * 
 * @author mingrisoft
 *
 */
public class Main {
    public static void main(String[] args) {
        MainFrame f = new MainFrame();// ����������
        f.setPanel(new MainPanel(f));// ��������������
        f.setVisible(true);// ��ʾ������
    }
}
