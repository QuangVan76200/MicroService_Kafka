package com.service.profileservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.service.profileservice.data.Profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProfileDTO {
	
	private long id;

	private String email;

	private String status;

	private double initialBalance;

	private String fullname;

	private String username;
	
	private String numberphone;
	
	@JsonIgnore
	private String password;

	private String role;

	public static Profile dtoToEntity(ProfileDTO profileDTO) {
		Profile profile = new Profile();
		profile.setFullname(profileDTO.getFullname());
		profile.setRole(profileDTO.getRole());
		profile.setNumberphone(profileDTO.getNumberphone());
		profile.setStatus(profileDTO.getStatus());
		profile.setEmail(profileDTO.getEmail());
		return profile;
	}

	public static ProfileDTO entityToDto(Profile profile) {
		ProfileDTO profileDTO = new ProfileDTO();
		profileDTO.setId(profile.getId());
		profileDTO.setEmail(profile.getEmail());
		profileDTO.setNumberphone(profile.getNumberphone());
		profileDTO.setStatus(profile.getStatus());
		profileDTO.setFullname(profile.getFullname());
		profileDTO.setRole(profile.getRole());
		return profileDTO;
	}

}
