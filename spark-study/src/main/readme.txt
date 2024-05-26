九项基本原则:
1.避免创建重复的RDD
2.尽可能重复使用同一个RDD
3.对多次使用的RDD进行持久化
4.尽量避免使用shuffle类算子
5.使用map-side预聚合的shuffle操作（也就是使用reduceByKey而不是groupByKey）
6.使用使用高性能算子
    使用reducebukey/aggregate 代替groupbykey
    使用mappartitions代替map
    使用foreachPartitions替代foreach
    使用filter之后进行coalesce操作
    使用repartitionAndSortWithinPartitions 代替repartition与sort类操作
7.广播中等规模的变量(几百M左右)
8.使用Kryo优化序列化性能
0.优化数据结构