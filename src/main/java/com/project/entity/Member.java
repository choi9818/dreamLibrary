package com.project.entity;

import java.util.Date;

import com.project.constant.Role;



public class Member {
	String id; //(PK)
	String name;
	String password;
	String email;
	Date birthday;
	String phone;
	Role role;

}
