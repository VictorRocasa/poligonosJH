package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PoligonoMapperTest {

    private PoligonoMapper poligonoMapper;

    @BeforeEach
    public void setUp() {
        poligonoMapper = new PoligonoMapperImpl();
    }
}
