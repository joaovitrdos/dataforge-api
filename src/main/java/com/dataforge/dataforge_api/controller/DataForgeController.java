package com.dataforge.dataforge_api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dataforge.dataforge_api.dto.DataForgeRequest;
import com.dataforge.dataforge_api.dto.DataForgeResponse;
import com.dataforge.dataforge_api.service.DataForgeService;

@RestController
@RequestMapping("/api/dataforge")
public class DataForgeController {

    private final DataForgeService service;

    public DataForgeController(DataForgeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DataForgeResponse> generate(@RequestBody DataForgeRequest request) {
        // Pega o schema do "user" (pode expandir depois para múltiplos tipos)
        Map<String, String> userSchema = request.getSchema().get("user");

        if (userSchema == null || userSchema.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Map<String, Object>> data = service.generateData(userSchema, request.getCount());

        DataForgeResponse response = new DataForgeResponse();
        response.setData(data);

        return ResponseEntity.ok(response);
    }
}
