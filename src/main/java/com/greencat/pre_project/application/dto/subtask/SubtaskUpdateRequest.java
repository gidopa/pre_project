package com.greencat.pre_project.application.dto.subtask;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubtaskUpdateRequest {

  private UUID todoId;
  private String title;
  private String description;
}
