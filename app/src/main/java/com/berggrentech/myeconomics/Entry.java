package com.berggrentech.myeconomics;

import java.util.Date;

/**
 * Created by Simon on 2016-09-16.
 */
public class Entry {
    int mID;
    String mTitle;
    Date mDate;
    int mSum;
    String mCategory;
    String mType;

    public Entry(int _ID, String _Title, Date _Date, int _Sum, String _Category, String _Type) {
        mID  = _ID;
        mTitle = _Title;
        mDate = _Date;
        mSum = _Sum;
        mCategory = _Category;
        mType = _Type;
    }
    public Entry(String _Title, Date _Date, int _Sum, String _Category, String _Type) {
        this(0, _Title, _Date, _Sum, _Category, _Type);
    }

    public String getTitle() { return mTitle; }
    public Date getDate() { return mDate; }
    public int getSum() { return mSum; }
    public String getCategory() { return mCategory; }
    public String getType() { return mType; }
    public int getID() {
        return mID;
    }

    public void setTitle(String _Title) {
        mTitle = _Title;
    }

    public void setDate(Date _Date) {
        mDate = _Date;
    }

    public void setSum(int _Sum) {
        mSum = _Sum;
    }

    public void setCategory(String _Category) {
        mCategory = _Category;
    }

    public void setType(String _Type) {
        mType = _Type;
    }

    int getCategoryImageID() {
        if(mCategory.equalsIgnoreCase("transport")) return R.drawable.category_transport_icon;
        else if (mCategory.equalsIgnoreCase("entertainment")) return R.drawable.category_entertainment_icon;
        else if (mCategory.equalsIgnoreCase("food")) return R.drawable.category_food_icon;
        else if (mCategory.equalsIgnoreCase("household")) return R.drawable.category_household_icon;
        else if (mCategory.equalsIgnoreCase("shopping")) return R.drawable.category_shopping_icon;
        else if (mCategory.equalsIgnoreCase("salary")) return R.drawable.category_income_other_icon;
        else if (mCategory.equalsIgnoreCase("savings")) return R.drawable.category_savings_icon;
        else if (mCategory.equalsIgnoreCase("other")) return R.drawable.category_expense_other_icon;
        else return 0;
    }

    int getTypeImageID() {
        if(mType.equalsIgnoreCase("income")) return R.drawable.type_income_icon;
        else if(mType.equalsIgnoreCase("expense")) return R.drawable.type_expense_icon;
        return 0;
    }
}
