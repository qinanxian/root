package com.vekai.batch.reader;

import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.MapObjectCruder;
import cn.fisok.sqloy.core.PaginResult;
import cn.fisok.sqloy.core.PaginQuery;
import org.springframework.batch.item.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * 读取MAP类型的数据
 */
public class MapDataReader implements ItemReader<MapObject> ,ItemStream{
    private MapObjectCruder mapObjectCruder;
    private String sql = "";
    private Map<String, Object> parameter = MapKit.newHashMap();
    private List<String> columns = ListKit.newArrayList();
    private String keyColumn = "";  //主要标识字段，用于标识读到哪一条记录了
    private int pageSize = 100;
    private List<MapObject> pageDataList;

    public MapObjectCruder getmapObjectCruder() {
        return mapObjectCruder;
    }

    public void setmapObjectCruder(MapObjectCruder mapObjectCruder) {
        this.mapObjectCruder = mapObjectCruder;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        this.parameter = parameter;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    private int pageCount = -1;
    private int curPageIndex = 0;
    protected List<MapObject> fetchDataList(){
        //单页数据使用完了之后，才重新查询一页新的数据
        if(pageDataList == null || pageDataList.size()==0){
            PaginQuery query = new PaginQuery();
            query.setQuery(sql);
            query.setSize(pageSize);
            query.setIndex(curPageIndex ++);

            query.setParameterMap(parameter);

            PaginResult<MapObject> paginationData = mapObjectCruder.selectListPagination(query,(ResultSet rs, int rowNumber)->{
                MapObject row = new MapObject();
                return row;
            });

            if(pageCount < 0) pageCount = paginationData.getPageCount();
            curPageIndex = paginationData.getIndex();

            pageDataList = paginationData.getDataList();
        }

        return pageDataList;
    }

    public MapObject read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        return null;
    }


    public void open(ExecutionContext executionContext) throws ItemStreamException {

    }

    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

    public void close() throws ItemStreamException {

    }
}