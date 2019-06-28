package com.dejanristic.blog.service.impl;

import com.dejanristic.blog.domain.Category;
import com.dejanristic.blog.repository.CategoryRepository;
import com.dejanristic.blog.service.CategoryService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImp implements CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryServiceImp(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category create(Category category) {
        Category oldCategory = categoryRepository.findByName(category.getName());

        if (oldCategory == null) {
            return categoryRepository.save(category);

        }
        return null;
    }
}
