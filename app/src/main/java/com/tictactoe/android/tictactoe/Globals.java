package com.tictactoe.android.tictactoe;

public class Globals {

    private static Globals instance = new Globals();

    public static Globals getInstance() {
        return instance;
    }

    public static void setInstance(Globals instance) {
        Globals.instance = instance;
    }

    private String notification_index;

    private Globals() {

    }

    public String getValue() {
        return notification_index;
    }

    public void setValue(String notification_index) {
        this.notification_index = notification_index;
    }
}