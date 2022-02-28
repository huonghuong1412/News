package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.example.demo.config.Erole;

@Entity
@Table(name = "tbl_role")
public class Role extends BaseEntity {

	@Enumerated(EnumType.STRING)
	@Column(name = "name")
	private Erole name;

	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<User>();

	public Role() {
	}

	public Role(Erole name) {
		super();
		this.name = name;
	}

	public Erole getName() {
		return name;
	}

	public void setName(Erole name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
