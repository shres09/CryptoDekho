package com.cryptowatch.api;

import com.cryptowatch.models.CryptoOhlcv;
import com.cryptowatch.models.Cryptocurrency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CryptoCompareService {
    // FIXME: change this
    @Headers("authorization: a2a5d85fc7adf8aad75411e9b3ab29c65ea3e40a49e5d4d0faa05f241a6f147f")
    @GET("top/mktcapfull?tsym=EUR&limit=10")
    Call<List<Cryptocurrency>> getToplistByMarketCap();

    // FIXME: change this
    @Headers("authorization: a2a5d85fc7adf8aad75411e9b3ab29c65ea3e40a49e5d4d0faa05f241a6f147f")
    @GET("v2/histoday?&tsym=EUR&limit=10")
    Call<List<CryptoOhlcv>> getDailyOhlcv(@Query("fsym") String id);
}
