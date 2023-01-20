package ua.pochernin.collection;

import lombok.Getter;

public class LinkedTreeNode<K, V> {

    private final K id;
    @Getter
    private final V value;
    @Getter
    private LinkedTreeNode<K, V> child;
    @Getter
    private LinkedTreeNode<K, V> next;

    public LinkedTreeNode(K id, V value) {
        this.id = id;
        this.value = value;
    }

    public void add(LinkedTreeNode<K, V> linkedTreeNode, K parentId) {
        final LinkedTreeNode<K, V> parent = findById(parentId, this);

        if (parent == null) {
            throw new IllegalArgumentException();
        }

        if (parent.child == null) {
            parent.child = linkedTreeNode;
        } else {
            LinkedTreeNode<K, V> last = parent.child;
            while (last.next != null) {
                last = last.next;
            }
            last.next = linkedTreeNode;
        }
    }

    public LinkedTreeNode<K, V> findById(K id) {
        if (this.id.equals(id)) {
            return this;
        }

        if (this.child == null) {
            return null;
        }

        return findById(id, this);
    }

    private LinkedTreeNode<K, V> findById(K id, LinkedTreeNode<K, V> linkedTreeNode) {
        if (linkedTreeNode.id.equals(id)) {
            return linkedTreeNode;
        }

        if (linkedTreeNode.next != null) {
            final LinkedTreeNode<K, V> result = findById(id, linkedTreeNode.next);
            if (result != null) {
                return result;
            }
        }

        if (linkedTreeNode.child != null) {
            return findById(id, linkedTreeNode.child);
        }

        return null;
    }
}
