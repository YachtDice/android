package kr.ac.uc.yachtdice.model;

public class PlayerInfo {
    private String name;
    private boolean ready;

    public PlayerInfo(String name) {
        this.name = name;
        this.ready = false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isReady() { return ready; }
    public void setReady(boolean ready) { this.ready = ready; }
}