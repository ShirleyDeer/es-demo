package cn.itcast.es.test;

import cn.itcast.es.pojo.Item;
import com.google.gson.Gson;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author deer
 * @version v1.0
 * @date 2019-05-22 16:39
 * @description TODO
 **/



//dev lxl ddd second commit 123
public class ElasticSearchTest {

    RestHighLevelClient client;

    @Before
    public void setClient(){
         client = new RestHighLevelClient(
                RestClient.builder(HttpHost.create("http://192.168.143.130:9201"),
                        HttpHost.create("http://192.168.143.130:9202"),
                        HttpHost.create("http://192.168.143.130:9203")
                ));
    }
    @After
    public void close() throws IOException {
        client.close();
    }

    private Gson gson =new Gson();
    @Test
    public void add() throws IOException {
        IndexRequest request = new IndexRequest("posts", "doc", "1");

        Item item = new Item(1L, "小米手机9", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");

       request.source(gson.toJson(item), XContentType.JSON);

        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    public void query() throws IOException {
       GetRequest request = new GetRequest("posts", "doc", "1");
       //查询请求
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        //解析查询到的数据
        String source = response.getSourceAsString();
        Item item = gson.fromJson(source, Item.class);

    }

    @Test
    public void delete() throws IOException {
        DeleteRequest request = new DeleteRequest("posts", "doc", "1");

        DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);


    }
    @Test
    public void bulk(){
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));


        BulkRequest bulkRequest = new BulkRequest();
        for (Item item : list) {
            bulkRequest.add(new IndexRequest("item","docs",item.getId().toString()).source(gson.toJson(item),XContentType.JSON));
        }

    }
}

