package com.spring.redis.stream.controller;


import com.spring.redis.stream.dto.PurchaseEvent;
import com.spring.redis.stream.service.JobProducer;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Redis Stream", description = """
        Below APIs Demonstrate how Redis can be used for stream. This APIs provide methods to start a new Job which is consumed by BillingDeptPurchaseEventConsumer.
        """)
public class RedisStreamJobController {

    private final JobProducer jobProducer;

    @PostMapping("/start")
    public String startJob(@RequestBody PurchaseEvent purchaseEvent) {
        return jobProducer.produce(purchaseEvent);
    }

    @GetMapping("/queued")
    public List<String> getQueuedJobIds() {
        return jobProducer.getQueuedJobsIds();
    }

    @DeleteMapping("/{job_id}/queued")
    public void removeJobFromQueue(@PathVariable("purchaseId") String jobId) {
        jobProducer.removeJobFromQueue(jobId);
    }

    @DeleteMapping("/queued")
    public void clearJobQueue() {
        jobProducer.clearJobQueue();
    }


}
