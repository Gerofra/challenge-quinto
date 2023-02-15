package com.universidadquinto.service.impl;

import com.universidadquinto.entity.Role;
import com.universidadquinto.enums.RoleEnum;
import com.universidadquinto.repository.RoleRepository;
import com.universidadquinto.service.RoleService;

public class RoleServiceImpl implements RoleService {

	private RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public Role findByName(String name) {
		return roleRepository.findByName(RoleEnum.valueOf(name));
	}

}
