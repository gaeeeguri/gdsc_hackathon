package com.sgp.gdsc_hackathon.foo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FooController {

    @GetMapping("/foo")
    @Operation(summary = "Get Foo", description = "Returns a single Foo")
    @ApiResponses(value= {
        @ApiResponse(responseCode = "200", description = "Foo found"),
        @ApiResponse(responseCode = "404", description = "Foo not found")
    })
    public String getFoo() {
        return "Foo";
    }
}
