package io.shelang.aghab.domain;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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

    @Column(name = "need_change_password")
    private boolean needChangePassword;

    @Builder.Default
    @Column(name = "role")
    private String role = "USER";

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "script_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "script_id") })
    Set<Script> scripts = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "webhook_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "webhook_id") })
    Set<Webhook> webhooks = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "workspace_user", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "workspace_id") })
    Set<Workspace> workspaces = new HashSet<>();
}
