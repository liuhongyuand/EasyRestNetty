package com.example.rest;

import com.easyrest.annotations.bean.BindController;
import com.easyrest.annotations.method.BindURL;
import com.easyrest.annotations.method.Get;
import com.easyrest.annotations.method.Post;
import com.easyrest.model.ResponseEntity;
import com.example.controller.StockInfoRestController;
import com.example.model.Stock;

import java.util.List;

@BindURL("/rest/{TENANT}/stock")
@BindController(StockInfoRestController.class)
public interface StockInfoRest {

    @Post("/personal/{USER_ID}/favorite/{CODE}")
    void addFavorite(String USER_ID, String CODE, long time);

    @Post
    ResponseEntity addStocks(int userNumber, String userName, List<Stock> stockList);

    @Get("/personal/{USER_ID}/favorite/list")
    List<Stock> getStockList(String USER_ID);

}