package com.wowhubb.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wowhubb.Activity.LandingPageActivity;
import com.wowhubb.Activity.ShareThoughtsActivity;
import com.wowhubb.Adapter.PaginationAdapter;
import com.wowhubb.FeedsData.Doc;
import com.wowhubb.FeedsData.Feeds;
import com.wowhubb.FeedsData.Message;
import com.wowhubb.Fonts.FontsOverride;
import com.wowhubb.GetFilePathFromDevice;
import com.wowhubb.R;
import com.wowhubb.Utils.ApiClient;
import com.wowhubb.Utils.ApiInterface;
import com.wowhubb.Utils.ConstantVariables;
import com.wowhubb.Utils.PaginationScrollListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EventFeeds extends Fragment {

    private static final String TAG = "MainActivity";
    private static final int PAGE_START = 1;
    private static final int INTENT_REQUEST_GET_COVERIMAGES = 11;
    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    TextView todayevent;
    RecyclerView rv;
    ProgressBar progressBar;
    String token, personalimage, selectedCoverFilePath;
    EditText sharethougts_lv;
    ImageView profile_iv;
    LinearLayout piclv;
    FloatingActionButton fab;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int total, limit;
    private int currentPage = PAGE_START;
    private ApiInterface movieService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eventfeed, container, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // todayevent = view.findViewById(R.id.todayevent);
        token = sharedPreferences.getString("token", "");
        personalimage = sharedPreferences.getString("profilepath", "");
        FontsOverride.overrideFonts(getActivity(), view);
        rv = (RecyclerView) view.findViewById(R.id.listview);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        sharethougts_lv = view.findViewById(R.id.sharethougts_lv);
        profile_iv = view.findViewById(R.id.imageview_profile);
        piclv = view.findViewById(R.id.piclv);
        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LandingPageActivity.class);
                intent.putExtra("fabstatus", "true");
                startActivity(intent);
            }
        });


        if (personalimage != null) {
            Glide.with(this).load("http://104.197.80.225:3010/wow/media/personal/" + personalimage).into(profile_iv);
        } else {
            profile_iv.setImageResource(R.drawable.profile_img);
        }

        adapter = new PaginationAdapter(getActivity());

        sharethougts_lv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ShareThoughtsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
        piclv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickIntent.setType("image/*");
                    startActivityForResult(pickIntent, INTENT_REQUEST_GET_COVERIMAGES);
                }

            }
        });

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                Log.e("tag", "scroll cp----------->" + currentPage);
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return 5;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        movieService = ApiClient.getClient().create(ApiInterface.class);

        loadFirstPage();


        return view;

    }


    private void loadFirstPage() {
        Log.e("tag", "---cp------ " + currentPage);
        //Call<Feeds> call = movieService.checkLogin("1", "application/x-www-form-urlencoded", ConstantVariables.TOKEN,currentPage);
        // currentPage = 1;
        callTopRatedMoviesApi().enqueue(new Callback<Feeds>() {
            @Override
            public void onResponse(Call<Feeds> call, Response<Feeds> response) {
                if (response.isSuccessful()) {
                    Log.e("tag", "response----------->" + response.body().toString());
                    List<Doc> results = fetchResults(response);
                    Log.e("tag", "TOTAL_PAGES----------->" + TOTAL_PAGES);

                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);

                    if (currentPage <= TOTAL_PAGES) {
                        if (currentPage == TOTAL_PAGES) {
                            isLastPage = true;
                        } else {
                            adapter.addLoadingFooter();
                            currentPage += 1;

                            // mocking network delay for API call
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadNextPage();
                                }
                            }, 1000);
                        }

                    } else {
                        isLastPage = true;
                    }
                } else {


                }
            }

            @Override
            public void onFailure(Call<Feeds> call, Throwable t) {
                t.printStackTrace();

            }
        });


    }


    private List<Doc> fetchResults(Response<Feeds> response) {
        Feeds feeds = response.body();
        Message msg = feeds.getMessage();
        TOTAL_PAGES = msg.getPages();
        currentPage = Integer.parseInt(msg.getPage());
        total = msg.getTotal();
        limit = msg.getLimit();

        return msg.getDocs();
    }


    private void loadNextPage() {

        Log.e("tag", "---cpnexttt------ " + currentPage);
        callTopRatedMoviesApi().enqueue(new Callback<Feeds>() {
            @Override
            public void onResponse(Call<Feeds> call, Response<Feeds> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Doc> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) {
                    adapter.addLoadingFooter();
                    // currentPage += 1;
                } else {
                    isLastPage = true;
                    Log.e("tag", "---cpnextttJJJJJJJJJJJJJJJJJJ------ ");

                }
            }

            @Override
            public void onFailure(Call<Feeds> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
            }
        });

    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<Feeds> callTopRatedMoviesApi() {
        return movieService.checkLogin("application/x-www-form-urlencoded", ConstantVariables.TOKEN, currentPage);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("tag", "<<<<---------------Pause---------->>>>");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("tag", "<<<<---------------Ressume---------->>>>");
        //movieService = ApiClient.getClient().create(ApiInterface.class);
        //  loadFirstPage();

    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        if ((result == PackageManager.PERMISSION_GRANTED) && (result1 == PackageManager.PERMISSION_GRANTED) && (result2 == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
            return false;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            Log.e("tag", "code------------->" + resultCode);
            if (requestCode == INTENT_REQUEST_GET_COVERIMAGES) {
                Uri selectedMediaUri = data.getData();
                selectedCoverFilePath = GetFilePathFromDevice.getPath(getActivity(), selectedMediaUri);

                if (selectedCoverFilePath != null && !selectedCoverFilePath.equals("null"))
                {
                    Intent intent = new Intent(getActivity(), ShareThoughtsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Bundle bundle = new Bundle();
                    bundle.putString("selectedCoverFilePath", selectedCoverFilePath);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                }


            }

        }

    }


}
