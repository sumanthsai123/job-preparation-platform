package com.jobprep.job_preparation_platform;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//To run the code use  ".\mvnw.cmd spring-boot:run"  commmand 
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {

        return "Job Preparation Platform Backend is running!";
    }

    @GetMapping("/api/users/{id}")
    public String getUser(@PathVariable int id) {
        return "User ID: " + id;
    }

    // @GetMapping("/api/jobs")
    // public String searchJobs(
    //         @RequestParam String role,
    //         @RequestParam String location) {

    //     return "Searching " + role + " jobs in " + location;
    // }
}
