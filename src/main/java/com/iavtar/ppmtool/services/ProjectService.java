package com.iavtar.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iavtar.ppmtool.domain.Project;
import com.iavtar.ppmtool.exceptions.ProjectIdException;
import com.iavtar.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	public Project saveOrUpdateProject(Project project) {

		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException(
					"Project Id " + project.getProjectIdentifier().toUpperCase() + " already exists");
		}

	}

	public Project findProjectByIdentfier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId);

		if (project == null) {
			throw new ProjectIdException("Project ID " + projectId.toUpperCase() + " does not exists");
		}

		return project;
	}

	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}

	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId);

		if (project == null) {
			throw new ProjectIdException("Cannot find project with ID " + projectId + " . This project does not exist");
		}

		projectRepository.delete(project);
	}

}
