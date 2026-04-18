package com.fixit.user.infraestructure.adapters.driven.feign.clients;

import com.fixit.user.infraestructure.adapters.driven.feign.configuration.CustomFeignErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "fixit-task",
        url = "${external.services.fixit-tasks-service.url}/api/v1/tasks",
        configuration = CustomFeignErrorDecoder.class
)
public interface ITaskFeignClient {

}