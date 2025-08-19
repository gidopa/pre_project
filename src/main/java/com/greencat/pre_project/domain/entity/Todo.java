package com.greencat.pre_project.domain.entity;

import static lombok.AccessLevel.PROTECTED;

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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLRestriction;

@Slf4j
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

  public void updateTodoStatus(TodoStatus updateStatus) {
    if(updateStatus == TodoStatus.DONE) {
      this.status = TodoStatus.DONE;
      this.getSubTasks().forEach(subTask -> {
        subTask.updateSubtaskStatus(TodoStatus.DONE);});
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

  public void addSubTask(SubTask subTask) {
    this.subTasks.add(subTask);
    subTask.setTodo(this);
    log.info("add sub task 호출");
  }

  public void removeSubTask(SubTask subTask) {
    this.subTasks.remove(subTask);
    subTask.setTodo(null);
  }

}
