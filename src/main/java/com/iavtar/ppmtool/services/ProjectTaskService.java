package com.iavtar.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iavtar.ppmtool.domain.Backlog;
import com.iavtar.ppmtool.domain.ProjectTask;
import com.iavtar.ppmtool.repositories.BacklogRepository;
import com.iavtar.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

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
	}

}
