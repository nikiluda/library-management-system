package com.zhanlin.library_management_system.service.impl;

import com.zhanlin.library_management_system.dto.PageResponse;
import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.exceptions.ResourceAlreadyExistsException;
import com.zhanlin.library_management_system.exceptions.ResourceNotFoundException;
import com.zhanlin.library_management_system.logging.annotation.Audit;
import com.zhanlin.library_management_system.logging.annotation.AuditAction;
import com.zhanlin.library_management_system.mappers.PageMapper;
import com.zhanlin.library_management_system.mappers.ReaderMapper;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import com.zhanlin.library_management_system.service.ReaderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;
    private final PageMapper pageMapper;


    public ReaderServiceImpl(ReaderRepository readerRepository, ReaderMapper readerMapper, PageMapper pageMapper) {
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
        this.pageMapper = pageMapper;
    }


    private Reader findById(Long id) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.READER_NOT_FOUND,
                        ApiErrorMessage.READER_NOT_FOUND_BY_ID.getMessage(id)
                ));

        return reader;

    }


    @Override
    @Audit(AuditAction.READER_CREATED)
    public ReaderResponseDto createReader(ReaderRequestDto dto) {

        if (readerRepository.existsByEmail(dto.email())) {
            throw new ResourceAlreadyExistsException(
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(dto.email())
            );
        }

        if (readerRepository.existsByPhone(dto.phone())) {
            throw new ResourceAlreadyExistsException(
                    ErrorCode.PHONE_ALREADY_EXISTS,
                    ApiErrorMessage.PHONE_ALREADY_EXISTS.getMessage(dto.phone())
            );
        }

        Reader reader = readerMapper.toEntity(dto);
        Reader savedReader = readerRepository.save(reader);

        return readerMapper.toDto(savedReader);

    }

    @Transactional(readOnly = true)
    @Override
    public ReaderResponseDto getReaderById(Long id) {
        Reader reader = findById(id);
        return readerMapper.toDto(reader);
    }


    @Override
    @Audit(AuditAction.READER_UPDATED)
    public ReaderResponseDto updateReader(Long id, ReaderRequestDto dto) {
        Reader reader = findById(id);

        if (!reader.getEmail().equals(dto.email()) && readerRepository.existsByEmail(dto.email())) {
            throw new ResourceAlreadyExistsException(
                    ErrorCode.EMAIL_ALREADY_EXISTS,
                    ApiErrorMessage.EMAIL_ALREADY_EXISTS.getMessage(dto.email())
            );
        }

        if (!reader.getPhone().equals(dto.phone()) && readerRepository.existsByPhone(dto.phone())) {
            throw new ResourceAlreadyExistsException(
                    ErrorCode.PHONE_ALREADY_EXISTS,
                    ApiErrorMessage.PHONE_ALREADY_EXISTS.getMessage(dto.phone())
            );
        }

        readerMapper.updateReader(dto, reader);
        Reader updatedReader = readerRepository.save(reader);
        return readerMapper.toDto(updatedReader);
    }


    @Override
    @Audit(AuditAction.READER_DELETED)
    public void deleteReader(Long  id) {
        Reader reader = findById(id);
        readerRepository.delete(reader);

    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ReaderResponseDto> getAllReaders(Pageable pageable) {
        Page<Reader> readers = readerRepository.findAll(pageable);
        Page<ReaderResponseDto> result = readers.map(readerMapper::toDto);

        return pageMapper.toPageResponse(result);
    }

}
