package com.greencat.pre_project.application.service;

import com.greencat.pre_project.application.dto.subtask.SubtaskCreateRequest;
import com.greencat.pre_project.application.dto.subtask.SubtaskResponse;
import com.greencat.pre_project.application.dto.subtask.SubtaskUpdateRequest;
import com.greencat.pre_project.domain.entity.SubTask;
import com.greencat.pre_project.domain.entity.Todo;
import com.greencat.pre_project.exception.error_code.SubtaskErrorCode;
import com.greencat.pre_project.exception.error_code.TodoErrorCode;
import com.greencat.pre_project.exception.exception.PreTaskException;
import com.greencat.pre_project.infrastructure.repository.SubtaskRepository;
import com.greencat.pre_project.infrastructure.repository.TodoRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubtaskService {

  private final SubtaskRepository subtaskRepository;
  private final TodoRepository todoRepository;
  private final CacheManager cm;


  public SubtaskResponse createSubtask(SubtaskCreateRequest request, String username) {
    UUID todoId = request.getTodoId();
    Todo todo = todoRepository.findById(todoId)
        .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
    SubTask subTask = SubTask.create(request,todo);
    todo.addSubTask(subTask);
    SubTask savedTask = subtaskRepository.save(subTask);
    cm.getCache("todoCache").evict(todoId);
    cm.getCache("todoListCache").evict(username);
    return SubtaskResponse.changeEntityToResponse(savedTask);
  }

  public SubtaskResponse updateSubtask(UUID subtaskId, SubtaskUpdateRequest request, String username) {
    SubTask subTask = subtaskRepository.findById(subtaskId)
        .orElseThrow(() -> new PreTaskException(SubtaskErrorCode.NOT_FOUND));
    if (subTask.getTodo().getId() != null) {
      Todo todo = todoRepository.findById(subTask.getTodo().getId())
          .orElseThrow(() -> new PreTaskException(TodoErrorCode.NOT_FOUND));
      subTask.updateSubtask(request, todo);
      cm.getCache("todoCache").evict(todo.getId());
    }else{
      subTask.updateSubtask(request, null);
    }
    cm.getCache("todoListCache").evict(username);

    return SubtaskResponse.changeEntityToResponse(subTask);
  }

  public void softDelete(UUID subtaskId, String username) {
    SubTask subTask = subtaskRepository.findById(subtaskId)
        .orElseThrow(() -> new PreTaskException(SubtaskErrorCode.NOT_FOUND));
    subTask.softDelete();
    Todo todo = subTask.getTodo();

    cm.getCache("todoCache").evict(todo.getId());
    cm.getCache("todoListCache").evict(username);
  }

  public SubtaskResponse getOneSubTask(UUID subtaskId) {
    SubTask subTask = subtaskRepository.findById(subtaskId)
        .orElseThrow(() -> new PreTaskException(SubtaskErrorCode.NOT_FOUND));
    return SubtaskResponse.changeEntityToResponse(subTask);
  }
}
