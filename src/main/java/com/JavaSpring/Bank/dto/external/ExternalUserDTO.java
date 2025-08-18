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
    
    // Constructors
    public ExternalUserDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
    
    public AddressDTO getAddress() { return address; }
    public void setAddress(AddressDTO address) { this.address = address; }
    
    public CompanyDTO getCompany() { return company; }
    public void setCompany(CompanyDTO company) { this.company = company; }
    
    // Nested classes
    public static class AddressDTO {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private GeoDTO geo;
        
        public AddressDTO() {}
        
        // Getters and Setters
        public String getStreet() { return street; }
        public void setStreet(String street) { this.street = street; }
        
        public String getSuite() { return suite; }
        public void setSuite(String suite) { this.suite = suite; }
        
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        
        public String getZipcode() { return zipcode; }
        public void setZipcode(String zipcode) { this.zipcode = zipcode; }
        
        public GeoDTO getGeo() { return geo; }
        public void setGeo(GeoDTO geo) { this.geo = geo; }
    }
    
    public static class GeoDTO {
        private String lat;
        private String lng;
        
        public GeoDTO() {}
        
        // Getters and Setters
        public String getLat() { return lat; }
        public void setLat(String lat) { this.lat = lat; }
        
        public String getLng() { return lng; }
        public void setLng(String lng) { this.lng = lng; }
    }
    
    public static class CompanyDTO {
        private String name;
        @JsonProperty("catchPhrase")
        private String catchPhrase;
        private String bs;
        
        public CompanyDTO() {}
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getCatchPhrase() { return catchPhrase; }
        public void setCatchPhrase(String catchPhrase) { this.catchPhrase = catchPhrase; }
        
        public String getBs() { return bs; }
        public void setBs(String bs) { this.bs = bs; }
    }
}