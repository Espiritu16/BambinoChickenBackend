package utp.bambinochicken.bambinochicken.ventas.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import utp.bambinochicken.bambinochicken.ventas.dto.VentaResumenDiarioResponse;
import utp.bambinochicken.bambinochicken.ventas.service.VentasService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VentasControllerContractTest {

    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        VentasService ventasService = mock(VentasService.class);
        when(ventasService.listVentas()).thenReturn(List.of());
        when(ventasService.registrarVenta(any())).thenReturn(null);
        when(ventasService.resumenDiario(any())).thenReturn(new VentaResumenDiarioResponse("2026-01-01", 1L, 0, 0.0));

        mockMvc = MockMvcBuilders.standaloneSetup(new VentasController(ventasService)).build();
    }

    @Test
    void shouldReturn200WhenGetVentas() throws Exception {
        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Ventas obtenidas"));
    }

    @Test
    void shouldReturn200WhenPostVentas() throws Exception {
        String payload = """
                {
                  "idLocal": 1,
                  "idCaja": 1,
                  "tipoComprobante": "BOLETA",
                  "serieComprobante": "B001",
                  "numeroComprobante": "000001",
                  "metodoPago": "EFECTIVO",
                  "subtotal": 100.0,
                  "igv": 18.0,
                  "total": 118.0
                }
                """;

        mockMvc.perform(post("/api/v1/ventas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Venta registrada"));
    }
}
