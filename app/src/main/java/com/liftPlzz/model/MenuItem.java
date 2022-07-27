package com.liftPlzz.model;

public class MenuItem {
    public MenuItem() {
    }

    public MenuItem(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    int id;
    String title;
    int icon;

    public MenuItem(int id, String title, int icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
