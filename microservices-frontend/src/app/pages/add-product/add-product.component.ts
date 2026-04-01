import {Component, inject} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Product} from "../../model/product";
import {ProductService} from "../../services/product/product.service";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-add-product',
  standalone: true,
  imports: [ReactiveFormsModule, NgIf],
  templateUrl: './add-product.component.html',
  styleUrl: './add-product.component.css'
})
export class AddProductComponent {
  addProductForm: FormGroup;
  private readonly productService = inject(ProductService);
  productCreated = false;

  constructor(private fb: FormBuilder) {
    this.addProductForm = this.fb.group({
      skuCode: ['', [Validators.required]],
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: [0, [Validators.required]]
    })
  }

  onSubmit(): void {
    if (this.addProductForm.valid) {
      const product: Product = {
        skucode: this.addProductForm.get('skucode')?.value,
        nome: this.addProductForm.get('nome')?.value,
        descricao: this.addProductForm.get('descricao')?.value,
        preco: this.addProductForm.get('preco')?.value
      }
      this.productService.createProduct(product).subscribe(product => {
        this.productCreated = true;
        this.addProductForm.reset();
      })
    } else {
      console.log('Form is not valid');
    }
  }

  get skucode() {
    return this.addProductForm.get('skucode');
  }

  get nome() {
    return this.addProductForm.get('nome');
  }

  get descricao() {
    return this.addProductForm.get('descricao');
  }

  get preco() {
    return this.addProductForm.get('preco');
  }
}
