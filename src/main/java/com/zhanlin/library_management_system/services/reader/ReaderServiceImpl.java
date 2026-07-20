package com.zhanlin.library_management_system.services.reader;

import com.zhanlin.library_management_system.dto.reader.ReaderRequestDto;
import com.zhanlin.library_management_system.dto.reader.ReaderResponseDto;
import com.zhanlin.library_management_system.mappers.ReaderMapper;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                .orElseThrow(() -> new IllegalArgumentException("Reader with id: " + id + " not found"));
    }


    @Override
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
    public ReaderResponseDto updateReader(Long id, ReaderRequestDto dto) {
        Reader reader = findById(id);
        readerMapper.updateReader(dto, reader);
        Reader updatedReader = readerRepository.save(reader);
        return readerMapper.toDto(updatedReader);
    }


    @Override
    public void deleteReader(Long  id) {
        readerRepository.delete(findById(id));
    }
}
