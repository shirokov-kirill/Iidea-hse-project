package ru.project.iidea.network;

import java.util.List;

public class ProjectSearchRequest {

    private final String query;
    private final List<String> tags;
    private final List<String> excludedTags;

    public ProjectSearchRequest(String query, List<String> tags, List<String> excludedTags) {
        this.query = query;
        this.tags = tags;
        this.excludedTags = excludedTags;
    }

    public String getQuery() {
        return query;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getExcludedTags() {
        return excludedTags;
    }
}
