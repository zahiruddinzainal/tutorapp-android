package zvhir.dev.tutormy;

import com.google.firebase.database.Exclude;

public class Upload {

    private String mName;
    private String mNamaTutor;
    private String mPhoneTutor;
    private String mHarga;
    private String mImageUrl;
    private String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String namaTutor, String phoneTutor, String name, String harga, String imageUrl) {
        if (name.trim().equals("")) {
            name = "Tiada data";
        }

        mNamaTutor = namaTutor;
        mPhoneTutor = phoneTutor;
        mName = name;
        mHarga = harga;
        mImageUrl = imageUrl;
    }


    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getNamaTutor() {
        return mNamaTutor;
    }

    public void setNamaTutor(String namaTutor) {
        mNamaTutor = namaTutor;
    }

    public String getPhoneTutor() {
        return mPhoneTutor;
    }

    public void setPhoneTutor(String phoneTutor) {
        mPhoneTutor = phoneTutor;
    }





    public String getHarga() {

        return mHarga;
    }

    public void setHarga(String harga) {
        mHarga = harga;
    }

    public String getImageUrl() {

        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {

        mImageUrl = imageUrl;
    }

    @Exclude
    public String getKey() {

        return mKey;
    }

    @Exclude
    public void setKey(String key) {
        mKey = key;
    }
}