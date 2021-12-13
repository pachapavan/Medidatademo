package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ElementsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Elements.class);
        Elements elements1 = new Elements();
        elements1.setId("id1");
        Elements elements2 = new Elements();
        elements2.setId(elements1.getId());
        assertThat(elements1).isEqualTo(elements2);
        elements2.setId("id2");
        assertThat(elements1).isNotEqualTo(elements2);
        elements1.setId(null);
        assertThat(elements1).isNotEqualTo(elements2);
    }
}
