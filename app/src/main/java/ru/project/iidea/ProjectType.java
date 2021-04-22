package ru.project.iidea;

import androidx.annotation.NonNull;

public enum ProjectType {
    IT,
    History,
    Business,
    Culture;


    @NonNull
    @Override
    public String toString() {
        switch (this){
            case IT:
                return "IT";
            case Culture:
                return "Culture";
            case History:
                return "History";
            case Business:
                return "Business";
        }
        return "";
    }
}
