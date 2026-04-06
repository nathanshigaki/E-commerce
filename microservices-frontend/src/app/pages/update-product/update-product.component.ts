import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from "@angular/forms";
import { ProductService } from "../../services/product/product.service";
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
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  productId: string = '';
  productUpdated = false;
    inventoryService: any;

  constructor(private fb: FormBuilder) {
    this.updateProductForm = this.fb.group({
      skucode: ['', [Validators.required]],
      nome: ['', [Validators.required]],
      descricao: ['', [Validators.required]],
      preco: [0, [Validators.required]]
    });
  }

  ngOnInit(): void {
    // Pega o ID da URL
    this.productId = this.route.snapshot.paramMap.get('id') || '';
    if (this.productId) {
      this.productService.getProductById(this.productId).subscribe(product => {
        // Preenche o formulário com os dados atuais do produto
        this.updateProductForm.patchValue(product);
      });
    }
  }

  onSubmit(): void {
    if (this.updateProductForm.valid) {
        const productData = this.updateProductForm.value;
        const skucode = this.updateProductForm.get('skucode')?.value;
        const novaQuantidade = this.updateProductForm.get('quantidade')?.value;

        this.productService.updateProduct(this.productId, productData).subscribe(() => {
            this.inventoryService.updateInventory(skucode, novaQuantidade).subscribe(() => {
                this.router.navigate(['/']);
            });
        });
    }
}
}