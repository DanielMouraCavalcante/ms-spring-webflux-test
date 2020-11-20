package br.com.nkt.springwebflux.conf;

import org.springframework.cloud.gateway.handler.AsyncPredicate;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Configuration
public class GatewayRouter {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
            .route(p -> p
                .path("/v1/gateway")
                .filters(f -> f.addRequestHeader("header_name", "header_value")
                    .rewritePath("/gateway", "/v1/order"))
                .uri("http://localhost:8091")
                  .predicate(applyOrder()))
            .route(p -> p
                .path("/v1/gateway")
                .filters(f -> f.addRequestHeader("header_name", "header_value")
                    .rewritePath("/gateway", "/v1/deposit"))
                .uri("http://localhost:8082")
                .predicate(applyDeposit()))
            .build();
    }

    public Predicate<ServerWebExchange> applyOrder() {
        return (ServerWebExchange serverWebExchange) -> {
            Flux<DataBuffer> dataBufferFlux = serverWebExchange.getRequest().getBody();
            return false;
        };
    }

    public Predicate<ServerWebExchange> applyDeposit() {
        return (ServerWebExchange serverWebExchange) -> {
            Flux<DataBuffer> dataBufferFlux = serverWebExchange.getRequest().getBody();
            return true;
        };
    }

}
