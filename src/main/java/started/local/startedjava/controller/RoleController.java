package started.local.startedjava.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import started.local.startedjava.dto.request.ApiResponse;
import started.local.startedjava.dto.request.PermissionRequest;
import started.local.startedjava.dto.request.RoleRequest;
import started.local.startedjava.dto.response.PermissionResponse;
import started.local.startedjava.dto.response.RoleResponse;
import started.local.startedjava.service.PermissionService;
import started.local.startedjava.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllPermission() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }

}
