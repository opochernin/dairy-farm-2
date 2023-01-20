package ua.pochernin.collection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LinkedTreeNodeImpl<K, V> implements LinkedTreeNode<K, V> {

    @Getter
    private final K key;
    @Getter
    private final V value;
    private LinkedTreeNodeImpl<K, V> parent;
    private LinkedTreeNodeImpl<K, V> firstChild;
    private LinkedTreeNodeImpl<K, V> next;

    @Override
    public void add(K key, V value) {
        final var newNode = new LinkedTreeNodeImpl<>(key, value);
        newNode.parent = this;

        if (firstChild == null) {
            firstChild = newNode;
            return;
        }

        var last = firstChild;
        while (last.next != null) {
            last = last.next;
        }
        last.next = newNode;
    }


    @Override
    public LinkedTreeNode<K, V> findNode(K key) {
        return findNode(key, findRoot());
    }

    @Override
    public LinkedTreeNode<K, V> getFirstChild() {
        return firstChild;
    }

    @Override
    public LinkedTreeNode<K, V> getNext() {
        return next;
    }

    private LinkedTreeNodeImpl<K, V> findRoot() {
        if (parent == null) {
            return this;
        }
        var root = parent;
        while (root.parent != null) {
            root = root.parent;
        }
        return root;
    }

    private LinkedTreeNodeImpl<K, V> findNode(K key, LinkedTreeNodeImpl<K, V> node) {

        if (key.equals(node.getKey())) {
            return node;
        }

        if (node.next != null) {
            var result = findNode(key, node.next);
            if (result != null) {
                return result;
            }
        }

        if (node.firstChild != null) {
            return findNode(key, node.firstChild);
        }

        return null;
    }
}
