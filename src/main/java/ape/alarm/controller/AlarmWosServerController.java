package ape.alarm.controller;

import com.google.gson.JsonObject;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@SuppressWarnings("SpellCheckingInspection")
@RequestMapping("/api/wos")
public class AlarmWosServerController {

    private static final String REDIS_KEY = "ALARM-WOS-WF-CACHE-SET";
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/createWF4IM")
    public ResponseEntity<String> createWF4IM(HttpServletRequest request, HttpServletResponse response) {
        String uuid = UUID.randomUUID().toString();
        try {
            GsonJsonObjectUtil json = new GsonJsonObjectUtil(request.getReader().lines().collect(Collectors.joining("\n")));
            uuid = json.string("uuid");
            String content = (String) stringRedisTemplate.opsForHash().get(REDIS_KEY, uuid);
            return new ResponseEntity<>(content == null ? createNew(uuid) : content, HttpStatus.OK);
        } catch (Exception e) {
            StringWriter writer = new StringWriter();
            e.printStackTrace(new PrintWriter(writer));
            JsonObject object = new JsonObject();
            object.addProperty("resmsg", writer.toString());
            object.addProperty("rescode", "0001");
            object.addProperty("uuid", uuid);
            return new ResponseEntity<>(object.toString(), HttpStatus.OK);
        }
    }

    private String createNew(String uuid) {
        JsonObject json = new JsonObject();
        String wfnum = "INM-%s-%06d".formatted(
                DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now()),
                stringRedisTemplate.opsForHash().size(REDIS_KEY) + 1);
        json.addProperty("uuid", uuid);
        json.addProperty("wfnum", wfnum);
        Instant instant = Instant.now();
        json.addProperty("wfid", instant.getEpochSecond() * 10E9 + instant.getNano() + "");
        json.addProperty("resmsg", "告警创建事件工单成功，工单号【" + wfnum + "】");
        json.addProperty("rescode", "0000");
        String string = json.toString();
        stringRedisTemplate.opsForHash().put(REDIS_KEY, uuid, string);
        return string;
    }

}
