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
                return "New";
            case Finished:
                return "Finished";
            case InProgress:
                return "InProgress";
            case Search:
                return "Search";
        }
        return "";
    }
}
