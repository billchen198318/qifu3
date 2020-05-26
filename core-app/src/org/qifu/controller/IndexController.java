package org.qifu.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
	
	@RequestMapping("/")
	public String index(ModelMap mm, HttpServletRequest request) {
		
		mm.addAttribute("testMessage", "測試 freemarker ~~ !!");
		
		return "index";
	}
	
}
