package com.mr.clock.service;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;

/**
 * ����ͷ����
 * 
 * @author mingrisoft
 *
 */
public class CameraService {

    private static final Webcam WEBCAM = Webcam.getDefault();// ����ͷ����

    /**
     * ��������ͷ
     * 
     * @return �Ƿ����ɹ�
     */
    public static boolean startCamera() {
        if (WEBCAM == null) {// ��������û����������ͷ
            return false;
        }
        // ����ͷ����Ĭ�ϵ�640*480���
        WEBCAM.setViewSize(new Dimension(640, 480));
        return WEBCAM.open();// ��������ͷ�����ؿ����Ƿ�ɹ�
    }

    /**
     * ����ͷ�Ƿ���
     * 
     * @return
     */
    public static boolean cameraIsOpen() {
        if (WEBCAM == null) {// ��������û����������ͷ
            return false;
        }
        return WEBCAM.isOpen();
    }

    /**
     * ��ȡ����ͷ�������
     * 
     * @return
     */
    public static JPanel getCameraPanel() {
        // ����ͷ�������
        WebcamPanel panel = new WebcamPanel(WEBCAM);
        panel.setMirrored(true);// ��������
        return panel;
    }

    /**
     * ��ȡ����ͷ�����֡����
     * 
     * @return ԭʼ��С֡����
     */
    public static BufferedImage getCameraFrame() {
        // ��ȡ��ǰ֡����
        return WEBCAM.getImage();
    }

    /**
     * �ͷ�����ͷ��Դ
     */
    public static void releaseCamera() {
        if (WEBCAM != null) {
            WEBCAM.close();// �ر�����ͷ
        }
    }
}
