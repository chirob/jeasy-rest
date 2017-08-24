package com.github.jeasyrest.test.core.json;

import com.github.jeasyrest.core.process.json.JSONProcessingResource;
import com.github.jeasyrest.test.core.xml.jaxb.Customer;

public class EchoJSONProcessingResource extends JSONProcessingResource<Customer, Customer> {

    @Override
    public Customer process(Customer request, Method method) {
        request.setAge(Integer.parseInt(getParameters()[0]));
        return request;
    }

    protected EchoJSONProcessingResource() {
        super(Customer.class, Customer.class);
    }
    
}
