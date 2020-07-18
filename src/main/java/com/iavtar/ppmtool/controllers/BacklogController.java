package com.iavtar.ppmtool.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iavtar.ppmtool.domain.ProjectTask;
import com.iavtar.ppmtool.services.MapValidationErrorService;
import com.iavtar.ppmtool.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin("*")
public class BacklogController {

	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private MapValidationErrorService mapValidationErrorService;

	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
			@PathVariable("backlog_id") String backlogId) {

		ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);

		if (!(errorMap == null)) {
			return errorMap;
		}

		ProjectTask projectTask1 = projectTaskService.addProjectTask(backlogId, projectTask);

		return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);

	}

}