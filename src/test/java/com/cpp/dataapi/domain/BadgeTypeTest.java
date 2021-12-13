package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BadgeTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BadgeType.class);
        BadgeType badgeType1 = new BadgeType();
        badgeType1.setId("id1");
        BadgeType badgeType2 = new BadgeType();
        badgeType2.setId(badgeType1.getId());
        assertThat(badgeType1).isEqualTo(badgeType2);
        badgeType2.setId("id2");
        assertThat(badgeType1).isNotEqualTo(badgeType2);
        badgeType1.setId(null);
        assertThat(badgeType1).isNotEqualTo(badgeType2);
    }
}
