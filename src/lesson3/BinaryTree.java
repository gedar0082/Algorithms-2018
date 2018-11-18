package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.omg.CORBA.NO_IMPLEMENT;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) return false;
        System.out.println(1);
        if (root == null) return false;
        System.out.println(2);

        Node<T> current = root;
        Node<T> parent = root;
        boolean l = true;
        System.out.println("null");
        System.out.println(o);


        while (l)
        {
            int result = current.value.compareTo((T)o);

            if (result > 0)
            {
                parent = current;
                current = current.left;
            }
            else if (result < 0)
            {
                parent = current;
                current = current.right;
            }
            else
            {
                l = false;
            }
        }
        System.out.println("toRemove " + current.value);

        if (current.right == null) {
            System.out.println("FIRST");
            if (parent == null) {
                System.out.println("parent");
                root = current.left;
            } else {
                System.out.println("normal");
                int result = parent.value.compareTo(current.value);
                if (result > 0) {
                    System.out.println("result >0");
                    parent.left = current.left;
                } else if (result < 0) {
                    System.out.println("result < 0");
                    parent.right = current.left;
                }
            }
        }
        else if (current.right.left == null) {
            System.out.println("SECOND");
            current.right.left = current.left;
            if (parent == null) {
                root = current.right;
            } else {
                int result = parent.value.compareTo(current.value);
                if (result > 0) {
                    parent.left = current.right;
                } else if (result < 0) {
                    parent.right = current.right;
                }
            }
        }
        else {
            System.out.println("THIRD");
            Node<T> leftmost = current.right.left;
            Node<T> leftmostParent = current.right;
            while (leftmost.left != null) {
                leftmostParent = leftmost;
                leftmost = leftmost.left;
            }
            leftmostParent.left = leftmost.right;
            leftmost.left = current.left;
            leftmost.right = current.right;
            if (parent == null) {
                root = leftmost;
            } else {
                int result = parent.value.compareTo(current.value);
                if (result > 0) {
                    parent.left = leftmost;
                } else if (result < 0) {
                    parent.right = leftmost;
                }
            }
        }
        size--;
        return true;
    }



    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;
        private Stack<Node<T>> stack;

        private BinaryTreeIterator() {
            stack = new Stack<>();
            while (root != null){
                stack.push(root);
                root = root.left;
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private Node<T> findNext() {
            return stack.pop();

        }

        @Override
        public boolean hasNext() {
            return !stack.empty();
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            while (current.right != null) {
                stack.push(current.right);
                current.right = current.right.left;
            }
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            // TODO
            throw new NotImplementedError();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        // TODO
        throw new NotImplementedError();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
