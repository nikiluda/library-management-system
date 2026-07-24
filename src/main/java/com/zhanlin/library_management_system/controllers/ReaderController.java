package com.zhanlin.library_management_system.controllers;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.services.reader.ReaderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
public class ReaderController {

    private final ReaderService readerService;

    public ReaderController(ReaderService readerService) {
        this.readerService = readerService;
    }

    @GetMapping
    public ResponseEntity<List<ReaderResponseDto>> getAllReaders() {
        return ResponseEntity.ok(readerService.getAllReaders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderResponseDto> getReaderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(readerService.getReaderById(id));
    }

    @PostMapping
    public ResponseEntity<ReaderResponseDto> createReader(@RequestBody @Valid ReaderRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readerService.createReader(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReaderResponseDto> updateReader(@RequestBody @Valid ReaderRequestDto dto,
                                                          @PathVariable("id") Long id) {
         return ResponseEntity.ok(readerService.updateReader(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable("id") Long id) {
        readerService.deleteReader(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
