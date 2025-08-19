package com.greencat.pre_project.infrastructure.repository;

import com.greencat.pre_project.domain.entity.SubTask;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepository extends JpaRepository<SubTask, UUID> {

}
