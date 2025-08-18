package com.JavaSpring.Bank.dto.external;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalUserDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private AddressDTO address;
    private CompanyDTO company;
    
    // Nested classes for complex objects
    public static class AddressDTO {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private GeoDTO geo;
        
        // Getters and setters
    }
    
    public static class GeoDTO {
        private String lat;
        private String lng;
        
        // Getters and setters
    }
    
    public static class CompanyDTO {
        private String name;
        @JsonProperty("catchPhrase")
        private String catchPhrase;
        private String bs;
        
        // Getters and setters
    }
    
    // Getters and setters for main class
}