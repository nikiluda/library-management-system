package com.zhanlin.library_management_system.services.reader;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.exceptions.ReaderNotFoundException;
import com.zhanlin.library_management_system.logging.annotation.Audit;
import com.zhanlin.library_management_system.logging.annotation.AuditAction;
import com.zhanlin.library_management_system.mappers.ReaderMapper;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;



    public ReaderServiceImpl(ReaderRepository readerRepository, ReaderMapper readerMapper) {
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
    }

    private Reader findById(Long id) {

        return readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException("Reader with id: " + id + " not found"));

    }


    @Override
    @Audit(AuditAction.READER_CREATED)
    public ReaderResponseDto createReader(ReaderRequestDto dto) {

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
    public List<ReaderResponseDto> getAllReaders() {
        List<ReaderResponseDto> readers =  readerRepository.findAll().stream().map(readerMapper::toDto).toList();
        return readers;
    }
}
