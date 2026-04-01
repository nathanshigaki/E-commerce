export interface Order {
  id?: number;
  numeroPedido?: string;
  skucode: string;
  preco: number;
  quantidade: number;
  userDetails: UserDetails
}

export interface UserDetails {
  email: string;
  firstName: string;
  lastName: string;
}
