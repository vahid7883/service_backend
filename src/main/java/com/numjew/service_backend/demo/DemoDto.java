package com.numjew.service_backend.demo;

import lombok.Data;

@Data
public class DemoDto {
    private Long id;
    private String title;
    private String location;
    private Long userId;
}
