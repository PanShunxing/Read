package com.read.pan.network.api;

import com.read.pan.entity.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pan on 2016/5/21.
 */
public interface BookApi {
    @GET("book/top")
    /**
     * 获得书城
     * @param offset 起始位置
     * @param limit 刷新条数
     */
    Call<List<Book>> topList(@Query("offset") int offset, @Query("limit") int limit);

    @GET("book/{bookId}")
    /**
     * 获得书城
     * @param bookId 起始位置
     * @param userId 刷新条数
     */
    Call<Book> book(@Path("bookId") String bookId,@Query("userId") String userId);

    @GET("book/like/{name}")
    /**
     * 搜索书籍
     * @param offset 起始位置
     * @param limit 刷新条数
     */
    Call<List<Book>> search(@Path("name") String name,@Query("offset") int offset, @Query("limit") int limit);

    @GET("book/type/{type}")
    /**
     * 不同类别书籍
     * @param offset 起始位置
     * @param limit 刷新条数
     */
    Call<List<Book>> type(@Path("type") int type,@Query("offset") int offset, @Query("limit") int limit);
}
