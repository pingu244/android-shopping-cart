package woowacourse.shopping.shopping

import woowacourse.shopping.database.ShoppingRepository
import woowacourse.shopping.model.ProductUiModel
import woowacourse.shopping.util.toUiModel

class ShoppingPresenter(
    private val view: ShoppingContract.View,
    private val repository: ShoppingRepository,
) : ShoppingContract.Presenter {

    private var numberOfReadProduct: Int = 0

    override fun loadProducts() {
        val products = selectProducts()
        val recentViewedProducts = repository.selectRecentViewedProducts().map { it.toUiModel() }

        view.setUpShoppingView(
            products = products,
            recentViewedProducts = recentViewedProducts,
            showMoreShoppingProducts = ::readMoreShoppingProducts,
        )
    }

    override fun readMoreShoppingProducts() {
        val products = selectProducts()

        view.refreshShoppingProductsView(products)
    }

    private fun selectProducts(): List<ProductUiModel> {
        val products = repository.selectProducts(
            from = numberOfReadProduct,
            count = COUNT_TO_READ,
        ).map { it.toUiModel() }

        numberOfReadProduct += products.size

        return products
    }

    override fun addToRecentViewedProduct(id: Int) {
        val product = repository.selectProductById(id)
        val recentViewedProducts = repository.selectRecentViewedProducts()
        val isDuplicated = recentViewedProducts.contains(product)
        if (isDuplicated) {
            repository.deleteFromRecentViewedProducts(id)
        }

        repository.insertToRecentViewedProducts(id)
    }

    override fun updateRecentViewedProducts() {
        val recentViewedProducts = repository.selectRecentViewedProducts().map { it.toUiModel() }
        view.refreshRecentViewedProductsView(recentViewedProducts)
    }

    companion object {
        private const val COUNT_TO_READ = 20
    }
}
