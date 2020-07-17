package com.iavtar.ppmtool.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.ppmtool.domain.Project;
import com.iavtar.ppmtool.services.MapValidationErrorService;
import com.iavtar.ppmtool.services.ProjectService;

@RestController
@RequestMapping("/api/project")
@CrossOrigin("*")
public class ProjectController {

	@Autowired
	private ProjectService projectservice;
	
	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@PostMapping("/")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result) {
		
		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
		
		if(!(errorMap == null)) {
			return errorMap;
		}
		
		Project newProject = projectservice.saveOrUpdateProject(project);
		return new ResponseEntity<Project>(newProject, HttpStatus.CREATED);
	}
	
	@GetMapping("/{projectId}")
	public ResponseEntity<?> getProjectById(@PathVariable("projectId") String projectId){
		
		Project project = projectservice.findProjectByIdentfier(projectId.toUpperCase());
		
		return new ResponseEntity<Project>(project, HttpStatus.OK);
		
	}
	
	@GetMapping("/all")
	public Iterable<Project> getAllProjects(){
		return projectservice.findAllProjects();
	}
	
	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable("projectId") String projectId){
		projectservice.deleteProjectByIdentifier(projectId);
		
		return new ResponseEntity<String>("Project with Id " + projectId.toUpperCase() + " deleted", HttpStatus.OK);
	}
}
