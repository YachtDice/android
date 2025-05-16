package kr.ac.uc.yachtdice.model;

public class RoomStatus {
    private String code;
    private int count;

    public RoomStatus(String code, int count) {
        this.code = code;
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public int getCount() {
        return count;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
