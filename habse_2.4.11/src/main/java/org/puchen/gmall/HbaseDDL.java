package org.puchen.gmall;

import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 重量级
 */
public class HbaseDDL {

    // 添加静态属性 connection 指向单例连接
    public static Connection connection = HBaseConnect.connection;

    /**
     * 创建命名空间
     *
     * @param namespace 命名空间名称
     */
    public static void createNamespace(String namespace) throws
            IOException {
        // 1. 获取 admin
        // 此处的异常先不要抛出  等待方法写完  再统一进行处理
        // admin 的连接是轻量级的  不是线程安全的  不推荐池化或者缓存这个连接
        Admin admin = connection.getAdmin();
        // 2. 调用方法创建命名空间
        // 代码相对 shell 更加底层  所以 shell 能够实现的功能 代码一定能实现

        // 所以需要填写完整的命名空间描述
        // 2.1 创建命令空间描述建造者 => 设计师
        NamespaceDescriptor.Builder builder =
                NamespaceDescriptor.create(namespace);
        // 2.2 给命令空间添加需求
        builder.addConfiguration("user", "puchen");
        // 2.3 使用 builder 构造出对应的添加完参数的对象  完成创建  // 创建命名空间出现的问题  都属于本方法自身的问题  不应该抛出
        try {
            admin.createNamespace(builder.build());
        } catch (IOException e) {
            System.out.println("命令空间已经存在");
            e.printStackTrace();
        }
        // 3. 关闭 admin admin.close();
        admin.close();
    }

    /**
     * 判断表格是否存在
     *
     * @param namespace 命名空间名称
     * @param tableName 表格名称
     * @return ture 表示存在
     */
    public static boolean isTableExists(String namespace, String tableName) throws IOException {
        // 1. 获取 admin
        Admin admin = connection.getAdmin();
        // 2. 使用方法判断表格是否存在
        boolean b = false;
        try {
            b = admin.tableExists(TableName.valueOf(namespace,
                    tableName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 admin admin.close();
        // 3. 返回结果
        return b;
        // 后面的代码不能生效
    }


    /**
     * 创建表格
     *
     * @param namespace      命名空间名称
     * @param tableName      表格名称
     * @param columnFamilies 列族名称  可以有多个
     */
    public static void createTable(String namespace, String
            tableName, String... columnFamilies) throws IOException { // 判断是否有至少一个列族
        if (columnFamilies.length == 0) {
            System.out.println("创建表格至少有一个列族");
            return;
        }
        // 判断表格是否存在
        if (isTableExists(namespace, tableName)) {
            System.out.println("namespace:" + namespace + "-" + "tableName:" + tableName + "->" + "表格已经存在");
            return;
        }
        // 1.获取 admin
        Admin admin = connection.getAdmin();
        // 2. 调用方法创建表格
        // 2.1 创建表格描述的建造者
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(TableName.valueOf(namespace, tableName));
        // 2.2 添加参数
        for (String columnFamily : columnFamilies) {
            // 2.3 创建列族描述的建造者
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily));
            // 2.4 对应当前的列族添加参数 // 添加版本参数
            columnFamilyDescriptorBuilder.setMaxVersions(5); // 2.5 创建添加完参数的列族描述
            tableDescriptorBuilder.setColumnFamily(columnFamilyDescriptorBuilder.build());
        }

        // 2.6 创建对应的表格描述
        try {
            admin.createTable(tableDescriptorBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 admin
        admin.close();
    }

    /**
     * 修改表格中一个列族的版本
     *
     * @param namespace    命名空间名称
     * @param tableName    表格名称
     * @param columnFamily 列族名称
     * @param version      版本
     */
    public static void modifyTable(String namespace, String tableName, String columnFamily, int version) throws IOException {
        // 判断表格是否存在
        if (!isTableExists(namespace, tableName)) {
            System.out.println("表格不存在无法修改");
            return;
        }
        // 1. 获取 admin
        Admin admin = connection.getAdmin();
        try {
            // 2. 调用方法修改表格
            // 2.0 获取之前的表格描述
            TableDescriptor descriptor = admin.getDescriptor(TableName.valueOf(namespace, tableName));
            // 2.1 创建一个表格描述建造者
            // 如果使用填写 tableName 的方法  相当于创建了一个新的表格描述建造者  没有之前的信息
            // 如果想要修改之前的信息   必须调用方法填写一个旧的表格描述
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(descriptor);

            // 2.2 对应建造者进行表格数据的修改
            ColumnFamilyDescriptor columnFamily1 = descriptor.getColumnFamily(Bytes.toBytes(columnFamily));
            // 创建列族描述建造者
            // 需要填写旧的列族描述
            ColumnFamilyDescriptorBuilder columnFamilyDescriptorBuilder = ColumnFamilyDescriptorBuilder.newBuilder(columnFamily1);
            // 修改对应的版本
            columnFamilyDescriptorBuilder.setMaxVersions(version);
            // 此处修改的时候  如果填写的新创建   那么别的参数会初始化
            tableDescriptorBuilder.modifyColumnFamily(columnFamilyDescriptorBuilder.build());
            admin.modifyTable(tableDescriptorBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 3. 关闭 admin admin.close();
    }

    /**
     * @param namespace
     * @param tableName
     * @return
     */
    public static boolean delteTable(String namespace, String tableName) throws IOException {

        if (!isTableExists(namespace, tableName)) {
            System.out.println("表不存在,无法删除!");
            return false;
        }

        Admin admin = connection.getAdmin();
        try {
            TableName tableNameDel = TableName.valueOf(namespace,tableName);
            //删除表格表格之前 一定要先笔记表格不可用
            admin.disableTable(tableNameDel);
            admin.deleteTable(tableNameDel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        admin.close();
        return true;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(isTableExists("bigdata", "student"));

        createTable("bigdata", "student", "info");
//
//        //修改表格
//        modifyTable("bigdata", "student1111", "info", 4);
//
//        delteTable("bigdata", "student222");

        System.out.println("其他代码");
        HBaseConnect.closeConnection();
    }

}