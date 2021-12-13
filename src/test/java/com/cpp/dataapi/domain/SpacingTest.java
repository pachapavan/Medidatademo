package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SpacingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Spacing.class);
        Spacing spacing1 = new Spacing();
        spacing1.setId("id1");
        Spacing spacing2 = new Spacing();
        spacing2.setId(spacing1.getId());
        assertThat(spacing1).isEqualTo(spacing2);
        spacing2.setId("id2");
        assertThat(spacing1).isNotEqualTo(spacing2);
        spacing1.setId(null);
        assertThat(spacing1).isNotEqualTo(spacing2);
    }
}
