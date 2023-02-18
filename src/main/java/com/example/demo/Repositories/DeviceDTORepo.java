package com.example.demo.Repositories;

import com.example.demo.DTO.DeviceDTO;
import com.example.demo.Entity.Device;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface DeviceDTORepo extends ReactiveCrudRepository<DeviceDTO, Long> {

    @Query(value = "select od.id,\n" +
            "\t   od.data_of_create,\n" +
            "\t   od.is_name, \n" +
            "\t   od.name, \n" +
            "\t   od.path_file, \n" +
            "\t   od.price, \n" +
            "\t   ob.name as brand_name,\n" +
            "\t   ot.name as type_name\n" +
            "\tfrom os_device od\n" +
            "\tjoin os_brand ob ON ob.id = od.brand_id\n" +
            "\tjoin os_type ot ON ot.id = od.type_id\n" +
            "\twhere ot.\"name\" = :typeName \n" +
            "\tand CAST(price AS integer) >= :minPrice\n" +
            "\tand CAST(price AS integer) <= :maxPrice \n" +
            "\torder by data_of_create desc\n" +
            "\toffset :offset\n" +
            "\tlimit :limit;")
    Flux<DeviceDTO> findAllWithParamsWithoutBrands(
            @Param("typeName") String typeName,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            @Param("offset") int offset,
            @Param("limit") int limit
    );


    @Query(value = "select od.id,\n" +
            "\t   od.data_of_create,\n" +
            "\t   od.is_name, \n" +
            "\t   od.name, \n" +
            "\t   od.path_file, \n" +
            "\t   od.price, \n" +
            "\t   ob.name as brand_name,\n" +
            "\t   ot.name as type_name\n" +
            "\tfrom os_device od\n" +
            "\tjoin os_brand ob ON ob.id = od.brand_id\n" +
            "\tjoin os_type ot ON ot.id = od.type_id\n" +
            "\twhere ot.\"name\" = :typeName \n" +
            "\tand ob.\"name\" in (:brandsName)\n" +
            " \tand CAST(price AS integer) >= :minPrice\n" +
            "\tand CAST(price AS integer) <= :maxPrice \n" +
            "\torder by data_of_create DESC\n" +
            "\toffset :offset\n" +
            "\tLIMIT :limit;")
    Flux<DeviceDTO> findAllWithParamsWithBrands(
            @Param("typeName") String typeName,
            @Param("brandsName") List<String> brandsName,
            @Param("minPrice") int minPrice,
            @Param("maxPrice") int maxPrice,
            @Param("offset") int offset,
            @Param("limit") int limit
    );


//    Flux<Device> findAllWithParamsWithBrands(Long id);

}
