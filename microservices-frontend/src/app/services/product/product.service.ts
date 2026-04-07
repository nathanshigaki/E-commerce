import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "../../model/product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private httpClient: HttpClient) {
  }

  getProducts(): Observable<Array<Product>> {
    return this.httpClient.get<Array<Product>>('http://localhost:9000/api/produto');
  }

  createProduct(product: Product): Observable<Product> {
    return this.httpClient.post<Product>('http://localhost:9000/api/produto', product);
  }

  getProductById(id: string): Observable<Product> {
    return this.httpClient.get<Product>(`http://localhost:9000/api/produto/${id}`);
  }

  updateProduct(id: string, product: Product): Observable<Product> {
    return this.httpClient.patch<Product>(`http://localhost:9000/api/produto/${id}`, product);
  }
}
