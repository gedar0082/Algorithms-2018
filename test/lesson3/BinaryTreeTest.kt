package lesson3

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Assertions
import java.lang.NullPointerException
import kotlin.test.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BinaryTreeTest {
    private fun testAdd(create: () -> CheckableSortedSet<Int>) {
        val tree = create()
        tree.add(10)
        tree.add(5)
        tree.add(7)
        tree.add(10)
        assertEquals(3, tree.size)
        assertTrue(tree.contains(5))
        tree.add(3)
        tree.add(1)
        tree.add(3)
        tree.add(4)
        assertEquals(6, tree.size)
        assertFalse(tree.contains(8))
        tree.add(8)
        tree.add(15)
        tree.add(15)
        tree.add(20)
        assertEquals(9, tree.size)
        assertTrue(tree.contains(8))
        assertTrue(tree.checkInvariant())
        assertEquals(1, tree.first())
        assertEquals(20, tree.last())
    }

    @Test
    @Tag("Example")
    fun testAddKotlin() {
        testAdd { createKotlinTree() }
    }

    @Test
    @Tag("Example")
    fun testAddJava() {
        testAdd { createJavaTree() }
    }

    private fun <T : Comparable<T>> createJavaTree(): CheckableSortedSet<T> = BinaryTree()

    private fun <T : Comparable<T>> createKotlinTree(): CheckableSortedSet<T> = KtBinaryTree()

    private fun testRemove(create: () -> CheckableSortedSet<Int>) {
        //содержание элемента
        val toRemove = 19
        val binarySet = create()
        binarySet += 17
        binarySet += 18
        assertEquals(false, binarySet.remove(toRemove))
        //когда удаляем null
        val r = null;
        val bS = create()
        bS += 19
        Assertions.assertThrows(NullPointerException::class.java) { bS.remove(r) }
        //когда удаляемое значение - root (без детей)
        println("first test")
        val deletableFst = 19
        val treeSetFst = TreeSet<Int>()
        val binarySetFst = create()
        treeSetFst += deletableFst
        binarySetFst += deletableFst
        treeSetFst.remove(deletableFst)
        binarySetFst.remove(deletableFst)
        assertEquals<SortedSet<*>>(binarySetFst, treeSetFst, "removing")
        //когда удаляемое значение - root (есть только левая ветка)
        println("second test")
        val deletableScd = 19
        val rand = Random()
        val treeSetScd = TreeSet<Int>()
        val binarySetScd = create()
        val listScd = mutableListOf<Int>()
        for (i in 1..20) {
            listScd.add(rand.nextInt(deletableScd - 1))
        }
        treeSetScd += deletableScd
        binarySetScd += deletableScd
        for (element in listScd) {
            treeSetScd += element
            binarySetScd += element
        }
        treeSetScd.remove(deletableScd)
        binarySetScd.remove(deletableScd)
        assertEquals<SortedSet<*>>(binarySetScd, treeSetScd, "removing")
        //когда удаляемое значение - root (есть только правая ветка)
        println("third test")
        val deletableTrd = 19
        val randTrd = Random()
        val treeSetTrd = TreeSet<Int>()
        val binarySetTrd = create()
        val listTrd = mutableListOf<Int>()
        for (i in 1..20) {
            listTrd.add(randTrd.nextInt(100) + deletableFst - 1)
        }
        treeSetTrd += deletableTrd
        binarySetTrd += deletableTrd
        for (element in listTrd) {
            treeSetTrd += element
            binarySetTrd += element
        }
        treeSetTrd.remove(deletableTrd)
        binarySetTrd.remove(deletableTrd)
        assertEquals<SortedSet<*>>(binarySetTrd, treeSetTrd, "removing")
        //когда удаляемое значение - root (обе ветки)
        println("IV test")
        val deletableFth = 19
        val randFth = Random()
        val treeSetFth = TreeSet<Int>()
        val binarySetFth = create()
        val listFth = mutableListOf<Int>()
        for (i in 1..20) {
            listFth.add(randFth.nextInt(100))
        }
        treeSetFth += deletableFth
        binarySetFth += deletableFth
        for (element in listFth) {
            treeSetFth += element
            binarySetFth += element
        }
        treeSetFth.remove(deletableFth)
        binarySetFth.remove(deletableFth)
        assertEquals<SortedSet<*>>(binarySetFth, treeSetFth, "removing")
        //пустое дерево
        val treeSetEmpty = TreeSet<Int>()
        val binarySetEmpty = create()
        treeSetEmpty.remove(19)
        binarySetEmpty.remove(19)
        assertEquals<SortedSet<*>>(binarySetEmpty, treeSetEmpty, "removing")
        //native tests
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            binarySet.remove(toRemove)
            println("Removing $toRemove from $list")
            assertEquals<SortedSet<*>>(treeSet, binarySet, "After removal of $toRemove from $list")
            assertEquals(treeSet.size, binarySet.size)
            for (element in list) {
                val inn = element != toRemove
                assertEquals(inn, element in binarySet,
                        "$element should be ${if (inn) "in" else "not in"} tree")
            }
            assertTrue(binarySet.checkInvariant())
        }
    }

    @Test
    @Tag("Normal")
    fun testRemoveKotlin() {
        testRemove { createKotlinTree() }
    }

    @Test
    @Tag("Normal")
    fun testRemoveJava() {
        testRemove { createJavaTree() }
    }

    private fun testIterator(create: () -> CheckableSortedSet<Int>) {
        //пустой итератор
        val treeSetFst = TreeSet<Int>()
        val binarySetFst = create()
        val treeIt = treeSetFst.iterator()
        val binaryIt = binarySetFst.iterator()
        while (treeIt.hasNext()) {
            assertEquals(treeIt.next(), binaryIt.next())
        }
        //один элемент
        val treeSetScd = TreeSet<Int>()
        val binarySetScd = create()
        treeSetScd += 19
        binarySetScd += 19
        val treeItScd = treeSetScd.iterator()
        val binaryItScd = binarySetScd.iterator()
        while (treeItScd.hasNext()) {
            assertEquals(treeItScd.next(), binaryItScd.next())
        }
        //native
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val treeIt = treeSet.iterator()
            val binaryIt = binarySet.iterator()
            while (treeIt.hasNext()) {
                assertEquals(treeIt.next(), binaryIt.next())
            }
        }
    }

    @Test
    @Tag("Normal")
    fun testIteratorKotlin() {
        testIterator { createKotlinTree() }
    }

    @Test
    @Tag("Normal")
    fun testIteratorJava() {
        testIterator { createJavaTree() }
    }

    private fun testIteratorRemove(create: () -> CheckableSortedSet<Int>) {
        //проверка постановки каретки
        val bSet = create()
        bSet += 19
        bSet += 20
        bSet.remove(19)
        val iterator = bSet.iterator()
        val element = iterator.next()
        assertEquals(element, 20)
        //native tests
        val random = Random()
        for (iteration in 1..100) {
            val list = mutableListOf<Int>()
            for (i in 1..20) {
                list.add(random.nextInt(100))
            }
            val treeSet = TreeSet<Int>()
            val binarySet = create()
            for (element in list) {
                treeSet += element
                binarySet += element
            }
            val toRemove = list[random.nextInt(list.size)]
            treeSet.remove(toRemove)
            println("Removing $toRemove from $list")
            val iterator = binarySet.iterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                print("$element ")
                if (element == toRemove) {
                    iterator.remove()
                }
            }

            assertEquals<SortedSet<*>>(treeSet, binarySet, "After removal of $toRemove from $list")
            assertEquals(treeSet.size, binarySet.size)
            for (element in list) {
                val inn = element != toRemove
                assertEquals(inn, element in binarySet,
                        "$element should be ${if (inn) "in" else "not in"} tree")
            }
            assertTrue(binarySet.checkInvariant())
        }
    }

    @Test
    @Tag("Hard")
    fun testIteratorRemoveKotlin() {
        testIteratorRemove { createKotlinTree() }
    }

    @Test
    @Tag("Hard")
    fun testIteratorRemoveJava() {
        testIteratorRemove { createJavaTree() }
    }
}