package com.projeto.pedido.event;

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
