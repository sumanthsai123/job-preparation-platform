package com.jobprep.job_preparation_platform.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobprep.job_preparation_platform.Job;
import com.jobprep.job_preparation_platform.JobRepository;
import com.jobprep.job_preparation_platform.dto.JobRequestDTO;
import com.jobprep.job_preparation_platform.dto.JobResponseDTO;
import com.jobprep.job_preparation_platform.exception.JobNotFoundException;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // GET ALL
    public List<JobResponseDTO> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(job -> new JobResponseDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation()))
                .collect(Collectors.toList());
    }

    // GET BY ID
    public JobResponseDTO getJobById(int id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(
                        "Job not found with id: " + id));

        return new JobResponseDTO(
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                job.getLocation());
    }

    // CREATE
    public JobResponseDTO createJob(JobRequestDTO request) {

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());

        Job savedJob = jobRepository.save(job);

        return new JobResponseDTO(
                savedJob.getId(),
                savedJob.getTitle(),
                savedJob.getCompany(),
                savedJob.getLocation());
    }

    // UPDATE
    public JobResponseDTO updateJob(int id, JobRequestDTO request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new JobNotFoundException(
                        "Job not found with id: " + id));

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());

        Job updatedJob = jobRepository.save(job);

        return new JobResponseDTO(
                updatedJob.getId(),
                updatedJob.getTitle(),
                updatedJob.getCompany(),
                updatedJob.getLocation());
    }

    // SEARCH
    public List<JobResponseDTO> searchJobs(String keyword) {

        return jobRepository
                .findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(job -> new JobResponseDTO(
                        job.getId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation()))
                .collect(Collectors.toList());
    }

    // DELETE
    public boolean deleteJob(int id) {

        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }

        return false;
    }
}