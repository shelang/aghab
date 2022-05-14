package io.shelang.aghab.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(generator = "users_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "users_id_gen", allocationSize = 1, sequenceName = "users_id_seq")
  private Long id;

  private String username;

  private String password;

  private String token;

  @Column(name = "token_issue_at")
  private Instant tokenIssueAt;

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "script_user",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "script_id")})
  Set<Script> scripts = new HashSet<>();

  @Builder.Default
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "webhook_user",
      joinColumns = {@JoinColumn(name = "user_id")},
      inverseJoinColumns = {@JoinColumn(name = "webhook_id")})
  Set<Webhook> webhooks = new HashSet<>();
}
