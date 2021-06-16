package ru.project.iidea;

import androidx.annotation.NonNull;

import java.io.Serializable;

public enum ProjectType implements Serializable {
    IT,
    History,
    Business,
    Culture,
    Art,
    Politics,
    Archaeology,
    Paleontology,
    Investments,
    Education,
    Economy,
    Sport,
    Supplies,
    Resale,
    Clothes,
    Jewelery,
    Fashion,
    Music;


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
            case Art:
                return "Art";
            case Music:
                return "Music";
            case Sport:
                return "Sport";
            case Resale:
                return "Resale";
            case Clothes:
                return "Clothes";
            case Economy:
                return "Economy";
            case Fashion:
                return "Fashion";
            case Jewelery:
                return "Jewelery";
            case Politics:
                return "Politics";
            case Supplies:
                return "Supplies";
            case Education:
                return "Education";
            case Archaeology:
                return "Archaeology";
            case Investments:
                return "Investments";
            case Paleontology:
                return "Paleontology";
        }
        return "";
    }
}
