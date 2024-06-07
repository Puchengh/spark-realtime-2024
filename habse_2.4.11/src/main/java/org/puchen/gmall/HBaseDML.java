package org.puchen.gmall;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 轻量级
 */
public class HBaseDML {
    public static Connection connection = HBaseConnect.connection;

    /**
     * @param nameSpace    命名空间
     * @param tableName    表格名称
     * @param rowKey       主键
     * @param columnFamily 列族名称
     * @param columnName   列名
     * @param value        值
     */
    public static void putCell(String nameSpace, String tableName, String rowKey, String columnFamily, String columnName, String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(nameSpace, tableName));

        //2 调用方法插入数据
        Put put = new Put(Bytes.toBytes(rowKey));

        //3.给pue对象添加数据
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName), Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.close();
    }

    /**
     * 读取对应的一行中的某一列
     *
     * @param nameSpace    命名空间
     * @param tableName    表格名称
     * @param rowKey       主键
     * @param columnFamily 列族名称
     * @param columnName   列名
     */
    public static void getCell(String nameSpace, String tableName, String rowKey, String columnFamily, String columnName) throws IOException {

        //1.获取table对象
        Table table = connection.getTable(TableName.valueOf(nameSpace, tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        //读取对应值的数据
        get.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(columnName));

        get.readAllVersions();
        try {
            //读取一整行的数据
            Result result = table.get(get);

            //处理数据  测试方法把读取的数据打印到控制台 如果是实际开发  需要在额外写方法 对应处理数据
            Cell[] cells = result.rawCells();

            for (Cell cell : cells) {
                String value = new String(CellUtil.cloneValue(cell));
                System.out.println(value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 扫描数据
     *
     * @param nameSpace 命名空间
     * @param tableName 表格名称
     * @param startRow  开始的row  包含
     * @param endRow    结束的row  不包含
     */
    public static void scanRows(String nameSpace, String tableName, String startRow, String endRow) throws IOException {
        Table table = connection.getTable(TableName.valueOf(nameSpace, tableName));

        //如果此时不填则会直接扫描整个表
        Scan scan = new Scan();
        //添加参数
        scan.withStartRow(Bytes.toBytes(startRow));
        scan.withStopRow(Bytes.toBytes(endRow));  //不包含

        try {
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print(new String(CellUtil.cloneRow(cell)) + "-" + new String(CellUtil.cloneFamily(cell)) + "-" +
                            new String(CellUtil.cloneQualifier(cell)) + "-" + new String(CellUtil.cloneValue(cell)) + '\t');

                }
                System.out.println();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        table.close();

    }


    /**
     * 扫描数据
     *
     * @param nameSpace    命名空间
     * @param tableName    表格名称
     * @param startRow     开始的row  包含
     * @param endRow       结束的row  不包含
     * @param columnFanily 列族
     * @param column       字段
     * @param value        值
     */
    public static void filterScan(String nameSpace, String tableName, String startRow, String endRow, String columnFanily, String column, String value) throws IOException {
        Table table = connection.getTable(TableName.valueOf(nameSpace, tableName));

        //如果此时不填则会直接扫描整个表
        Scan scan = new Scan();
        //添加参数
        scan.withStartRow(Bytes.toBytes(startRow));
        scan.withStopRow(Bytes.toBytes(endRow));  //不包含

        //添加一个过滤  可以添加多个过滤
        FilterList filterList = new FilterList();

        //创建过滤器
        //(1)结果只保留当前列的数据
        ColumnValueFilter columnValueFilter = new ColumnValueFilter(
                Bytes.toBytes(columnFanily),
                Bytes.toBytes(column),
                CompareOperator.EQUAL,
                Bytes.toBytes(value)
        );
//        filterList.addFilter(columnValueFilter);
        //(2)结果保留整行数据 包含其他  结果同时会保留没有当前列的数据

        SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
                Bytes.toBytes(columnFanily),
                Bytes.toBytes(column),
                CompareOperator.EQUAL,
                Bytes.toBytes(value)
        );
        filterList.addFilter(singleColumnValueFilter);
        scan.setFilter(filterList);

        try {
            ResultScanner scanner = table.getScanner(scan);
            for (Result result : scanner) {
                Cell[] cells = result.rawCells();
                for (Cell cell : cells) {
                    System.out.print(new String(CellUtil.cloneRow(cell)) + "-" + new String(CellUtil.cloneFamily(cell)) + "-" +
                            new String(CellUtil.cloneQualifier(cell)) + "-" + new String(CellUtil.cloneValue(cell)) + '\t');

                }
                System.out.println();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        table.close();

    }

    /**
     * @param nameSpace
     * @param tableName
     * @param rowKey
     * @param columnFanily
     * @param column
     * @param value
     */
    public static void delteColumn(String nameSpace, String tableName, String rowKey, String columnFanily, String column) throws IOException {

        Table table = connection.getTable(TableName.valueOf(nameSpace, tableName));
        Delete delete = new Delete(
                Bytes.toBytes(rowKey)

        );
//        delete.addColumn()  //删除一个版本的数据
//        delete.addColumns()  //删除所有版本的数据
        delete.addColumn(Bytes.toBytes(columnFanily), Bytes.toBytes(column));
        delete.addColumns(Bytes.toBytes(columnFanily), Bytes.toBytes(column));

        try {
            table.delete(delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        table.close();
    }


    public static void main(String[] args) throws IOException {
//        putCell("bigdata", "student", "2001","info","name","wangwu");
//        putCell("bigdata", "student", "2001","info","name","zhaoliu");
//
//        putCell("bigdata", "student", "1001","info","name","zhaoliu");
//        putCell("bigdata", "student", "1002","info","name","zhaoliu");
//        putCell("bigdata", "student", "1003","info","name","zhaoliu");
//        putCell("bigdata", "student", "1004","info","name","zhaoliu");
//
//        getCell("bigdata", "student", "2001","info","name");


//        1001-info-name-zhaoliu
//        1002-info-name-zhaoliu
//        scanRows("bigdata", "student", "1001", "1003");

//        filterScan("bigdata", "student", "1001", "1006", "info", "name", "zhangsan");

        delteColumn("bigdata", "student", "2001","info","name");

        System.out.println("====结束====");

        HBaseConnect.closeConnection();
    }
}
