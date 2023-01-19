package com.iron.tec.labs.ecommercejava.services;

import com.iron.tec.labs.ecommercejava.db.dao.CategoryDAO;
import com.iron.tec.labs.ecommercejava.db.entities.Category;
import com.iron.tec.labs.ecommercejava.dto.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryDAO categoryDAO;

    @Mock
    ConversionService conversionService;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    void createCategoryTest() {
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();

        CategoryCreationDTO categoryCreationDTO = CategoryCreationDTO.builder()
                .id(id.toString())
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .id(id.toString())
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();
        when(conversionService.convert(any(CategoryCreationDTO.class), eq(Category.class)))
                .thenReturn(category);
        when(conversionService.convert(any(Category.class), eq(CategoryDTO.class)))
                .thenReturn(categoryDTO);
        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryDAO.create(argumentCaptor.capture())).thenReturn(Mono.just(category));
        CategoryDTO createdCategory = categoryService.createCategory(categoryCreationDTO).block();

        assertNotNull(createdCategory);
        assertNotNull(category.getId());
        assertEquals(category.getId().toString(), createdCategory.getId());
        assertNotNull(argumentCaptor.getValue());
        assertEquals(category, argumentCaptor.getValue());
        verify(categoryDAO).create(any(Category.class));
    }

    @Test
    void testDelete() {
        String id = UUID.randomUUID().toString();

        when(categoryDAO.delete(id)).thenReturn(Mono.empty());

        categoryService.deleteCategory(id).block();

        verify(categoryDAO).delete(id);
    }

    @Test
    void testUpdateExisting() {
        UUID id = UUID.randomUUID();
        Category category = Category.builder()
                .id(id)
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();
        CategoryUpdateDTO categoryDTO = CategoryUpdateDTO.builder()
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build();
        ArgumentCaptor<Category> argumentCaptor = ArgumentCaptor.forClass(Category.class);
        when(conversionService.convert(any(CategoryUpdateDTO.class), eq(Category.class)))
                .thenReturn(category);
        when(categoryDAO.update(argumentCaptor.capture())).thenReturn(Mono.empty());

        categoryService.updateCategory(id.toString(), categoryDTO).block();

        assertEquals(category, argumentCaptor.getValue());
        verify(categoryDAO, times(1)).update(any(Category.class));
    }

    @Test
    void testGetAll() {
        when(categoryDAO.getAll()).thenReturn(Flux.just(Category.builder()
                        .id(UUID.randomUUID())
                        .name("Motherboards")
                        .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                        .build(),
                Category.builder()
                        .id(UUID.randomUUID())
                        .name("Motherboards")
                        .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                        .build()));

        when(conversionService.convert(any(Category.class), eq(CategoryDTO.class)))
                .thenAnswer(x -> CategoryDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Motherboards")
                        .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                        .build());

        StepVerifier.create(categoryService.getAll())
                .expectNextCount(2)
                .verifyComplete();

    }

    @Test
    void getCategoryPage() {
        when(categoryDAO.getPage(0, 1)).thenReturn(Mono.just(new PageImpl<>(
                Arrays.asList(Category.builder()
                                .id(UUID.randomUUID())
                                .name("Motherboards")
                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                .build(),
                        Category.builder()
                                .id(UUID.randomUUID())
                                .name("Motherboards")
                                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                                .build()), PageRequest.of(0,1),2)));
        when(conversionService.convert(any(Category.class), eq(CategoryDTO.class)))
                .thenAnswer(x -> CategoryDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Motherboards")
                        .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                        .build());

        PageResponseDTO<CategoryDTO> page =
                categoryService.getCategoryPage(ProductPageRequestDTO.builder().page(0).size(1).build()).block();

        assertNotNull(page);
        assertEquals(2,page.getTotalPages());
        assertEquals(0,page.getNumber());
        assertEquals(2,page.getTotalElements());
        assertNotNull(page.getContent());
        assertEquals(2,page.getContent().size());
    }

    @Test
    void testGetById() {
        when(categoryDAO.getById(any(UUID.class))).thenReturn(Mono.just(Category.builder()
                .id(UUID.randomUUID())
                .name("Motherboards")
                .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                .build()));

        when(conversionService.convert(any(Category.class), eq(CategoryDTO.class)))
                .thenAnswer(x -> CategoryDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .name("Motherboards")
                        .description("A motherboard is the main printed circuit board (PCB) in general-purpose computers and other expandable systems.")
                        .build());

        StepVerifier.create(categoryService.getById(UUID.randomUUID()))
                .expectNextCount(1)
                .verifyComplete();

    }
}
