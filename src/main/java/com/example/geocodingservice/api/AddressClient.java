package com.example.geocodingservice.api;

public interface AddressClient {
    String getAddressByCoords(double lon, double lat);
}
