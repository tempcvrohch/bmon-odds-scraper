package com.rohanch.bmonoddsscraper.models.db;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
		"id",
		"username",
		"password",
		"balance",
})

@Entity
@Table(name = "users")
public class User extends BaseEntity {
	@Id
	@JsonProperty("id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonProperty("username")
	@Column(name = "user_name", nullable = false, unique = true)
	private String username;

	@JsonProperty("password")
	@Column(name = "password", nullable = false)
	private String password;

	@JsonProperty("balance")
	@Column(name = "balance")
	private Float balance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Float getBalance() {
		return balance;
	}

	public void setBalance(Float balance) {
		this.balance = balance;
	}
}
