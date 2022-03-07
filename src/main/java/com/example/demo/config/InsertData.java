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
import com.example.demo.model.Source;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.SourceRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.utils.Slug;

@Component
public class InsertData implements ApplicationListener<ContextRefreshedEvent>, InitializingBean {

	private static boolean eventFired = false;
	private static final Logger logger = LoggerFactory.getLogger(InsertData.class);

	@Autowired
	private UserRepository repos;

	@Autowired
	private RoleRepository roleRepos;

	@Autowired
	private SourceRepository sourceRepos;

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
			createSource();
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

	private void createSource() {
		if (sourceRepos.existsBySlug("vietnamnet") || sourceRepos.existsBySlug("tuoi-tre")
				|| sourceRepos.existsBySlug("thanh-nien") || sourceRepos.existsBySlug("vnexpress")) {
			return;
		} else {
			List<Source> entities = new ArrayList<Source>();
			Source vnexpress = new Source("VnExpress", "vnexpress", "vne_logo_rss.png");
			Source vietnamnet = new Source("VietNamNet", "vietnamnet", "Vietnamnet-Logo.png");
			Source tt = new Source("Tuổi Trẻ Online", Slug.makeCode("Tuổi Trẻ"), "Tuổi_Trẻ_Logo.png");
			Source tn = new Source("Thanh Niên Online", Slug.makeCode("Thanh Niên"), "Thanh_Niên_logo.png");
			entities.add(vietnamnet);
			entities.add(tt);
			entities.add(tn);
			entities.add(vnexpress);
			try {
				sourceRepos.saveAll(entities);
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
