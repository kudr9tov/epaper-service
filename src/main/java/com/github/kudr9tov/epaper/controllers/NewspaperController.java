package com.github.kudr9tov.epaper.controllers;

import com.github.kudr9tov.epaper.controllers.payload.responses.PageResponse;
import com.github.kudr9tov.epaper.repositories.entities.NewspaperEntity;
import com.github.kudr9tov.epaper.services.NewspaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/newspapers")
public class NewspaperController {

    private final NewspaperService newspaperService;

    @Autowired
    public NewspaperController(NewspaperService newspaperService) {
        this.newspaperService = newspaperService;
    }

    @GetMapping
    public ResponseEntity<?> getNewsPage(@RequestParam(required = false) String newspaperName,
                                         @RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "5") int size,
                                         @RequestParam(defaultValue = "id:desc") String[] sort) {
        Page<NewspaperEntity> newsPage = newspaperService.getNewsPage(newspaperName, page, size, sort);
        PageResponse<NewspaperEntity> response = PageResponse
                .<NewspaperEntity>builder()
                .entries(newsPage.getContent())
                .totalNumEntries(newsPage.getTotalElements())
                .page(newsPage.getNumber() + 1)
                .totalPages(newsPage.getTotalPages())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNews(@PathVariable Long id) {
        return ResponseEntity.ok(newspaperService.getNews(id));
    }

    @PostMapping(value = "upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadNews(@RequestParam(name = "file") MultipartFile multipartFile) {
        return ResponseEntity.ok(newspaperService.uploadNews(multipartFile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeNews(@PathVariable Long id) {
        newspaperService.removeNews(id);
        return ResponseEntity.ok().build();
    }
}
