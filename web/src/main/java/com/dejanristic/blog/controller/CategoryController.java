package com.dejanristic.blog.controller;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.service.CategoryService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    @ResponseBody
    private List<Category> get() {
        List<Category> categories = categoryService.findAll();
        return categories;
    }

}
