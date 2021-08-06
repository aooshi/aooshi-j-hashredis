package org.aooshi.j;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 一致性哈希
 */
@Slf4j
class ConsistentHashing<T> {

    private SortedMap<Integer, T> nodeMap = new TreeMap<Integer, T>();

    /**
     * get nodes
     */
    @Getter
    private T[] nodes;

    /**
     * get or set virtual node number
     */
    @Getter
    @Setter
    private Integer virtualNumber = 5;

    /**
     * 节点初始化
     * T 需要支持通过 toString() 方法获取节点名或唯一标识
     *
     * @param nodes
     */
    public ConsistentHashing(T[] nodes) {
        this.nodes = nodes;
        this.reset(nodes);
    }

    /**
     * 重置节点
     *
     * @param nodes
     */
    public void reset(T[] nodes) {
        SortedMap<Integer, T> nodeMap = new TreeMap<Integer, T>();
        for (T node : nodes) {
            for (int i = 0; i < this.virtualNumber; i++) {
                String n = i + "&&" + node.toString();
                int code = this.getCode(n);
                // System.out.println(code + " : " + n);
                nodeMap.put(code, node);
            }
        }
        this.nodeMap = nodeMap;
    }

    /**
     * 获取一个节点
     *
     * @param key
     */
    public T getNode(String key) {
        int code = this.getCode(key);
        return this.getNode(code);
    }

    /**
     * 获取一个节点
     *
     * @param code
     * @return not find return null
     */
    public T getNode(Integer code) {
        SortedMap<Integer, T> nodeMap = this.nodeMap;
        if (nodeMap.size() == 0)
            return null;

        SortedMap<Integer, T> sortedMap = nodeMap.tailMap(code);
        //
        code = sortedMap.isEmpty() ? nodeMap.lastKey() : sortedMap.firstKey();
        return sortedMap.get(code);
    }

    /**
     * FNV1_32_HASH
     *
     * @param value 任意字符串
     * @return
     */
    private int getCode(String value) {
        if (value == null) return 0;
        final int p = 16777619;
        int hash = (int) 2166136261L;
        for (int i = 0; i < value.length(); i++) {
            hash = (hash ^ value.charAt(i)) * p;
        }
        hash += hash << 13;
        hash ^= hash >> 7;
        hash += hash << 3;
        hash ^= hash >> 17;
        hash += hash << 5;
        //
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return hash;
    }

    /**
     * get node maps
     *
     * @return
     */
    public Map<Integer, T> getNodeMaps() {
        return this.nodeMap;
    }

    /**
     * print nodes
     */
    public void printNodes() {
        log.info(this.getClass().getSimpleName() + " -- nodes");
        for (T node : this.nodes) {
            log.info(node.toString());
        }

        log.info(this.getClass().getSimpleName() + " -- node maps");
        this.nodeMap.forEach((k, v) -> {
            log.info(k + " : " + v);
        });
    }
}
