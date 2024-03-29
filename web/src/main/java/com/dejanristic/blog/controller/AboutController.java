package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.service.CategoryService;
import com.dejanristic.blog.util.AttributeNames;
import com.dejanristic.blog.util.UrlMappings;
import com.dejanristic.blog.util.ViewNames;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class AboutController {

    private final CategoryService categoryService;

    @Autowired
    public AboutController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute(AttributeNames.CATEGORIES)
    public List<Category> getCategories() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

    @GetMapping(UrlMappings.ABOUT)
    public String about() {
        return ViewNames.ABOUT;
    }
}
