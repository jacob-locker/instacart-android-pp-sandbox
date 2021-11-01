package com.instacart.android.challenges

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.instacart.android.challenges.network.DeliveryItem
import com.instacart.android.challenges.network.NetworkService
import com.instacart.android.challenges.network.OrderResponse
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    interface UpdateListener {
        fun onUpdate(state: ItemListViewState)
        fun onError(throwable: Throwable?)
    }

    private var itemListViewState: ItemListViewState
    private var listener: UpdateListener? = null

    init {
        itemListViewState = ItemListViewState("Delivery Items", emptyList())

        val networkService = NetworkService()

        networkService.api.fetchOrdersObservable()
            .subscribeOn(Schedulers.io())
            .flatMap {
                val fetchOrderObservables = it.orders.map { id ->
                    networkService.api.fetchOrderByIdObservable(id)
                }

                Observable.zip(fetchOrderObservables) { zipper ->
                    zipper.map { it as OrderResponse }
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responses -> handleOrderResponses(responses) }) { t -> handleError(t) }
    }

    private fun handleError(throwable: Throwable?) {
        listener?.onError(throwable)
    }

    private fun handleOrderResponses(responses: List<OrderResponse>) {
        val deliveryItems = responses.flatMap { it.items }

        val rowItems = deliveryItems.groupBy { it.name }.map {
            DeliveryItem(it.value[0].id, it.value[0].name, it.value[0].imageUrl, it.value.fold(0) { acc, deliveryItem ->
                acc + deliveryItem.count
            })
        }.map { item ->
            ItemRow(item.name, item.count)
        }

        itemListViewState = ItemListViewState("Delivery Items", rowItems)
        listener?.onUpdate(itemListViewState)
    }

    fun setStateUpdateListener(listener: UpdateListener?) {
        this.listener = listener

        listener?.onUpdate(itemListViewState)
    }
}
