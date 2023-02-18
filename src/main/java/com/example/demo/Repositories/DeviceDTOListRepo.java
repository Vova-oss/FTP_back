package com.example.demo.Repositories;

import com.example.demo.DTO.DeviceDTOList;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeviceDTOListRepo extends ReactiveCrudRepository<DeviceDTOList, Long> {

    @Query(value = "select max(CAST(price AS integer)) as max_price,\n" +
            "\t   min(CAST(price AS integer)) as min_price,\n" +
            "\t   count(*) as amount_of_all_devices\n" +
            "\tfrom os_device od\n" +
            "\tjoin os_type ot ON ot.id = od.type_id\n" +
            "\twhere ot.\"name\" = :typeName")
    Mono<DeviceDTOList> findByParamsWithoutBrands(@Param("typeName") String typeName);


    @Query(value = "select max(CAST(price AS integer)) as max_price,\n" +
            "\t   min(CAST(price AS integer)) as min_price,\n" +
            "\t   count(*) as amount_of_all_devices\n" +
            "\tfrom os_device od\n" +
            "\tjoin os_type ot ON ot.id = od.type_id\n" +
            "\tjoin os_brand ob ON ob.id = od.brand_id\n" +
            "\twhere ot.\"name\" = :typeName \n" +
            "\tand ob.\"name\" in (:brandsName);")
    Mono<DeviceDTOList> findByParamsWithBrands(
            @Param("typeName") String typeName,
            @Param("brandsName") List<String> brandsName
    );

}
