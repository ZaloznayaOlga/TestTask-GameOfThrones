package ru.skillbranch.gameofthrones.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun List<String>.dropLastUntil(predicate: (String) -> Boolean): String {
    val array = this
    for ((index, indexOfWord) in array.withIndex()) {
        if (predicate(indexOfWord))
            return array[index - 1]
    }
    return ""
}

fun <T, A, B> LiveData<A>.combineAndCompute(other: LiveData<B>, onChange : (A,B) -> T) : MediatorLiveData<T>{
    var source1emitted = false
    var source2emitted = false

    val result = MediatorLiveData<T>()

    val mergeF = {
        val source1Value = this.value
        val source2Value = other.value

        if(source1emitted && source2emitted){
            result.value = onChange.invoke(source1Value!!, source2Value!!)
        }
    }
    result.addSource(this) {source1emitted = true ; mergeF.invoke()}
    result.addSource(other) {source2emitted = true ; mergeF.invoke()}

    return  result
}