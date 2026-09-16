package com.numjew.service_backend.mappers;

import com.numjew.service_backend.demo.DemoDto;
import com.numjew.service_backend.demo.Demo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DemoMapper {
    DemoDto toDto(Demo demo);

}
