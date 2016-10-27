package com.nklmish.cs.catalogs.bounday;

import com.nklmish.cs.catalogs.model.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class CatalogService {

    private final RestTemplate restTemplate;

    private final SpanAccessor spanAccessor;

    @Autowired
    public CatalogService(RestTemplate restTemplate, SpanAccessor spanAccessor) {
        this.restTemplate = restTemplate;
        this.spanAccessor = spanAccessor;
    }

    @RequestMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Catalog createCatalog(@PathVariable Integer id) throws InterruptedException {
        spanAccessor.getCurrentSpan().logEvent("price.fetch");
        String price = fetchPrice(id);
        return new Catalog(id, price, new ArrayList<>());
    }

    private String fetchPrice(Integer id) {
        return restTemplate.
                getForObject(
                        "http://localhost:8070/{id}",
                        String.class,
                        id);
    }
}
