package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FormaMapperTest {

    private FormaMapper formaMapper;

    @BeforeEach
    public void setUp() {
        formaMapper = new FormaMapperImpl();
    }
}
