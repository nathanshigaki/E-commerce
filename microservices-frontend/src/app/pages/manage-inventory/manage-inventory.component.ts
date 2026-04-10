import { Component, OnInit, inject } from '@angular/core';
import { InventoryService } from '../../services/inventory/inventory.service';
import { Inventory } from '../../model/inventory';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-manage-inventory',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './manage-inventory.component.html'
})
export class ManageInventoryComponent implements OnInit {
  private readonly inventoryService = inject(InventoryService);
  inventarios: Inventory[] = [];

  ngOnInit() {
    this.carregarInventario();
  }

  carregarInventario() {
    this.inventoryService.getInventarios().subscribe(data => this.inventarios = data);
  }

  atualizarEstoque(item: Inventory) {
    if (item.id) {
      this.inventoryService.updateInventario(item.id, item).subscribe(() => {
        alert(`Estoque do SKU ${item.skucode} atualizado!`);
      });
    }
  }

  trackByFn(_index: number, item: any) {
    return item.skucode; 
  }
}