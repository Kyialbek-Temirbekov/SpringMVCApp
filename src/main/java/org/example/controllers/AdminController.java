package org.example.controllers;

import org.example.dao.PersonDao;
import org.example.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final PersonDao personDao;
    @Autowired
    public AdminController(PersonDao personDao) {
        this.personDao = personDao;
    }
    @GetMapping()
    public String adminPage(Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("people", personDao.index());
        return "adminPage";
    }
    @PatchMapping("/add")
    public String makeAdmin(@ModelAttribute("person") Person person) {
        System.out.println(person.getId());
        return "redirect:/people/"+person.getId();
    }
}
