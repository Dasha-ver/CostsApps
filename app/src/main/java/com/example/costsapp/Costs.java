package com.example.costsapp;

class Costs {

    private String name;
    private int avatarId;
    private String manats;
    private boolean checkEnter;

    Costs(String name, int avatarId) {
        setName(name);
        setAvatarId(avatarId);
        setManats(manats);
        checkEnter = false;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getManats() {
        return manats;
    }

    void setManats(String manats) {
        this.manats = manats;
    }

    int getAvatarId() {
        return avatarId;
    }

    void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }


}