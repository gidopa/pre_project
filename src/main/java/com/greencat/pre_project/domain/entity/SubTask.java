package com.greencat.pre_project.domain.entity;

import static lombok.AccessLevel.PROTECTED;

import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Builder
@SQLRestriction("is_delete is false")
@Table(name = "subtask")
public class SubTask extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "subtask_id")
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "todo_id", nullable = false)
  private Todo todo;

  @NotBlank
  @Column(nullable = false, length = 50, name = "subtask_title")
  private String title;

  @Column(name = "subtask_description")
  private String description;

  @Column(name = "subtask_status")
  @Enumerated(EnumType.STRING)
  private TodoStatus status;

  private void addSubTask(Todo todo) {
    this.todo = todo;
    todo.getSubTasks().add(this);
  }
}
