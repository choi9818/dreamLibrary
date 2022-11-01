package com.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class InfoController {
	
	@GetMapping(value = "/info/intro")
	public String intro() {
		return "info/libraryIntro";
	}
	
	@GetMapping(value = "/info/usetime")
	public String useTime() {
		return "info/libraryUse";
	}

}
