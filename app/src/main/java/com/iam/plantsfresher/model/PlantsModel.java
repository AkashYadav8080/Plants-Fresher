package com.iam.plantsfresher.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class PlantsModel implements Parcelable {
    private String id;
    private String name;
    private String category;
    private String description;
    private List<String> careInstruction;
    private double realPrice;
    private double offeredPrice;
    private int quantity;
    private String imageUrl;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PlantsModel() {
    }

    public PlantsModel(String id, String name, String category, String description, List<String> careInstruction, double realPrice, double offeredPrice, int quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.careInstruction = careInstruction;
        this.realPrice = realPrice;
        this.offeredPrice = offeredPrice;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getCareInstruction() {
        return careInstruction;
    }

    public void setCareInstruction(List<String> careInstruction) {
        this.careInstruction = careInstruction;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public double getOfferedPrice() {
        return offeredPrice;
    }

    public void setOfferedPrice(double offeredPrice) {
        this.offeredPrice = offeredPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    // Add this constructor
    protected PlantsModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readString();
        description = in.readString();
        careInstruction = in.createStringArrayList();
        realPrice = in.readDouble();
        offeredPrice = in.readDouble();
        quantity = in.readInt();
        imageUrl = in.readString();
    }

    // Add these methods
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(description);
        dest.writeStringList(careInstruction);
        dest.writeDouble(realPrice);
        dest.writeDouble(offeredPrice);
        dest.writeInt(quantity);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlantsModel> CREATOR = new Creator<PlantsModel>() {
        @Override
        public PlantsModel createFromParcel(Parcel in) {
            return new PlantsModel(in);
        }

        @Override
        public PlantsModel[] newArray(int size) {
            return new PlantsModel[size];
        }
    };
}