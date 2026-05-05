package com.projeto.pedido_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoFeitoEvent {
    private String numeroPedido;
    private String email;
}
