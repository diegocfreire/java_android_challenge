package com.example.list_app.data.entities;

import com.example.list_app.common.time.LocalDate;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class Commits{
    public String href;
}

class Head{
    public String label;
    public String ref;
    public String sha;
    public User user;
    public Repo repo;
}

class Html{
    public String href;
}

class Issue{
    public String href;
}

class Links{
    public Self self;
    public Html html;
    public Issue issue;
    public Comments comments;
    public ReviewComments review_comments;
    public ReviewComment review_comment;
    public Commits commits;
    public Statuses statuses;
}

class Repo{
    public int id;
    public String node_id;
    public String name;
    public String full_name;
    @SerializedName("private")
    public boolean myprivate;
    public Owner owner;
    public String html_url;
    public String description;
    public boolean fork;
    public String url;
    public String forks_url;
    public String keys_url;
    public String collaborators_url;
    public String teams_url;
    public String hooks_url;
    public String issue_events_url;
    public String events_url;
    public String assignees_url;
    public String branches_url;
    public String tags_url;
    public String blobs_url;
    public String git_tags_url;
    public String git_refs_url;
    public String trees_url;
    public String statuses_url;
    public String languages_url;
    public String stargazers_url;
    public String contributors_url;
    public String subscribers_url;
    public String subscription_url;
    public String commits_url;
    public String git_commits_url;
    public String comments_url;
    public String issue_comment_url;
    public String contents_url;
    public String compare_url;
    public String merges_url;
    public String archive_url;
    public String downloads_url;
    public String issues_url;
    public String pulls_url;
    public String milestones_url;
    public String notifications_url;
    public String labels_url;
    public String releases_url;
    public String deployments_url;
    public LocalDate created_at;
    public LocalDate updated_at;
    public LocalDate pushed_at;
    public String git_url;
    public String ssh_url;
    public String clone_url;
    public String svn_url;
    public String homepage;
    public int size;
    public int stargazers_count;
    public int watchers_count;
    public String language;
    public boolean has_issues;
    public boolean has_projects;
    public boolean has_downloads;
    public boolean has_wiki;
    public boolean has_pages;
    public int forks_count;
    public Object mirror_url;
    public boolean archived;
    public boolean disabled;
    public int open_issues_count;
    public License license;
    public boolean allow_forking;
    public boolean is_template;
    public boolean web_commit_signoff_required;
    public ArrayList<String> topics;
    public String visibility;
    public int forks;
    public int open_issues;
    public int watchers;
    public String default_branch;
}

class ReviewComment{
    public String href;
}

class ReviewComments{
    public String href;
}

public class PullRequests {
    public String url;
    public int id;
    public String node_id;
    public String html_url;
    public String diff_url;
    public String patch_url;
    public String issue_url;
    public int number;
    public String state;
    public boolean locked;
    public String title;
    public User user;
    public String body;
    public LocalDate created_at;
    public LocalDate updated_at;
    public Object closed_at;
    public Object merged_at;
    public String merge_commit_sha;
    public Object assignee;
    public ArrayList<Object> assignees;
    public ArrayList<Object> requested_reviewers;
    public ArrayList<Object> requested_teams;
    public ArrayList<Object> labels;
    public Object milestone;
    public boolean draft;
    public String commits_url;
    public String review_comments_url;
    public String review_comment_url;
    public String comments_url;
    public String statuses_url;
    public Head head;
    public Base base;
    public Links _links;
    public String author_association;
    public Object auto_merge;
    public Object active_lock_reason;
}

class Self{
    public String href;
}

class Statuses{
    public String href;
}

class User{
    public String login;
    public int id;
    public String node_id;
    public String avatar_url;
    public String gravatar_id;
    public String url;
    public String html_url;
    public String followers_url;
    public String following_url;
    public String gists_url;
    public String starred_url;
    public String subscriptions_url;
    public String organizations_url;
    public String repos_url;
    public String events_url;
    public String received_events_url;
    public String type;
    public boolean site_admin;
}

