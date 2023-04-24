package com.mr.clock.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import com.mr.clock.session.Session;

/**
 * ͼ���ļ�����
 * 
 * @author mingrisoft
 *
 */
public class ImageService {
    // ���Ա����Ƭ�ļ����ļ���
    private static final File FACE_DIR = new File("src/faces");
    // ͼ���ļ���Ĭ�ϸ�ʽ
    private static final String SUFFIX = "png";

    /**
     * ������������ͼ���ļ�
     * 
     * @return
     */
    public static Map<String, BufferedImage> loadAllImage() {
        if (!FACE_DIR.exists()) {// �������ͼ����ļ��ж�ʧ
            System.err.println("src\\face\\����ͼ���ļ��ж�ʧ��");
            return null;
        }
        File faces[] = FACE_DIR.listFiles();// ��ȡ�ļ����µ������ļ�
        for (File f : faces) {// ����ÿһ���ļ�
            try {
                // ������ͼ���ļ���BufferedImage����
                BufferedImage img = ImageIO.read(f);
                String fileName = f.getName();// �ļ���
                // ��ȡ�ļ�����ȥ����׺��
                String code = fileName.substring(0, fileName.indexOf('.'));
                // ������ͼ����ӵ�ȫ�ֻỰ��
                Session.IMAGE_MAP.put(code, img);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * ��������ͼ���ļ�
     * 
     * @param image Ա������ͼ��
     * @param code  Ա��������
     */
    public static void saveFaceImage(BufferedImage image, String code) {
        try {
            // ��ͼ����SUFFIX��ʽд�뵽�ļ�����
            ImageIO.write(image, SUFFIX, new File(FACE_DIR, code + "." + SUFFIX));
            // ������ͼ����ӵ�ȫ�ֻỰ��
            Session.IMAGE_MAP.put(code, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * ɾ������ͼ���ļ�
     * 
     * @param code Ա��������
     */
    public static void deleteFaceImage(String code) {
        Session.IMAGE_MAP.remove(code);// ��ȫ�ֻỰ��ɾ����Ա����ͼ��
        // ������Ա������ͼ���ļ�����
        File image = new File(FACE_DIR, code + "." + SUFFIX);
        if (image.exists()) {// ������ļ�����
            image.delete();// ɾ���ļ�
            // ��ʾɾ���ļ��ɹ�
            System.out.println(image.getAbsolutePath() + " ---��ɾ��");
        }
    }
}
