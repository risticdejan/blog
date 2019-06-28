package com.dejanristic.blog.repository;

import com.dejanristic.blog.domain.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Category findByName(String username);
}
