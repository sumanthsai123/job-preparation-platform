package com.jobprep.job_preparation_platform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobprep.job_preparation_platform.dto.JobRequestDTO;
import com.jobprep.job_preparation_platform.dto.JobResponseDTO;
import com.jobprep.job_preparation_platform.service.JobService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // GET ALL JOBS
    @GetMapping
    public List<JobResponseDTO> getAllJobs() {
        return jobService.getAllJobs();
    }

    // SEARCH JOBS
    @GetMapping("/search")
    public List<JobResponseDTO> searchJobs(
            @RequestParam String keyword) {

        return jobService.searchJobs(keyword);
    }

    // GET JOB BY ID
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(
            @PathVariable int id) {

        JobResponseDTO job = jobService.getJobById(id);

        return ResponseEntity.ok(job);
    }

    // CREATE JOB
    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(
            @Valid @RequestBody JobRequestDTO request) {

        JobResponseDTO createdJob = jobService.createJob(request);

        return ResponseEntity.status(201).body(createdJob);
    }

    // UPDATE JOB
    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable int id,
            @RequestBody JobRequestDTO request) {

        JobResponseDTO updatedJob = jobService.updateJob(id, request);

        return ResponseEntity.ok(updatedJob);
    }

    // DELETE JOB
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(
            @PathVariable int id) {

        boolean deleted = jobService.deleteJob(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // @GetMapping("/admin")
    // public ResponseEntity<String> adminOnly() {
    //     return ResponseEntity.ok("Welcome Admin! You can access this endpoint.");
    // }
}