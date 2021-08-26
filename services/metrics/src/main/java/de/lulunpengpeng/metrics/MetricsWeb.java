package de.lulunpengpeng.metrics;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class MetricsWeb {

    Counter parameterCounter;
    Counter nullParameterCounter;
    Timer timer;

    public MetricsWeb(MeterRegistry meterRegistry) {
        this.parameterCounter = meterRegistry.counter("VISIT_COUNTER", "id", "NOT_NULL");
        this.nullParameterCounter = meterRegistry.counter("VISIT_COUNTER", "id", "NULL");
        this.timer = meterRegistry.timer("HTTP_TIMER", "method", "GET");
    }

    @GetMapping("greeting")
    public String sayHello(@RequestParam(required = false) String name) {
        return timer.record(() -> {
            String responseName = Optional.ofNullable(name).orElse("test");

            if (StringUtils.hasText(name))
                parameterCounter.increment();
            else
                nullParameterCounter.increment();

            return String.format("Hello %s", responseName);
        });
    }

    @GetMapping("names")
    @Timed(value = "GET_NAMES", percentiles = {0.5, 0.95})
    public List<String> getNames() {
        return List.of("eric", "alex", "philipp", "söhren", "rene");
    }

    @GetMapping("names2")
    @Timed(value = "GET_NAMES_WITH_OUT_PERCENTILES")
    public List<String> getNames2() {
        return List.of("eric", "alex", "philipp", "söhren", "rene");
    }
}
