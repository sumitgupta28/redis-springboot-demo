package com.spring.redis.stream.service;


import com.spring.redis.stream.dto.PurchaseEvent;
import com.spring.redis.stream.exception.PurchaseEventAlreadyQueuedException;
import com.spring.redis.stream.exception.PurchaseEventNotFoundInQueueException;
import com.spring.redis.stream.exception.RemovingRunningPurchaseEventException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobProducer {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${redis-stream.stream-key}")
    private String streamKey;


    public String produce(PurchaseEvent jobMessage) {
        if (getQueuedJobsIds().contains(jobMessage.getPurchaseId())) {
            log.error("job with id {} is already queued", jobMessage.getPurchaseId());
            throw new PurchaseEventAlreadyQueuedException(jobMessage.getPurchaseId());
        }

        ObjectRecord<String, PurchaseEvent> jobRecord = StreamRecords.newRecord()
                .ofObject(jobMessage)
                .withStreamKey(streamKey);

        RecordId recordId = redisTemplate.opsForStream()
                .add(jobRecord);

        if (isNull(recordId)) {
            log.error("error producing message for job {}", jobMessage);
            return null;
        }

        log.info("job {} was added to the queue with id {}", jobMessage, recordId);
        return recordId.getValue();
    }


    public List<String> getQueuedJobsIds() {
        return redisTemplate.opsForStream()
                .read(PurchaseEvent.class, StreamOffset.fromStart(streamKey))
                .stream()
                .map(Record::getValue)
                .map(PurchaseEvent::getPurchaseId)
                .toList();
    }

    public void removeJobFromQueue(String jobId) {
        List<ObjectRecord<String, PurchaseEvent>> allQueuedJobs = redisTemplate.opsForStream()
                .read(PurchaseEvent.class, StreamOffset.fromStart(streamKey));

        if (allQueuedJobs.isEmpty()) {
            log.error("job with id {} is not queued", jobId);
            throw new PurchaseEventNotFoundInQueueException(jobId);
        }

        allQueuedJobs.stream()
                .findFirst()
                .map(Record::getValue)
                .map(PurchaseEvent::getPurchaseId)
                .filter(jobId::equalsIgnoreCase)
                .ifPresent(firstJobId -> {
                    log.error("job with id {} is running and can't be removed from the queue", jobId);
                    throw new RemovingRunningPurchaseEventException(jobId);
                });

        allQueuedJobs.stream()
                .skip(1)
                .filter(jobRecord -> jobRecord.getValue().getPurchaseId().equals(jobId))
                .findFirst()
                .ifPresentOrElse(jobRecord -> redisTemplate.opsForStream().delete(jobRecord),
                        () -> {
                            throw new PurchaseEventNotFoundInQueueException(jobId);
                        }
                );
    }

    public void clearJobQueue() {
        redisTemplate.opsForStream()
                .trim(streamKey, 0);
    }


}
