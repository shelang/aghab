package io.shelang.aghab.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
