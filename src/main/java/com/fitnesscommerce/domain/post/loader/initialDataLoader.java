package com.fitnesscommerce.domain.post.loader;

import com.fitnesscommerce.domain.post.domain.PostCategory;
import com.fitnesscommerce.domain.post.repository.PostCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class initialDataLoader implements CommandLineRunner {

    private PostCategoryRepository postCategoryRepository;

    @Override
    public void run(String... args) throws Exception{
        if(postCategoryRepository.count() == 0){
            PostCategory postCategory1 = PostCategory.builder().title("자유게시판").build();
            postCategoryRepository.save(postCategory1);

            PostCategory postCategory2 = PostCategory.builder().title("팁 공유").build();
            postCategoryRepository.save(postCategory2);

            PostCategory postCategory3 = PostCategory.builder().title("멸치 양식장").build();
            postCategoryRepository.save(postCategory3);
        }
    }
}
