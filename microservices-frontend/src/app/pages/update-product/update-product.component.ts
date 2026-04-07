import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ProductService } from "../../services/product/product.service";
import { InventoryService } from "../../services/inventory/inventory.service"; 
import { ActivatedRoute, Router } from "@angular/router";
import { NgIf } from "@angular/common";

@Component({
  selector: 'app-update-product',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './update-product.component.html'
})
export class UpdateProductComponent implements OnInit {
  updateProductForm: FormGroup;
  private readonly productService = inject(ProductService);
  private readonly inventoryService = inject(InventoryService); 
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  
  productId: string = '';
  productUpdated = false;
  inventoryId?: number;

  constructor(private fb: FormBuilder) {
    this.updateProductForm = this.fb.group({
      skucode: ['', [Validators.required]],
      nome: ['', [Validators.required]],
      descricao: ['', [Validators.required]],
      preco: [0, [Validators.required]],
      quantidade: [0, [Validators.required, Validators.min(0)]] 
    });
  }

  ngOnInit(): void {
    this.productId = this.route.snapshot.paramMap.get('id') || '';
    if (this.productId) {
      this.productService.getProductById(this.productId).subscribe(product => {
        this.updateProductForm.patchValue(product);
        this.inventoryService.getInventoryBySkucode(product.skucode).subscribe(inv => {
          this.inventoryId = inv.id;
          this.updateProductForm.patchValue({ quantidade: inv.quantidade });
        });
      });
    }
  }

  onSubmit(): void {
    if (this.updateProductForm.valid) {
        const productData = this.updateProductForm.value;
        const novaQuantidade = this.updateProductForm.get('quantidade')?.value;

        this.productService.updateProduct(this.productId, productData).subscribe(() => {
            if (this.inventoryId) {
                const inventoryData = { 
                  id: this.inventoryId, 
                  skucode: productData.skucode, 
                  quantidade: novaQuantidade 
                };
                this.inventoryService.updateInventario(this.inventoryId, inventoryData).subscribe(() => {
                    this.productUpdated = true;
                    setTimeout(() => this.router.navigate(['/']), 2000);
                });
            }
        });
    }
  }
}