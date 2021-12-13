package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BodyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Body.class);
        Body body1 = new Body();
        body1.setId("id1");
        Body body2 = new Body();
        body2.setId(body1.getId());
        assertThat(body1).isEqualTo(body2);
        body2.setId("id2");
        assertThat(body1).isNotEqualTo(body2);
        body1.setId(null);
        assertThat(body1).isNotEqualTo(body2);
    }
}
