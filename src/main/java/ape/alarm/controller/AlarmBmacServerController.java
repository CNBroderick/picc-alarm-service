package ape.alarm.controller;

import ape.master.common.parameter.ParameterEnum;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.bklab.quark.util.json.GsonJsonObjectUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/bmac")
public class AlarmBmacServerController {

    @RequestMapping("/alarm")
    public ResponseEntity<String> alarm(HttpServletRequest request, HttpServletResponse response) {
        try {
            if (!checkSecret(request.getHeader("X-Secret"))) {
                return createResponse(false, 1, "密钥错误");
            }
            String data = request.getReader().lines().collect(Collectors.joining("\n"));
            log.debug("蓝鲸告警中心收到请求:\n" + new GsonJsonObjectUtil(data).pretty());

            return createResponse(true, 1200, "创建告警成功。");
        } catch (Exception e) {
            log.error("蓝鲸告警中心处理请求失败。", e);
            return createResponse(false, 5001, e.getMessage());
        }
    }

    private boolean checkSecret(String secret) {
        return Objects.equals(secret, ParameterEnum.后台接口.蓝鲸告警中心密钥.getEntryValue("Q0bxS1vQMK1hy2YbiZdqETVx1pwNIThR"));
    }

    private ResponseEntity<String> createResponse(boolean result, int code, String message) {
        JsonObject json = new JsonObject();

        json.addProperty("result", result);
        json.addProperty("code", code);
        json.add("data", new JsonObject());
        json.addProperty("message", message);

        return ResponseEntity.ok(json.toString());
    }
}
