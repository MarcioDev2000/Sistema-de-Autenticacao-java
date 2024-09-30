package com.uevocola.com.uevocola.repositories;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uevocola.com.uevocola.models.TaskModel;

@Repository
public interface TaskRepository extends JpaRepository<TaskModel, UUID>{
}
