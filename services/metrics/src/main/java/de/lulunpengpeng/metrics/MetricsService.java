package de.lulunpengpeng.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class MetricsService {

    public static final int BOUND = 10000;
    public static final int THREADSHOLD = 3000;
    private Timer timer;

    @Autowired
    public MetricsService(MeterRegistry meterRegistry) {
        this.timer = Timer.builder("CHECK_SCHEDULE").tag("name", "schedule").register(meterRegistry);
    }

    @Scheduled(fixedRate = BOUND)
    public void checkingThread() {
        timer.record(this::doSomething);
    }

    @SneakyThrows
    private void doSomething() {
        long sleepTime = ThreadLocalRandom.current().nextLong(BOUND);
        Thread.sleep(sleepTime);

        if (sleepTime > THREADSHOLD) {
            log.warn("current Thread is {}", Thread.currentThread().getName());
        } else {
            log.info("current Thread is {}", Thread.currentThread().getName());
        }
    }
}
