package com.openwt.officetracking.api.contract_type;

import com.openwt.officetracking.api.AbstractControllerTest;
import com.openwt.officetracking.api.WithMockAdmin;
import com.openwt.officetracking.domain.auth.AuthsProvider;
import com.openwt.officetracking.domain.contract_type.ContractType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContractTypeController.class)
@ExtendWith(SpringExtension.class)
class ContractTypeControllerTest extends AbstractControllerTest {

    private static final String BASE_URL = "/api/v1/contract-types";

    @MockBean
    private AuthsProvider authsProvider;

    @BeforeEach
    void init() {
        when(authsProvider.getCurrentAuthentication()).thenCallRealMethod();
    }

    @Test
    @WithMockAdmin
    void shouldGetCurrentContent_Ok() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]").value(ContractType.EMPLOYEE.name()))
                .andExpect(jsonPath("$[1]").value(ContractType.INTERN.name()))
                .andExpect(jsonPath("$[2]").value(ContractType.SHORT_TERM.name()));
    }
}