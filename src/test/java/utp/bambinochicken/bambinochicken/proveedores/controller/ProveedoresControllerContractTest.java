package utp.bambinochicken.bambinochicken.proveedores.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utp.bambinochicken.bambinochicken.proveedores.service.ProveedoresService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProveedoresControllerContractTest {

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        ProveedoresService proveedoresService = mock(ProveedoresService.class);
        when(proveedoresService.listProveedores(any())).thenReturn(List.of());
        when(proveedoresService.createProveedor(any())).thenReturn(null);
        mockMvc = MockMvcBuilders.standaloneSetup(new ProveedoresController(proveedoresService)).build();
    }

    @Test
    void shouldReturn200WhenGetProveedores() throws Exception {
        mockMvc.perform(get("/api/v1/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Proveedores obtenidos"));
    }

    @Test
    void shouldReturn200WhenPostProveedores() throws Exception {
        String payload = """
                {
                  "nombre": "Proveedor Demo",
                  "ruc": "12345678901",
                  "telefono": "999888777",
                  "correo": "proveedor@bambino.com",
                  "estado": 1
                }
                """;

        mockMvc.perform(post("/api/v1/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Proveedor creado"));
    }
}
