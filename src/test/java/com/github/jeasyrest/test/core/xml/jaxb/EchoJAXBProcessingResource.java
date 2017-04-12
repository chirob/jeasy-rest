package com.github.jeasyrest.test.core.xml.jaxb;

import com.github.jeasyrest.core.xml.JAXBProcessingResource;

public class EchoJAXBProcessingResource extends JAXBProcessingResource<Customer, Customer> {

    @Override
    public Customer process(Customer request) {
        return request;
    }

    protected EchoJAXBProcessingResource() {
        super(Customer.class, Customer.class);
    }
    
}
