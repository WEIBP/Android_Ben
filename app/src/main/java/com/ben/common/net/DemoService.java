package com.ben.common.net;

import com.ben.core.worktest.PeopleBean;
import com.ben.core.worktest.PeopleListBean;
import com.ben.core.worktest.ResponseBean;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by Su on 2016/11/11.
 * Retrofit服务接口的一些常用示例（来源于官方文档），GET,POST,常用注解
 * http://square.github.io/retrofit/
 */

public interface DemoService {

    @GET("contact/list/")
    Observable<PeopleListBean> getList();

    @POST("contact/new/") Observable<ResponseBean> addPeople(@Body PeopleBean people);

    @POST("contact/delete/{id}/")
    Observable<ResponseBean> deletePeople(@Path("id") int id);

    /**
     * 1、@replacement blocks与@path
     */
    @GET("group/{id}/users") Observable<List<DemoUser>> groupList(@Path("id") int groupId);

    /**
     * 2、@Query,附带参数，等同于@GET("group/{id}/users?sort=value")
     */
    @GET("group/{id}/users") Observable<List<DemoUser>> groupList(@Path("id") int groupId,
        @Query("sort") String sort);

    /**
     * 3、@QueryMap，携带多个参数
     */
    @GET("group/{id}/users") Observable<List<DemoUser>> groupList(@Path("id") int groupId,
        @QueryMap Map<String, String> options);

    /**
     * 4、@POST
     * 这里唯一需要说明的就是，通过@BODY这种方式封装请求体，Retrofit是通过JSON的形式来封装数据的
     */
    @POST("users/new") Observable<DemoUser> createUser(@Body DemoUser user);

    /**
     * 5、@FormUrlEncoded
     * 想要通过request.getParameter的方式直接读取参数信息
     * 同理多个@Field 可以参考@QueryMap使用@FieldMap
     */
    @FormUrlEncoded
    @POST("User/edit") Observable<DemoUser> updateUser(@Field("first_name") String first,
        @Field("last_name") String last);


    /**
     * 6、@Headers与@Header
     * 使用了@FormUrlEncoded之后,参数中含有中文信息，出现乱码,以通过URLEncode对数据进行指定编码，然后服务器再进行对应的解码读取
     * String name =  URLEncoder.encode("张德帅","UTF-8");
     * Call<ResponseInfo> call = service.uploadNewUser(name,"male",24);
     * 另一种解决方式：在Request-Header中设置charset信息
     * 通过@Headers我们在Content-type中同时指明了编码信息，再次运行程序测试，就会发现服务器正确读取到了中文的信息
     * <p>
     * 除了@Headers之外，还有另一个注解叫做@Header。它的不同在于是动态的来添加请求头信息，也就是说更加灵活一点。
     */
    @Headers("Content-type:application/x-www-form-urlencoded;charset=UTF-8")
    @FormUrlEncoded
    @POST("api/users") Observable<DemoUser> uploadNewUser(@Field("username") String username,
        @Field("gender") String male, @Field("age") int age);


    /**
     * 7、@Multipart
     */
    @Multipart
    @PUT("User/photo") Observable<DemoUser> updateUser(@Part("photo") RequestBody photo,
        @Part("description") RequestBody description);
}