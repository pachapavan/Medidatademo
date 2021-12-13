package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ButtonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Button.class);
        Button button1 = new Button();
        button1.setId("id1");
        Button button2 = new Button();
        button2.setId(button1.getId());
        assertThat(button1).isEqualTo(button2);
        button2.setId("id2");
        assertThat(button1).isNotEqualTo(button2);
        button1.setId(null);
        assertThat(button1).isNotEqualTo(button2);
    }
}
