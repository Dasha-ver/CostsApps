package com.example.costsapp;

//класс данных для спиннера в расчёте расходов

public class ThirdPageSpinnerInfo {
    private String categoryName;
    private int categoryPicture;

    public ThirdPageSpinnerInfo(String cName, int categoryImage)
    {
        categoryName = cName;
        categoryPicture = categoryImage;
    }
    public String getCategoryName()
    {
        return categoryName;
    }

    public int getCategoryPicture()
    {
        return categoryPicture;
    }

    public String toString()
    {
        return categoryName;
    }
}
