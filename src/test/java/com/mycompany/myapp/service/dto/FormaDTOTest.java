package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormaDTO.class);
        FormaDTO formaDTO1 = new FormaDTO();
        formaDTO1.setId(1L);
        FormaDTO formaDTO2 = new FormaDTO();
        assertThat(formaDTO1).isNotEqualTo(formaDTO2);
        formaDTO2.setId(formaDTO1.getId());
        assertThat(formaDTO1).isEqualTo(formaDTO2);
        formaDTO2.setId(2L);
        assertThat(formaDTO1).isNotEqualTo(formaDTO2);
        formaDTO1.setId(null);
        assertThat(formaDTO1).isNotEqualTo(formaDTO2);
    }
}
