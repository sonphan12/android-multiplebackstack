package io.github.sonphan12.myplayground

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayDeque
import java.util.EmptyStackException
import java.util.LinkedHashMap
import java.util.NoSuchElementException

/**
 * A stack of key/stack pairs. Behaves as both a Stack and a Map, and the value of each entry is also a Stack.
 * K is the Key type and V is the type of the elements in the nested stacks.
 */
internal class StackOfStacks() : Parcelable {

    private val listOfStacks: LinkedHashMap<Int, Stack<InternalTag>> = LinkedHashMap()

    constructor(parcel: Parcel) : this() {
        val size = parcel.readInt()
        for (i in 0 until size) {
            val key = parcel.readInt()
            val list = mutableListOf<InternalTag>()
            parcel.readTypedList(list, InternalTag.CREATOR)
            listOfStacks[key] = list.toStack()
        }
    }

    fun stackExists(key: Int) = listOfStacks[key] != null && !listOfStacks[key]!!.isEmpty()

    /**
     * Pushes a value on to the given key's stack and moves that stack to the top.
     */
    fun push(key: Int, value: InternalTag) {
        var stack = listOfStacks[key]
        if (stack == null) {
            stack = Stack()
            listOfStacks[key] = stack
        } else {
            moveToTop(key)
        }

        stack.push(value)
    }

    fun moveToTop(key: Int) {
        val stackToMove = listOfStacks[key]
        if (stackToMove != null && peekKey() != key) {
            listOfStacks.remove(key)
            listOfStacks[key] = stackToMove
        }
    }

    fun pop(): InternalTag? {
        return try {
            getTopStack().second.pop()
        } catch (e: EmptyStackException) {
            null
        }
    }

    /**
     * Removes the specified key and its corresponding stack from this map.
     */
    fun remove(key: Int) {
        listOfStacks.remove(key)
    }

    /**
     * Returns the stack to which the specified key is mapped,
     * or `null` if this map contains no mapping for the key.
     * The top of the stack is the last element in the returned list.
     */
    operator fun get(key: Int): List<InternalTag>? = listOfStacks[key]?.asList()

    /**
     * returns the key/value Pair at the top of the stack
     */
    fun peek(): Pair<Int, InternalTag>? {
        return try {
            val (key, stack) = getTopStack()
            Pair(key, stack.peek())
        } catch (e: EmptyStackException) {
            null
        }
    }

    /**
     * returns the key at the top of the stack
     */
    fun peekKey(): Int? {
        return try {
            val (key, _) = getTopStack()
            return key
        } catch (e: EmptyStackException) {
            null
        }
    }

    /**
     * returns the value at the top of the stack of the top stack
     */
    fun peekValue(): InternalTag? {
        return try {
            val (_, value) = getTopStack()
            return value.peek()
        } catch (e: EmptyStackException) {
            null
        }
    }

    /**
     * Removes all the keys from the map
     */
    fun clear() {
        listOfStacks.clear()
    }

    /**
     * Prunes empty stacks
     * Can throw EmptyStackException
     */
    private fun getTopStack(): Pair<Int, Stack<InternalTag>> {
        // prune empty stacks
        var (topKey, topStack) = try {
            listOfStacks.entries.last()
        } catch (e: NoSuchElementException) {
            throw EmptyStackException()
        }
        while (topStack.isEmpty()) {
            listOfStacks.remove(topKey)
            if (listOfStacks.isEmpty()) throw EmptyStackException()
            val topEntry = listOfStacks.entries.last()
            topKey = topEntry.key
            topStack = topEntry.value
        }

        return Pair(topKey, topStack)
    }

    fun keys() = listOfStacks.keys


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        val list = listOfStacks.toList()
        parcel.writeInt(list.size)
        list.forEach {
            parcel.writeInt(it.first)
            parcel.writeTypedList(it.second.asList())
        }
    }

    override fun describeContents(): Int = 0

    companion object {
        @SuppressWarnings("UnusedDeclaration")
        @JvmField
        val CREATOR = object : Parcelable.Creator<StackOfStacks> {
            override fun createFromParcel(parcel: Parcel): StackOfStacks {
                return StackOfStacks(parcel)
            }

            override fun newArray(size: Int): Array<StackOfStacks?> {
                return arrayOfNulls(size)
            }
        }
    }
}

/**
 * ArrayDeque based Stack. Because java.util.Stack is Vector based and synchronised and slow.
 */
private class Stack<T> : Iterable<T> {
    private val dequeue = ArrayDeque<T>()

    fun size() = dequeue.size

    fun isEmpty() = dequeue.isEmpty()

    fun push(e: T) = dequeue.addLast(e)

    fun pop(): T = dequeue.removeLast()

    fun peek(): T = dequeue.peekLast()

    override fun iterator(): Iterator<T> = dequeue.descendingIterator()

    fun asList() = ArrayList<T>(dequeue.size).apply { addAll(dequeue) }
}

private fun <T> List<T>.toStack(): Stack<T> {
    val stack = Stack<T>()
    forEach {
        stack.push(it)
    }
    return stack
}
