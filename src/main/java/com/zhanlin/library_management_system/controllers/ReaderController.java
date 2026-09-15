package com.zhanlin.library_management_system.controllers;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.messages.ApiMessage;
import com.zhanlin.library_management_system.models.response.ApiResponse;
import com.zhanlin.library_management_system.service.ReaderService;
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

    //TODO: переделать под пагинацию
    @GetMapping
    public ResponseEntity<List<ReaderResponseDto>> getAllReaders() {
        return ResponseEntity.ok(readerService.getAllReaders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReaderResponseDto>> getReaderById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.createSuccessful(readerService.getReaderById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReaderResponseDto>> createReader(@RequestBody @Valid ReaderRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessful(
                        ApiMessage.READER_CREATED.getMessage(),
                        readerService.createReader(dto)
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReaderResponseDto>> updateReader(@RequestBody @Valid ReaderRequestDto dto,
                                                          @PathVariable Long id) {
         return ResponseEntity.ok(
                 ApiResponse.createSuccessful(
                         ApiMessage.READER_UPDATED.getMessage(),
                         readerService.updateReader(id, dto)
                 )
         );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
