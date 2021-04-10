package io.shelang.aghab.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Links {

  @Id
  @GeneratedValue
  private Long id;
  private String hash;
  private String alias;
  private String url;
  private Integer status;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "link_id", referencedColumnName = "id")
  private LinkMeta linkMeta;

  public Long getId() {
    return id;
  }

  public Links setId(Long id) {
    this.id = id;
    return this;
  }

  public String getHash() {
    return hash;
  }

  public Links setHash(String hash) {
    this.hash = hash;
    return this;
  }

  public String getAlias() {
    return alias;
  }

  public Links setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Links setUrl(String url) {
    this.url = url;
    return this;
  }

  public Integer getStatus() {
    return status;
  }

  public Links setStatus(Integer status) {
    this.status = status;
    return this;
  }

  public LinkMeta getLinkMeta() {
    return linkMeta;
  }

  public Links setLinkMeta(LinkMeta linkMeta) {
    this.linkMeta = linkMeta;
    return this;
  }
}
