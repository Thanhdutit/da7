package com.example.doan7;

public class History {
    private String id; // Thêm thuộc tính id
    private String name;
    private int imageId;

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    private String lop;

    public History() {
    }

    public History(String id, String name, int imageId, String lop) {
        this.id = id;
        this.name = name;
        this.imageId = imageId;
        this.lop = lop;
    }

    // Getter và Setter cho id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
