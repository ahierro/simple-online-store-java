package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAO;
import com.iron.tec.labs.ecommercejava.domain.CategoryDomain;
import com.iron.tec.labs.ecommercejava.domain.PageDomain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

        @Mock
        CategoryDAO categoryDAO;

        @InjectMocks
        CategoryServiceImpl categoryService;

        @Test
        @DisplayName("Should create a category successfully")
        void createCategoryTest() {
                UUID id = UUID.randomUUID();
                CategoryDomain category = CategoryDomain.builder()
                                .id(id)
                                .name("Motherboards")
                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                .build();

                ArgumentCaptor<CategoryDomain> argumentCaptor = ArgumentCaptor.forClass(CategoryDomain.class);
                when(categoryDAO.create(argumentCaptor.capture())).thenReturn(category);
                CategoryDomain createdCategory = categoryService.createCategory(category);

                assertNotNull(createdCategory);
                assertNotNull(category.getId());
                assertEquals(category.getId(), createdCategory.getId());
                assertNotNull(argumentCaptor.getValue());
                assertEquals(category, argumentCaptor.getValue());
                verify(categoryDAO).create(any(CategoryDomain.class));
        }

        @Test
        @DisplayName("Should delete a category successfully")
        void testDelete() {
                String id = UUID.randomUUID().toString();

                doNothing().when(categoryDAO).delete(id);

                categoryService.deleteCategory(id);

                verify(categoryDAO).delete(id);
        }

        @Test
        @DisplayName("Should update an existing category successfully")
        void testUpdateExisting() {
                UUID id = UUID.randomUUID();
                CategoryDomain category = CategoryDomain.builder()
                                .id(id)
                                .name("Motherboards")
                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                .build();
                ArgumentCaptor<CategoryDomain> argumentCaptor = ArgumentCaptor.forClass(CategoryDomain.class);

                when(categoryDAO.update(argumentCaptor.capture())).thenReturn(category);

                CategoryDomain updatedCategory = categoryService.updateCategory(category);

                assertEquals(category, argumentCaptor.getValue());
                assertEquals(category, updatedCategory);
                verify(categoryDAO, times(1)).update(any(CategoryDomain.class));
        }

        @Test
        @DisplayName("Should return a page of categories")
        void getCategoryPage() {
                when(categoryDAO.getPage(0, 1))
                                .thenReturn(new PageDomain<>(
                                                Arrays.asList(CategoryDomain.builder()
                                                                .id(UUID.randomUUID())
                                                                .name("Motherboards")
                                                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                                                .build(),
                                                                CategoryDomain.builder()
                                                                                .id(UUID.randomUUID())
                                                                                .name("Motherboards")
                                                                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                                                                .build()),
                                                2, 2, 0));

                PageDomain<CategoryDomain> page = categoryService.getCategoryPage(0, 1);

                assertNotNull(page);
                assertEquals(2, page.getTotalPages());
                assertEquals(0, page.getNumber());
                assertEquals(2, page.getTotalElements());
                assertNotNull(page.getContent());
                assertEquals(2, page.getContent().size());
        }

        @Test
        @DisplayName("Should return a category by ID")
        void testGetById() {
                UUID categoryId = UUID.randomUUID();
                CategoryDomain expectedCategory = CategoryDomain.builder()
                                .id(categoryId)
                                .name("Motherboards")
                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                .build();

                when(categoryDAO.getById(categoryId)).thenReturn(expectedCategory);

                CategoryDomain actualCategory = categoryService.getById(categoryId);

                assertNotNull(actualCategory);
                assertEquals(expectedCategory.getId(), actualCategory.getId());
                assertEquals(expectedCategory.getName(), actualCategory.getName());
                assertEquals(expectedCategory.getDescription(), actualCategory.getDescription());
                verify(categoryDAO).getById(categoryId);
        }
}
