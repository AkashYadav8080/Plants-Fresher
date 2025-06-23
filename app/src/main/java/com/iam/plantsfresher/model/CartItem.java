package com.iam.plantsfresher.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartItem implements Parcelable {
    private PlantsModel plant;
    private int quantity;

    public CartItem(PlantsModel plant, int quantity) {
        this.plant = plant;
        this.quantity = quantity;
    }

    public PlantsModel getPlant() {
        return plant;
    }

    public void setPlant(PlantsModel plant) {
        this.plant = plant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return plant.getOfferedPrice() * quantity;
    }

    protected CartItem(Parcel in) {
        plant = in.readParcelable(PlantsModel.class.getClassLoader());
        quantity = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(plant, flags);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}