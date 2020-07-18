package com.iavtar.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iavtar.ppmtool.domain.Backlog;
import com.iavtar.ppmtool.domain.Project;
import com.iavtar.ppmtool.domain.ProjectTask;
import com.iavtar.ppmtool.exceptions.ProjectNotFoundException;
import com.iavtar.ppmtool.repositories.BacklogRepository;
import com.iavtar.ppmtool.repositories.ProjectRepository;
import com.iavtar.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

		try {
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			projectTask.setBacklog(backlog);

			Integer backlogSequence = backlog.getPTSequence();
			backlogSequence++;

			backlog.setPTSequence(backlogSequence);

			projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + backlogSequence);

			projectTask.setProjectIdentifier(backlog.getProjectIdentifier());

			if (projectTask.getPriority() == null) {
				projectTask.setPriority(3);
			}

			if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("To-Do");
			}

			return projectTaskRepository.save(projectTask);

		} catch (Exception e) {
			throw new ProjectNotFoundException("Project with " + projectIdentifier + " not found");
		}

	}

	public Iterable<ProjectTask> findBacklogById(String backlogId) {

		Project project = projectRepository.findByProjectIdentifier(backlogId);
		
		if(project == null) {
			throw new ProjectNotFoundException("Project with ID: " + backlogId + " does not exist");
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlogId);
	}
	
	public ProjectTask findPTByProjectSequence(String backlogId, String ptId) {
		
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlogId);
		
		if(backlog == null) {
			throw new ProjectNotFoundException("Project with ID: " + backlogId + " does not exist");
		}
		
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
		
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task " + ptId + " does not found");
		}
		
		if(!projectTask.getProjectIdentifier().equalsIgnoreCase(backlogId)) {
			throw new ProjectNotFoundException("Project Task " + ptId + " does not exist in project " + backlogId);
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlogId, String ptId) {
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(ptId);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
	}

}
