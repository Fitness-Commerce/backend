package com.fitnesscommerce.loader;

import com.fitnesscommerce.domain.item.domain.ItemCategory;
import com.fitnesscommerce.domain.item.repository.ItemCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialDataLoader implements CommandLineRunner {

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Override
    public void run(String... args) throws Exception {

        if (itemCategoryRepository.count() == 0) {

            ItemCategory category1 = ItemCategory.builder().title("비타민").build();
            itemCategoryRepository.save(category1);

            ItemCategory category2 = ItemCategory.builder().title("오메가3").build();
            itemCategoryRepository.save(category2);

            ItemCategory category3 = ItemCategory.builder().title("식이섬유").build();
            itemCategoryRepository.save(category3);

            ItemCategory category4 = ItemCategory.builder().title("유산균").build();
            itemCategoryRepository.save(category4);

            ItemCategory category5 = ItemCategory.builder().title("닭가슴살").build();
            itemCategoryRepository.save(category5);

            ItemCategory category6 = ItemCategory.builder().title("단백질").build();
            itemCategoryRepository.save(category6);

            ItemCategory category7 = ItemCategory.builder().title("부스터").build();
            itemCategoryRepository.save(category7);

            ItemCategory category8 = ItemCategory.builder().title("다이어트").build();
            itemCategoryRepository.save(category8);

            ItemCategory category9 = ItemCategory.builder().title("쉐이커").build();
            itemCategoryRepository.save(category9);

            ItemCategory category10 = ItemCategory.builder().title("용품").build();
            itemCategoryRepository.save(category10);

            ItemCategory category11 = ItemCategory.builder().title("아르기닌").build();
            itemCategoryRepository.save(category11);

            ItemCategory category12 = ItemCategory.builder().title("기타").build();
            itemCategoryRepository.save(category12);

        }
    }
}
