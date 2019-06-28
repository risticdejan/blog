package com.dejanristic.blog.service;

import com.dejanristic.blog.domain.Category;
import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(Long id);

    Category findByName(String name);

    Category create(Category category);
}
