package com.mr.clock.pojo;

/**
 * ��˾��Ϣʱ��
 * 
 * @author mingrisoft
 *
 */
public class WorkTime {
    private String start;// �ϰ�ʱ��
    private String end;// �°�ʱ��

    public WorkTime() {
        super();
    }

    public WorkTime(String start, String end) {
        super();
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "WorkTime [start=" + start + ", end=" + end + "]";
    }
}
