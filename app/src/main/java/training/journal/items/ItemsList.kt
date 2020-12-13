package training.journal.items

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ItemsList<Item>(var items: MutableList<Item>) {

    private val addingSubject: PublishSubject<Item> = PublishSubject.create()
    private val removingSubject: PublishSubject<Int> = PublishSubject.create()
    private val updatingSubject: PublishSubject<Int> = PublishSubject.create()
    private val replacingSubject: PublishSubject<List<Item>> = PublishSubject.create()

    fun addingSubject(): Observable<Item> {
        return addingSubject
    }

    fun removingSubject(): Observable<Int> {
        return removingSubject
    }

    fun updatingSubject(): Observable<Int> {
        return updatingSubject
    }

    fun replacingSubject(): Observable<List<Item>> {
        return replacingSubject
    }

    fun add(item: Item) {
        items.add(0, item)
        addingSubject.onNext(item)
    }

    fun remove(item: Item) {
        val ind = items.indexOf(item)
        items.remove(item)
        removingSubject.onNext(ind)
    }

    fun update(position: Int, item: Item) {
        items[position] = item
        updatingSubject.onNext(position)
    }

    fun setData(data: MutableList<Item>) {
        items = data
        replacingSubject.onNext(items)
    }

    fun size(): Int {
        return items.size
    }

    fun contains(item: Item): Boolean {
        return items.contains(item)
    }
}