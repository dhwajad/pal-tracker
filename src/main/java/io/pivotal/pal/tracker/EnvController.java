package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private Map<String, String> env = new HashMap<>();

    public EnvController(@Value("${port:NA}") String port, @Value("${memory.limit:NA}") String memoryLimit,
                         @Value("${cf.instance.index:NA}") String cfInstanceIndex, @Value("${cf.instance.addr:NA}") String cfInstanceAddress) {
        env.put("PORT", port);
        env.put("MEMORY_LIMIT", memoryLimit);
        env.put("CF_INSTANCE_INDEX", cfInstanceIndex);
        env.put("CF_INSTANCE_ADDR", cfInstanceAddress);
    }

    @GetMapping("/env")
    public Map getEnv() {
        return env;
    }

}
