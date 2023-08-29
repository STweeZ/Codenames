package fr.univartois.ili.jai.object;

import fr.univartois.ili.jai.persistance.PersistableConcrete;

public class Player extends PersistableConcrete {

	private String username;
	private Role role;
	
	public Player() {
		super();
	}

	public Player(String username) {
		super();
		this.setUsername(username);
	}

	public Player(String username, Role role) {
		super();
		this.setUsername(username);
		this.setRole(role);
	}

	public Player(long id, String username) {
		this(username);
		this.setId(id);
	}

	public Player(long id, String username, Role role) {
		this(id, username);
		this.setRole(role);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public boolean isSpy() {
		return this.role == Role.SPY;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
