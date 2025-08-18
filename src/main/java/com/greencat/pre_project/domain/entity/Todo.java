package com.greencat.pre_project.domain.entity;

import static lombok.AccessLevel.*;

import com.greencat.pre_project.application.dto.todo.TodoCreateRequestDto;
import com.greencat.pre_project.application.dto.todo.TodoUpdateRequestDto;
import com.greencat.pre_project.domain.enums.TodoPriority;
import com.greencat.pre_project.domain.enums.TodoStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
@SQLRestriction("is_deleted is false") // soft delete 정책으로 삭제되지 않은 것만 검색
public class Todo extends BaseEntity {

  @Id @GeneratedValue
  @Column(name = "todo_id")
  private UUID id;

  @NotBlank
  @Column(nullable = false, length = 50, name = "todo_title")
  private String title;

  @Column(name = "todo_description")
  private String description;

  @Column(name = "todo_status")
  @Enumerated(EnumType.STRING)
  private TodoStatus status;

  @Column(name = "todo_priority")
  @Enumerated(EnumType.STRING)
  private TodoPriority priority;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id") //, nullable = false
  private Users user;

  @OneToMany(mappedBy = "todo", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SubTask> subTasks = new ArrayList<>();

  private LocalDateTime dueTime;

  public void updateTodo(TodoUpdateRequestDto request) {
    if(request.getDescription() != null) {
      this.description = request.getDescription();
    }

    if(request.getStatus() != null) {
      this.status = request.getStatus();
    }

    if(request.getPriority() != null) {
      this.priority = request.getPriority();
    }

    if(request.getDueTime() != null) {
      this.dueTime = request.getDueTime();
    }

    if(request.getTitle() != null) {
      this.title = request.getTitle();
    }
  }

  public void softDelete(Todo todo) {
    this.isDeleted = true;
  }

  public static Todo create(TodoCreateRequestDto requestDto, Users user){
    return Todo.builder()
        .priority(requestDto.getPriority())
        .title(requestDto.getTitle())
        .description(requestDto.getDescription())
        .status(requestDto.getStatus())
        .dueTime(requestDto.getDueTime())
        .user(user)
        .build();
  }

}
