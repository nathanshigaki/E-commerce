import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Inventory} from "../../model/inventory";

@Injectable({
  providedIn: 'root'
})
export class InventoryService {
  private readonly url = 'http://localhost:9000/api/inventario';

  constructor(private httpClient: HttpClient) {}

  getInventarios(): Observable<Array<Inventory>> {
    return this.httpClient.get<Array<Inventory>>(this.url);
  }

  createInventario(inventory: Inventory): Observable<Inventory> {
    return this.httpClient.post<Inventory>(this.url, inventory);
  }

  updateInventario(id: number, inventory: Inventory): Observable<Inventory> {
    return this.httpClient.put<Inventory>(`${this.url}/${id}`, inventory);
  }

  deleteInventario(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.url}/${id}`);
  }
}