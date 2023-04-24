package com.mr.clock.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.naming.ConfigurationException;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.mr.clock.session.Session;

/**
 * ����ʶ���������
 * 
 * @author mingrisoft
 *
 */
public class FaceEngineService {
    private static String appId = null;
    private static String sdkKey = null;
    private static FaceEngine faceEngine = null;// ����ʶ������
    private static String ENGINE_PATH = "ArcFace/WIN64";// �㷨���ַ
    // �����ļ���ַ
    private static final String CONFIG_FILE = "src/com/mr/clock/config/ArcFace.properties";

    static {
        Properties pro = new Properties();// �����ļ�������
        File config = new File(CONFIG_FILE);// �����ļ����ļ�����
        try {
            if (!config.exists()) {// ��������ļ�������
                throw new FileNotFoundException("ȱ���ļ���" + config.getAbsolutePath());
            }
            pro.load(new FileInputStream(config));// ���������ļ�
            appId = pro.getProperty("app_id");// ��ȡָ���ֶ�ֵ
            sdkKey = pro.getProperty("sdk_key");
            if (appId == null || sdkKey == null) {// ��������ļ��л�ȡ���������ֶ�
                throw new ConfigurationException("ArcFace.properties�ļ�ȱ��������Ϣ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File path = new File(ENGINE_PATH);// �㷨���ļ���
        faceEngine = new FaceEngine(path.getAbsolutePath());// ����ʶ������
        // �������棬**�״μ�����Ҫ����**
        int errorCode = faceEngine.activeOnline(appId, sdkKey);
        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.err.println("ERROR: ArcFace���漤��ʧ�ܣ�������Ȩ���Ƿ���д���󣬻������������");
        }

        // ��������
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        // ����ͼ��ģʽ
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        // ������нǶ�
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);
        // ������������
        engineConfiguration.setDetectFaceMaxNum(1);
        // �����������������ͼƬ�ĳ��ߵ�ռ��
        engineConfiguration.setDetectFaceScaleVal(16);
        // ��������
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        // ֧���������
        functionConfiguration.setSupportFaceDetect(true);
        // ֧������ʶ��
        functionConfiguration.setSupportFaceRecognition(true);
        // ����ʹ�ô˹�������
        engineConfiguration.setFunctionConfiguration(functionConfiguration);
        // ��ʼ������
        errorCode = faceEngine.init(engineConfiguration);
        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.err.println("ERROR:ArcFace�����ʼ��ʧ��");
        }
    }

    /**
     * ��ȡһ���������沿����
     * 
     * @param img ������Ƭ
     * @return
     */
    public static FaceFeature getFaceFeature(BufferedImage img) {
        if (img == null) {
            throw new NullPointerException("����ͼ��Ϊnull");
        }
        //����һ����ԭͼ��һ�������ʱͼ����ʱͼ������Ϊ��ͨBRGͼ��
        BufferedImage face = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_BGR);
        face.setData(img.getData());// ��ʱͼ��ʹ��ԭͼ���е�����
        // �ɼ�ͼ����Ϣ
        ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(face);
        // ������Ϣ�б�
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        // ��ͼ����Ϣ�вɼ�������Ϣ
        faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
                imageInfo.getImageFormat(), faceInfoList);

        if (faceInfoList.isEmpty()) {// ���������Ϣ�ǿյ�
            return null;
        }
        // ��������
        FaceFeature faceFeature = new FaceFeature();
        // ��������Ϣ�вɼ���������
        faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
                imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
        return faceFeature;// �ɼ�֮�����������
    }

    /**
     * ���������沿����
     */
    public static void loadAllFaceFeature() {
        // ��ȡ��������ͼƬ��Ӧ�������뼯��
        Set<String> keys = Session.IMAGE_MAP.keySet();
        for (String code : keys) {// ��������������
            // ȡ��һ������ͼƬ
            BufferedImage image = Session.IMAGE_MAP.get(code);
            // ��ȡ������ͼƬ��������������
            FaceFeature faceFeature = getFaceFeature(image);
            // �������������󱣴���ȫ�ֻỰ��
            Session.FACE_FEATURE_MAP.put(code, faceFeature);
        }
    }

    /**
     * �������������м������
     * 
     * @param targetFaceFeature Ŀ����������
     * @return ������������Ӧ��������
     */
    public static String detectFace(FaceFeature targetFaceFeature) {
        if (targetFaceFeature == null) {
            return null;
        }
        // ��ȡ��������������Ӧ�������뼯��
        Set<String> keys = Session.FACE_FEATURE_MAP.keySet();
        float score = 0;// ƥ����ߵ÷�
        String resultCode = null;// ���ֶ�Ӧ��������
        for (String code : keys) {// ��������������
            // ȡ��һ��������������
            FaceFeature sourceFaceFeature = Session.FACE_FEATURE_MAP.get(code);
            // �����ȶԶ���
            FaceSimilar faceSimilar = new FaceSimilar();
            // �Ա�Ŀ������������ȡ������������
            faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
            if (faceSimilar.getScore() > score) {// ����÷ִ��ڵ�ǰ��ߵ÷�
                score = faceSimilar.getScore();// ���¼�¼��ǰ��ߵ÷�
                resultCode = code;// ��¼��ߵ÷ֵ�������
            }
        }
        if (score > 0.9) {// �����ߵ÷ִ���0.9������Ϊ�ҵ�ƥ������
            return resultCode;// ����������Ӧ��������
        }
        return null;
    }

    /**
     * �ͷ���Դ
     */
    public static void dispost() {
        faceEngine.unInit();// ����ж��
    }
}
