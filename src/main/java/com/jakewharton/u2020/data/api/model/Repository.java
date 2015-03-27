package com.jakewharton.u2020.data.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.gson.annotations.SerializedName;
import org.joda.time.DateTime;

import static com.jakewharton.u2020.util.Preconditions.checkNotNull;

public final class Repository {
  @NonNull public final String name;
  @NonNull public final User owner;
  @Nullable public final String description;

  @SerializedName("watchers")
  public final long stars;
  public final long forks;

  @SerializedName("html_url")
  public final String htmlUrl;

  @SerializedName("updated_at")
  public final DateTime updatedAt;

  private Repository(Builder builder) {
    this.name = checkNotNull(builder.name, "name == null");
    this.owner = checkNotNull(builder.owner, "owner == null");
    this.description = builder.description;
    this.stars = builder.stars;
    this.forks = builder.forks;
    this.htmlUrl = checkNotNull(builder.htmlUrl, "htmlUrl == null");
    this.updatedAt = checkNotNull(builder.updatedAt, "updatedAt == null");
  }

  @Override public String toString() {
    return "Repository{" +
        "name='" + name + '\'' +
        ", owner=" + owner +
        ", description='" + description + '\'' +
        ", stars=" + stars +
        ", forks=" + forks +
        ", htmlUrl='" + htmlUrl + '\'' +
        ", updatedAt=" + updatedAt +
        '}';
  }

  public static final class Builder {
    private String name;
    private User owner;
    private String description;
    private long stars;
    private long forks;
    private String htmlUrl;
    private DateTime updatedAt;

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder owner(User owner) {
      this.owner = owner;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder stars(long stars) {
      this.stars = stars;
      return this;
    }

    public Builder forks(long forks) {
      this.forks = forks;
      return this;
    }

    public Builder htmlUrl(String htmlUrl) {
      this.htmlUrl = htmlUrl;
      return this;
    }

    public Builder updatedAt(DateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Repository build() {
      return new Repository(this);
    }
  }
}
