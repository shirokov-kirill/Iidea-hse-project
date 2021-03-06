package ru.project.iidea;

import androidx.annotation.NonNull;

public enum UserState {
    SEEKING,
    NOT_SEEKING,
    WORKING;
    //TODO


    @NonNull
    @Override
    public String toString() {
        switch (this){
            case SEEKING:
                return "В поиске";
            case WORKING:
                return "Работаю";
            case NOT_SEEKING:
                return "Не ищу";
        }
        return "";
    }

    public String toServerString() {
        switch (this){
            case SEEKING:
                return "SEEKING";
            case WORKING:
                return "WORKING";
            case NOT_SEEKING:
                return "NOT_SEEKING";
        }
        return "";
    }

    public static UserState fromString(String s){
        switch (s){
            case "SEEKING":
                return SEEKING;
            case "WORKING":
                return WORKING;
            case "NOT_SEEKING":
                return NOT_SEEKING;
        }
        return SEEKING;
    }
}
