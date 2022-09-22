package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoligonoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Poligono.class);
        Poligono poligono1 = new Poligono();
        poligono1.setId(1L);
        Poligono poligono2 = new Poligono();
        poligono2.setId(poligono1.getId());
        assertThat(poligono1).isEqualTo(poligono2);
        poligono2.setId(2L);
        assertThat(poligono1).isNotEqualTo(poligono2);
        poligono1.setId(null);
        assertThat(poligono1).isNotEqualTo(poligono2);
    }
}
