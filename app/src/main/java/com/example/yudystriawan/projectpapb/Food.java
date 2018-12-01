package com.example.yudystriawan.projectpapb;

public class Food {
    private String desc;
    private int image;
    private int button;

    public Food(String desc, int image, int button) {
        this.desc = desc;
        this.image = image;
        this.button = button;
    }

    public String getDesc() {
        return desc;
    }

    public int getImage() {
        return image;
    }

    public int getButton() {
        return button;
    }
}
