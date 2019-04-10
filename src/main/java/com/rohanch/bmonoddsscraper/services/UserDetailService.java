package com.rohanch.bmonoddsscraper.services;

import com.rohanch.bmonoddsscraper.models.db.User;
import com.rohanch.bmonoddsscraper.models.wrapper.UserWrapper;
import com.rohanch.bmonoddsscraper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserWrapper(user);
	}

	public void Register(User newUser) {
		User existingUser = userRepository.findByUsername(newUser.getUsername());
		if (existingUser != null) {
			throw new UsernameTakenException();
		}

		newUser.setPassword(encoder.encode(newUser.getPassword())); //TODO: should probably add a salt
		newUser.setBalance(1000f);

		userRepository.save(newUser);
	}

	public class UsernameTakenException extends RuntimeException {
	}
}


