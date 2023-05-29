package com.aton.aton.first.task.controllers;

import com.aton.aton.first.task.DAO.PersonDAO;
import com.aton.aton.first.task.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping()
public class MainController {

    private final PersonDAO personDAO;

    @Autowired
    public MainController(PersonDAO personDAO) {
        this.personDAO = personDAO;
    }

    @GetMapping
    public String getHomepage() {
        return "homepage";
    }

    @GetMapping(params = {"selected", "inputField"})
    public String getHomepageWithParams(@RequestParam("selected") String selected,
                              @RequestParam(name = "inputField", required = false) String inputField,
                              Model model) {
        List<Person> listOfPerson = null;
        if (!inputField.equals("") || selected.equals("name"))
            switch (selected) {
                case "name":
                    listOfPerson = personDAO.getPersonByName(inputField);
                    model.addAttribute("name", inputField);
                    if (listOfPerson != null) model.addAttribute("persons", listOfPerson);
                    else model.addAttribute("msg", "Такого нэма");
                    break;
                case "value":
                    listOfPerson = personDAO.getPersonByValue(Double.parseDouble(inputField));
                    model.addAttribute("value", inputField);
                    if (listOfPerson != null) model.addAttribute("persons", listOfPerson);
                    else model.addAttribute("msg", "Такого нэма");
                    break;
                case "account":
                    listOfPerson = personDAO.getPersonByAccount(Long.parseLong(inputField));
                    model.addAttribute("account", inputField);
                    if (listOfPerson != null) model.addAttribute("persons", listOfPerson);
                    else model.addAttribute("msg", "Такого нэма");
                    break;
            }
        return "homepage";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") long account,
                       Model model) {
        model.addAttribute("person", personDAO.findPerson(account));
        return "edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") Person person,
                               @PathVariable("id") long account) {
        personDAO.updatePerson(account, person);
        return "redirect:/";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") long accountId,
                               Model model) {
        personDAO.deletePerson(accountId);
        model.addAttribute("msg", "Удалено успешно");
        return "redirect:/";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "new";
    }

    @PostMapping
    public String create(@ModelAttribute("person") Person person) {
        personDAO.save(person);
        return "redirect:/";
    }
}
