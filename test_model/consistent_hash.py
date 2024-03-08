import hashlib
import bisect


"""
    # https://www.xiaolincoding.com/os/8_network_system/hash.html#%E4%BD%BF%E7%94%A8%E5%93%88%E5%B8%8C%E7%AE%97%E6%B3%95%E6%9C%89%E4%BB%80%E4%B9%88%E9%97%AE%E9%A2%98
    1. 定义：
        machines/physical machines: 用于实际处理请求的物理机器
        replicas/virtual machines: 模拟用于处理请求的物理机器
    2. 特点:
        * 在一致哈希算法中，如果增加或者移除一个节点，仅影响该节点在哈希环上顺时针相邻的后继节点，其它数据也不会受到影响。
          很好的减少了当node扩缩容时候，旧node上的数据的迁移问题；普通hash需要迁移所有的node数据，一致性hash只需要迁移1个node数据
        * 存在节点分布不均匀的问题, nodes的值可能存在于hash环中过于集中的位置, 某个nodes下线流量会直接转发到下一个nodes打高了负载。
          每个physical映射到多个virtual节点上， 多个virtual nodes更加均匀， 避免了单个node下线流量打高的问题, 不同node不同权重能分配负载
    3. 应用场景:
        * 缓存系统
        * 分布式系统
        * 负载均衡
"""

class ConsistentHash:
    def __init__(self, num_replicas=10, num_machines=1):
        """consistent hash

            Args:
                num_replicas (int, optional): number of virtual machines per physical machine. Defaults to 10.
                num_machines (int, optional): number of physical machines. Defaults to 1.
        """

        # (hash, replica, machine)
        hash_ring = [(self._hash_digest(str(machine) + "-" + str(replica)), replica, machine)
            for machine in range(num_machines)
            for replica in range(num_replicas)
        ]

        # Sort the hash tuples based on hash order by asc
        hash_ring.sort(key=lambda x: x[0])

        self.hash_ring = hash_ring
        print(f"hash_ring: {self.hash_ring}")

    def _hash_digest(self, key):
        # Use MD5 to create a hash of the given key
        m = hashlib.md5()
        m.update(bytes(key, "utf-8"))
        return int(m.hexdigest(), 16)

    def find_lvb_info(self, key):
        """
            return: the last ring info which will process this key
        """
        # Get the hash of the key
        key_hash = self._hash_digest(key)

        # Get the index of the machine which will process this key
        index = bisect.bisect(self.hash_ring, (key_hash,))

        # If the index is larger than the length of the hash_ring, we wrap around
        index = index if index < len(self.hash_ring) else 0

        return self.hash_ring[index]

    def get_machine(self, key):
        """
            return: the last machine which will process this key
        """
        return self.find_lvb_info(key)[2]


tasksCount = 100
tasks = ["taskNum" + str(t) for t in range(tasksCount)]
ch = ConsistentHash(num_machines=5, num_replicas=5)
count = {}
for task in tasks:
    print(f"{task} will be processed on machine {ch.get_machine(task)}, lvbInfo: {ch.find_lvb_info(task)}")

    machinedId = ch.get_machine(task)
    count.update({machinedId: count.get(machinedId, 0) + 1})

print("\nAll machines load information: ", count)

