package io.shelang.aghab.domain;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "workspaces")
public class Workspace implements Serializable {

  @Id
  @GeneratedValue(generator = "workspaces_id_gen", strategy = GenerationType.SEQUENCE)
  @SequenceGenerator(name = "workspaces_id_gen", allocationSize = 1, sequenceName = "workspaces_id_seq")
  private Long id;

  @Column(length = 50)
  private String name;

  @Column(name = "creator_user_id")
  private Long creatorUserId;

}
