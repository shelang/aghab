package io.shelang.aghab.domain;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class LinkMeta {

  @Id
  @GeneratedValue
  private Long id;
  private Long linkId;
  private String title;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;

  @OneToOne(mappedBy = "linkMeta")
  private Links links;

  public Long getId() {
    return id;
  }

  public LinkMeta setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getLinkId() {
    return linkId;
  }

  public LinkMeta setLinkId(Long linkId) {
    this.linkId = linkId;
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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public LinkMeta setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public LinkMeta setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
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
