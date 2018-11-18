package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
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
     * T = O(Height);
     * R = O(1);
     */
    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        T element = (T)o;
        if (element == null) throw new NullPointerException();
        if (root == null) return false;
        if (!contains(o)) return false;

        Node<T> current = root;
        Node<T> parent = root;

        while (true) {
            int result = current.value.compareTo(element);
            if (result > 0) {
                parent = current;
                current = current.left;
            } else if (result < 0) {
                parent = current;
                current = current.right;
            } else {
                break;
            }
        }

        if (root.value == o) {
            if (root.left == null && root.right == null) root = null;
            else if (root.right == null) root = root.left;
            else if (root.left == null) root = root.right;
            else {
                current = current.right;
                while (current.left != null) current = current.left;
                current.left = root.left;
                root = root.right;
            }
            size--;
            return true;
        }


        if (current.right == null) {
            int result = parent.value.compareTo(current.value);
            if (result > 0) {
                parent.left = current.left;
            } else if (result < 0) {
                parent.right = current.left;
            }
        } else if (current.right.left == null) {
            current.right.left = current.left;

            int result = parent.value.compareTo(current.value);
            if (result > 0) {
                parent.left = current.right;
            } else if (result < 0) {
                parent.right = current.right;
            }

        } else {
            Node<T> leftmost = current.right.left;
            Node<T> leftmostParent = current.right;
            while (leftmost.left != null) {
                leftmostParent = leftmost;
                leftmost = leftmost.left;
            }
            leftmostParent.left = leftmost.right;
            leftmost.left = current.left;
            leftmost.right = current.right;

            int result = parent.value.compareTo(current.value);
            if (result > 0) {
                parent.left = leftmost;
            } else if (result < 0) {
                parent.right = leftmost;
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
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;

        /**
         * Поиск следующего элемента
         * Средняя
         * T = O(N); N - height
         * R = (1);
         */
        private Node<T> findNext() {
            Node<T> next = null;
            if (current == null) {
                Node<T> min = root;
                while (min.left != null) min = min.left;
                next = min;
            } else {
                Node<T> tempRoot = root;
                while (tempRoot != null) {
                    if (tempRoot.value.compareTo(current.value) <= 0) tempRoot = tempRoot.right;
                    else {
                        next = tempRoot;
                        tempRoot = tempRoot.left;
                    }
                }
            }
            return next;
        }


        @Override
        public boolean hasNext() {
            return findNext() != null;
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }


        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            BinaryTree.this.remove(current.value);
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
