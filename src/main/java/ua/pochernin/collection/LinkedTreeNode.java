package ua.pochernin.collection;

public interface LinkedTreeNode<K, V> {

    K getKey();

    V getValue();

    void add(K key, V value);

    LinkedTreeNode<K, V> findNode(K key);

    LinkedTreeNode<K, V> getFirstChild();

    LinkedTreeNode<K, V> getNext();

}
