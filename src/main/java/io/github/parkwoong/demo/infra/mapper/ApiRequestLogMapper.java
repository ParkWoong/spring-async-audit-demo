package io.github.parkwoong.demo.infra.mapper;

import io.github.parkwoong.demo.infra.entity.RequestLog;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApiRequestLogMapper {

    @Insert(
            "insert into api_request_log values (#{tYmdhms}, #{randomId}, #{userId}, #{clientId},"
                + " #{tokenKeyId}, #{requestUri}, #{requestHeader}, #{requestBody},"
                + " #{responseCode})")
    int insertRequestHistory(RequestLog history);

    @Select("select count(*) from api_request_log")
    int countRequestHistory();
}
