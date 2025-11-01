package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.model.Category;
import com.adam.buzas.webshop.main.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepositoryRepository;

    public Optional<Category> getCategory(int id){
        return categoryRepositoryRepository.findById(id);
    }

    public Optional<Category> getcategoryByName(String name){
        return categoryRepositoryRepository.findByName(name);
    }

    public Iterable<Category> getAll(){
        return categoryRepositoryRepository.findAll();
    }

    public Category newCategory(String categoryName){
        Category category = new Category();
        category.setName(categoryName);
        return categoryRepositoryRepository.save(category);
    }

    public void deleteKategoria(int id) {
        categoryRepositoryRepository.deleteById(id);
    }

    public boolean isHaveCategoryWithName(String category) {
        return categoryRepositoryRepository.findByName(category).isPresent();
    }
}
