package com.openwt.officetracking.api.contract_type;

import com.openwt.officetracking.domain.contract_type.ContractType;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RestController
@RequestMapping("/api/v1/contract-types")
public class ContractTypeController {

    @Operation(summary = "Find all contract types")
    @GetMapping
    public List<ContractType> getCurrentContent() {
        return asList(ContractType.values());
    }
}
