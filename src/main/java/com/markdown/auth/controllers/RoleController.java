package com.markdown.auth.controllers;

import com.markdown.auth.dtos.RoleDTO;
import com.markdown.auth.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.google.common.base.Preconditions.checkNotNull;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;
    
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public RoleDTO createRole(@RequestBody RoleDTO roleDTO){
        
        checkNotNull(roleDTO);
        roleService.createRole(roleDTO);

        return roleDTO;
    }

    @GetMapping("/info/{roleId}")
    public RoleDTO createRole(@PathVariable String roleId){

        return roleService.roleInfo(roleId);
    }

}
