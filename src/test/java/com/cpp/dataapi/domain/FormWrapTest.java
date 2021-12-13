package com.cpp.dataapi.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.cpp.dataapi.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormWrapTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormWrap.class);
        FormWrap formWrap1 = new FormWrap();
        formWrap1.setId("id1");
        FormWrap formWrap2 = new FormWrap();
        formWrap2.setId(formWrap1.getId());
        assertThat(formWrap1).isEqualTo(formWrap2);
        formWrap2.setId("id2");
        assertThat(formWrap1).isNotEqualTo(formWrap2);
        formWrap1.setId(null);
        assertThat(formWrap1).isNotEqualTo(formWrap2);
    }
}
