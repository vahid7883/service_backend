package com.numjew.service_backend.demo;

import com.numjew.service_backend.demo.DemoDto;
import com.numjew.service_backend.mappers.DemoMapper;
import com.numjew.service_backend.demo.DemoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

// import java.util.Optional;

@AllArgsConstructor
@Service
public class DemoService {
    private final DemoRepository demoRepository;
private DemoMapper demoMapper;

    public DemoDto getDemoById(Long id){
        var demo = demoRepository.findById(id).orElseThrow();
        return demoMapper.toDto(demo);
    }
}
