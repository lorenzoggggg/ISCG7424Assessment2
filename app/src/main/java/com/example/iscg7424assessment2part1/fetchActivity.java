package com.example.iscg7424assessment2part1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;

public class fetchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DogAdapter dogAdapter;
    private Button fetchButton;
    private Spinner breedSpinner;
    private String selectedBreed = "retriever";
    private final String[] breedList = {
            "random",
            "affenpinscher",
            "african",
            "airedale",
            "akita",
            "appenzeller",
            "australian/kelpie",
            "australian/shepherd",
            "bakharwal/indian",
            "basenji",
            "beagle",
            "bluetick",
            "borzoi",
            "bouvier",
            "boxer",
            "brabancon",
            "briard",
            "bulldog/boston",
            "bulldog/english",
            "bulldog/french",
            "bullterrier/staffordshire",
            "cattledog/australian",
            "cavapoo",
            "chihuahua",
            "chippiparai/indian",
            "chow",
            "clumber",
            "cockapoo",
            "collie/border",
            "coonhound",
            "corgi/cardigan",
            "cotondetulear",
            "dachshund",
            "dalmatian",
            "dane/great",
            "danish/swedish",
            "deerhound/scottish",
            "dhole",
            "dingo",
            "doberman",
            "elkhound/norwegian",
            "entlebucher",
            "eskimo",
            "finnish/lapphund",
            "frise/bichon",
            "gaddi/indian",
            "germanshepherd",
            "greyhound/indian",
            "greyhound/italian",
            "groenendael",
            "havanese",
            "hound/afghan",
            "hound/basset",
            "hound/blood",
            "hound/english",
            "hound/ibizan",
            "hound/plott",
            "hound/walker",
            "husky",
            "keeshond",
            "kelpie",
            "kombai",
            "komondor",
            "kuvasz",
            "labradoodle",
            "labrador",
            "leonberg",
            "lhasa",
            "malamute",
            "malinois",
            "maltese",
            "mastiff/bull",
            "mastiff/english",
            "mastiff/indian",
            "mastiff/tibetan",
            "mexicanhairless",
            "mix",
            "mountain/bernese",
            "mountain/swiss",
            "mudhol/indian",
            "newfoundland",
            "otterhound",
            "ovcharka/caucasian",
            "papillon",
            "pariah/indian",
            "pekinese",
            "pembroke",
            "pinscher/miniature",
            "pitbull",
            "pointer/german",
            "pointer/germanlonghair",
            "pomeranian",
            "poodle/medium",
            "poodle/miniature",
            "poodle/standard",
            "poodle/toy",
            "pug",
            "puggle",
            "pyrenees",
            "rajapalayam/indian",
            "redbone",
            "retriever/chesapeake",
            "retriever/curly",
            "retriever/flatcoated",
            "retriever/golden",
            "ridgeback/rhodesian",
            "rottweiler",
            "saluki",
            "samoyed",
            "schipperke",
            "schnauzer/giant",
            "schnauzer/miniature",
            "segugio/italian",
            "setter/english",
            "setter/gordon",
            "setter/irish",
            "sharpei",
            "sheepdog/english",
            "sheepdog/indian",
            "sheepdog/shetland",
            "shiba",
            "shihtzu",
            "spaniel/blenheim",
            "spaniel/brittany",
            "spaniel/cocker",
            "spaniel/irish",
            "spaniel/japanese",
            "spaniel/sussex",
            "spaniel/welsh",
            "spitz/indian",
            "spitz/japanese",
            "springer/english",
            "stbernard",
            "terrier/american",
            "terrier/australian",
            "terrier/bedlington",
            "terrier/border",
            "terrier/cairn",
            "terrier/dandie",
            "terrier/fox",
            "terrier/irish",
            "terrier/kerryblue",
            "terrier/lakeland",
            "terrier/norfolk",
            "terrier/norwich",
            "terrier/patterdale",
            "terrier/russell",
            "terrier/scottish",
            "terrier/sealyham",
            "terrier/silky",
            "terrier/tibetan",
            "terrier/toy",
            "terrier/welsh",
            "terrier/westhighland",
            "terrier/wheaten",
            "terrier/yorkshire",
            "tervuren",
            "vizsla",
            "waterdog/spanish",
            "weimaraner",
            "whippet",
            "wolfhound/irish"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchButton = findViewById(R.id.btnFetch);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDogImages();
            }
        });

        breedSpinner = findViewById(R.id.breedSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, breedList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breedSpinner.setAdapter(adapter);

        breedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBreed = breedList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void fetchDogImages() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dog.ceo/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DogApiService apiService = retrofit.create(DogApiService.class);

        Call<DogResponse> call;
        if (selectedBreed.equals("random")) {
            call = apiService.getRandomDogImage();
        } else if (selectedBreed.contains("/")) {
            String[] parts = selectedBreed.split("/");
            call = apiService.getImagesBySubBreed(parts[0], parts[1]);
        } else {
            call = apiService.getRandomImageByBreed(selectedBreed);
        }

        call.enqueue(new retrofit2.Callback<DogResponse>() {
            @Override
            public void onResponse(Call<DogResponse> call, Response<DogResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> imageUrls = response.body().getMessage();
                    dogAdapter = new DogAdapter(fetchActivity.this, imageUrls);
                    recyclerView.setAdapter(dogAdapter);
                }
            }

            @Override
            public void onFailure(Call<DogResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(fetchActivity.this, "Failed to fetch puppies :/", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
