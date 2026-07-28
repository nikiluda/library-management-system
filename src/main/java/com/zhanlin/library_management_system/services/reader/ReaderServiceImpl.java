package com.zhanlin.library_management_system.services.reader;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.exceptions.ReaderNotFoundException;
import com.zhanlin.library_management_system.mappers.ReaderMapper;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final ReaderMapper readerMapper;
    private static final Logger log = LoggerFactory.getLogger(ReaderServiceImpl.class);



    public ReaderServiceImpl(ReaderRepository readerRepository, ReaderMapper readerMapper) {
        this.readerRepository = readerRepository;
        this.readerMapper = readerMapper;
    }

    private Reader findById(Long id) {
        log.debug("Searching reader by id={}", id);

        return readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException("Reader with id: " + id + " not found"));

    }


    @Override
    public ReaderResponseDto createReader(ReaderRequestDto dto) {
        log.debug("Creating reader with email={}",dto.email());

        Reader reader = readerMapper.toEntity(dto);
        Reader savedReader = readerRepository.save(reader);

        log.info("Reader created: id={}, firstName={}, lastName={}", savedReader.getId(), savedReader.getFirstName(), savedReader.getLastName());
        return readerMapper.toDto(savedReader);

    }

    @Transactional(readOnly = true)
    @Override
    public ReaderResponseDto getReaderById(Long id) {
        Reader reader = findById(id);
        return readerMapper.toDto(reader);
    }


    @Override
    public ReaderResponseDto updateReader(Long id, ReaderRequestDto dto) {
        log.debug("Updating reader with id={}, email={}", id, dto.email());

        Reader reader = findById(id);
        readerMapper.updateReader(dto, reader);
        Reader updatedReader = readerRepository.save(reader);
        log.info("Reader updated: id={}, firstName={}, lastName={}", updatedReader.getId(), updatedReader.getFirstName(), updatedReader.getLastName());
        return readerMapper.toDto(updatedReader);
    }


    @Override
    public void deleteReader(Long  id) {
        log.debug("Deleting reader with id={}", id);
        Reader reader = findById(id);
        readerRepository.delete(reader);
        log.info("Reader deleted id={}, firstName={}, lastName={}", reader.getId(), reader.getFirstName(), reader.getLastName());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReaderResponseDto> getAllReaders() {
        log.debug("Fetching all readers");
        List<ReaderResponseDto> readers =  readerRepository.findAll().stream().map(readerMapper::toDto).toList();
        log.info("Fetched {} readers", readers.size());
        return readers;
    }
}
