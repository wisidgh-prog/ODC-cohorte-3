package com.example.Dashboard.Service;

import com.example.Dashboard.Dto.TaskRequestDTO;
import com.example.Dashboard.Dto.TaskResponseDTO;
import com.example.Dashboard.Dto.TaskUpdateRequestDTO;
import com.example.Dashboard.Execption.ResourceNotFoundException;
import com.example.Dashboard.Mapper.TaskMapper;
import com.example.Dashboard.Model.Board;
import com.example.Dashboard.Model.Task;
import com.example.Dashboard.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Couche Service pour la gestion des taches (Task).
 *
 * Utilise TaskMapper pour convertir les DTOs vers les entites JPA et inversement.
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final BoardService boardService;
    private final TaskMapper taskMapper;

    /**
     * Creer une nouvelle tache dans un tableau.
     */
    public TaskResponseDTO createTask(Long boardId, TaskRequestDTO requestDTO) {
        Board board = boardService.findBoardEntityById(boardId);

        Task task = taskMapper.toEntity(requestDTO);
        task.setBoard(board);

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDTO(savedTask);
    }

    /**
     * Recuperer toutes les taches d'un tableau sous forme de DTOs.
     */
    public List<TaskResponseDTO> getTasksByBoard(Long boardId) {
        boardService.findBoardEntityById(boardId);

        return taskRepository.findByBoardId(boardId)
                .stream()
                .map(taskMapper::toResponseDTO)
                .toList();
    }

    /**
     * Recuperer une tache par son identifiant sous forme de DTO.
     */
    public TaskResponseDTO getTaskById(Long id) {
        Task task = findTaskEntityById(id);
        return taskMapper.toResponseDTO(task);
    }

    /**
     * Mettre a jour une tache (titre, description, statut).
     */
    public TaskResponseDTO updateTask(Long id, TaskUpdateRequestDTO requestDTO) {
        Task task = findTaskEntityById(id);
        taskMapper.updateEntityFromDTO(task, requestDTO);

        Task updatedTask = taskRepository.save(task);
        return taskMapper.toResponseDTO(updatedTask);
    }

    /**
     * Supprimer une tache par son identifiant.
     */
    public void deleteTask(Long id) {
        Task task = findTaskEntityById(id);
        taskRepository.delete(task);
    }

    /**
     * Methode utilitaire interne pour recuperer l'entite Task.
     */
    public Task findTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tache introuvable avec id=" + id));
    }
}
