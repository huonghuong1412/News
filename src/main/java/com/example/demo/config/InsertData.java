package com.example.demo.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.RoleRepository;

@Component
public class InsertData implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

	private static boolean eventFired = false;
	private static final Logger logger = LoggerFactory.getLogger(InsertData.class);

	@Autowired
	private UserRepository repos;

	@Autowired
	private RoleRepository roleRepos;

	@Autowired
	private PasswordEncoder encoder;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (eventFired) {
			return;
		}
		logger.info("Application started.");

		eventFired = true;

		try {
			createRoles();
			createAdminUser();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createRoles() {
		List<Role> roleNames = new ArrayList<>();
		roleNames.add(new Role(Erole.ROLE_ADMIN));
		roleNames.add(new Role(Erole.ROLE_USER));

		for (Role roleName : roleNames) {
			if (roleRepos.existsByName(roleName.getName())) {
				return;
			}
			roleName.setName(roleName.getName());
			try {
				roleRepos.save(roleName);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	private void createAdminUser() {
		if (repos.existsByEmail("admin@gmail.com")) {
			return;
		} else {
			User admin = new User();
			admin.setEmail("admin@gmail.com");
			admin.setPassword(encoder.encode("huong1412"));
			admin.setFullname("Trí Nguyễn");

			List<Role> roles = new ArrayList<Role>();
			Role role = roleRepos.findOneByName(Erole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
			roles.add(role);
			admin.setRoles(roles);
			try {
				repos.save(admin);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// TODO Auto-generated method stub

	}

}
