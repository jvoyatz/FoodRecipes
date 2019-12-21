package gr.jvoyatz.foodrecipes.requests;

import gr.jvoyatz.foodrecipes.util.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class ServiceGenerator {
    private static Retrofit.Builder retrofitBuilder;


    private ServiceGenerator() {
/*        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
*/

        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                //  .client(client)
                .addConverterFactory(GsonConverterFactory.create());

        retrofit = retrofitBuilder.build();

        recipeApi = retrofit.create(RecipeApi.class);
    }

    private static Retrofit retrofit /*= retrofitBuilder.build()*/;

    private static RecipeApi recipeApi /*= retrofit.create(RecipeApi.class)*/;

    static RecipeApi getRecipeApi() {
        if (recipeApi == null) {
            new ServiceGenerator();
        }

        return recipeApi;
    }
}
