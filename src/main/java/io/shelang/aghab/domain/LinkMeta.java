package io.shelang.aghab.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "link_meta")
public class LinkMeta {

  @Id @GeneratedValue private Long id;
  private String title;
  private String description;

  @CreationTimestamp
  @Column(name = "create_at")
  private Instant createAt;

  @UpdateTimestamp
  @Column(name = "update_at")
  private Instant updateAt;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "link_id")
  private Links links;

  public LinkMeta() {}

  public Long getId() {
    return id;
  }

  public LinkMeta setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public LinkMeta setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public LinkMeta setDescription(String description) {
    this.description = description;
    return this;
  }

  public Instant getCreateAt() {
    return createAt;
  }

  public LinkMeta setCreateAt(Instant createAt) {
    this.createAt = createAt;
    return this;
  }

  public Instant getUpdateAt() {
    return updateAt;
  }

  public LinkMeta setUpdateAt(Instant updateAt) {
    this.updateAt = updateAt;
    return this;
  }

  public Links getLinks() {
    return links;
  }

  public LinkMeta setLinks(Links links) {
    this.links = links;
    return this;
  }
}
