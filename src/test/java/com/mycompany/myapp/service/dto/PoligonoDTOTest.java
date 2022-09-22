package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoligonoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoligonoDTO.class);
        PoligonoDTO poligonoDTO1 = new PoligonoDTO();
        poligonoDTO1.setId(1L);
        PoligonoDTO poligonoDTO2 = new PoligonoDTO();
        assertThat(poligonoDTO1).isNotEqualTo(poligonoDTO2);
        poligonoDTO2.setId(poligonoDTO1.getId());
        assertThat(poligonoDTO1).isEqualTo(poligonoDTO2);
        poligonoDTO2.setId(2L);
        assertThat(poligonoDTO1).isNotEqualTo(poligonoDTO2);
        poligonoDTO1.setId(null);
        assertThat(poligonoDTO1).isNotEqualTo(poligonoDTO2);
    }
}
