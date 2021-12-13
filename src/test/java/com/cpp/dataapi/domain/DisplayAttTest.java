package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DisplayAttTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DisplayAtt.class);
        DisplayAtt displayAtt1 = new DisplayAtt();
        displayAtt1.setId("id1");
        DisplayAtt displayAtt2 = new DisplayAtt();
        displayAtt2.setId(displayAtt1.getId());
        assertThat(displayAtt1).isEqualTo(displayAtt2);
        displayAtt2.setId("id2");
        assertThat(displayAtt1).isNotEqualTo(displayAtt2);
        displayAtt1.setId(null);
        assertThat(displayAtt1).isNotEqualTo(displayAtt2);
    }
}
