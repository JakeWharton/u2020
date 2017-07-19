package com.jakewharton.u2020.data.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import org.threeten.bp.Instant;

import static com.jakewharton.u2020.util.Preconditions.checkNotNull;

public final class Repository {
  @NonNull public final String name;
  @NonNull public final User owner;
  @Nullable public final String description;

  public final long watchers;
  public final long forks;

  public final String html_url;

  public final Instant updated_at;

  private Repository(Builder builder) {
    this.name = checkNotNull(builder.name, "name == null");
    this.owner = checkNotNull(builder.owner, "owner == null");
    this.description = builder.description;
    this.watchers = builder.stars;
    this.forks = builder.forks;
    this.html_url = checkNotNull(builder.htmlUrl, "html_url == null");
    this.updated_at = checkNotNull(builder.updatedAt, "updated_at == null");
  }

  @Override public String toString() {
    return "Repository{" +
        "name='" + name + '\'' +
        ", owner=" + owner +
        ", description='" + description + '\'' +
        ", watchers=" + watchers +
        ", forks=" + forks +
        ", html_url='" + html_url + '\'' +
        ", updated_at=" + updated_at +
        '}';
  }

  public static final class Builder {
    private String name;
    private User owner;
    private String description;
    private long stars;
    private long forks;
    private String htmlUrl;
    private Instant updatedAt;

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

    public Builder updatedAt(Instant updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Repository build() {
      return new Repository(this);
    }
  }
}
