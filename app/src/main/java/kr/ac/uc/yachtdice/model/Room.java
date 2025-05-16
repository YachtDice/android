package kr.ac.uc.yachtdice.model;

public class Room {
    private String code;
    private boolean started;

    public Room(String code) {
        this.code = code;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public boolean isStarted() { return started; }
    public void setStarted(boolean started) { this.started = started; }
}
