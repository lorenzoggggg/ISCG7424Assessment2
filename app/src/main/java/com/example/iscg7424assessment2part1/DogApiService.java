package com.example.iscg7424assessment2part1;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DogApiService {
    @GET("breeds/image/random/10")
    Call<DogResponse> getRandomDogImage();

    // Endpoint for a random image from a specific breed
    @GET("breed/{breed}/images/random/10")
    Call<DogResponse> getRandomImageByBreed(@Path("breed") String breed);

    @GET("breed/{breed}/{subBreed}/images/random/10")
    Call<DogResponse> getImagesBySubBreed(@Path("breed") String breed, @Path("subBreed") String subBreed);
}
