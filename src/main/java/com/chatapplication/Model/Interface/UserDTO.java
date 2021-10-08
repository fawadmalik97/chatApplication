package com.chatapplication.Model.Interface;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO implements Serializable {

	private long userId; // User ID
	private String firstName; // User First Name
	private String lastName;// User Last Name
	private String email;// User email
	private int age;// User age
	private String password; // User Password
	private List<ChatDTO> chat = new ArrayList<>();
	private List<CategoryDTO> categories = new ArrayList<>();

}