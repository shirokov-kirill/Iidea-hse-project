package ru.project.iidea;

import androidx.annotation.NonNull;

public enum ProjectState {
    New,
    Finished,
    InProgress,
    Search;

    @NonNull
    @Override
    public String toString() {
        switch (this) {
            case New:
                return "IT";
            case Finished:
                return "Finished";
            case InProgress:
                return "In Progress";
            case Search:
                return "Search";
        }
        return "";
    }
}
