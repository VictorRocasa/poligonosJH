package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Forma.class);
        Forma forma1 = new Forma();
        forma1.setId(1L);
        Forma forma2 = new Forma();
        forma2.setId(forma1.getId());
        assertThat(forma1).isEqualTo(forma2);
        forma2.setId(2L);
        assertThat(forma1).isNotEqualTo(forma2);
        forma1.setId(null);
        assertThat(forma1).isNotEqualTo(forma2);
    }
}
