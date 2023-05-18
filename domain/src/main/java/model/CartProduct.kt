package model

data class CartProduct(
    val product: Product,
    val count: Int,
    val isSelected: Boolean,
)
